# Onboarding (AppToolkit)

The **Onboarding** feature provides a reusable, Compose-first onboarding flow that host apps can plug
in to handle:

- "first run" gating (is onboarding completed?)
- a multi-page onboarding pager (default pages + fully custom pages)
- privacy / diagnostics consent UX (Crashlytics + ad-related consents)
- theme personalization (mode, AMOLED, dynamic/static palettes, seasonal palettes)
- completion signaling back to the host

It is built around the same AppToolkit pattern used by other screens:
**Data → Domain → UI**, with a `LoggedScreenViewModel` driving state and one-shot actions.

---

## Architecture

### Data
- **`OnboardingPreferencesDataSource`**
  - `startup: Flow<Boolean>` emits if this is the first launch.
  - `saveStartup(isFirstTime: Boolean)` persists the first-launch flag.
  - The repository interprets this as onboarding completion state.

### Domain
- **Repository**
  - `OnboardingRepository`
    - `observeOnboardingCompletion(): Flow<Boolean>`
    - `setOnboardingCompleted()`
  - `OnboardingRepositoryImpl`
    - maps `startup` (isFirstTime) → **completed = !isFirstTime**
    - uses `distinctUntilChanged()` to avoid redundant UI updates
- **Use cases**
  - `ObserveOnboardingCompletionUseCase`
    - thin wrapper over the repository flow (keeps domain clean)
  - `CompleteOnboardingUseCase`
    - marks onboarding as completed (writes `startup=false`)

### UI (Compose)
- **ViewModel**
  - `OnboardingViewModel : LoggedScreenViewModel<OnboardingUiState, OnboardingEvent, OnboardingAction>`
- **State**
  - `OnboardingUiState`
    - `currentTabIndex`: current onboarding pager page
    - `isOnboardingCompleted`: completion flag (derived + persisted)
    - `isCrashlyticsDialogVisible`: privacy dialog gate (defaults true)
- **Contracts**
  - `OnboardingEvent`
    - observe completion, update page index, request consent, complete, show/hide dialog
  - `OnboardingAction`
    - `OnboardingCompleted` (one-shot signal to close onboarding / navigate)

---

## Data flow

### 1) Completion observation
- `OnboardingViewModel` starts with `OnboardingEvent.ObserveCompletion` in `init`.
- It collects `ObserveOnboardingCompletionUseCase()`.
- Each emission updates:
  - `OnboardingUiState.isOnboardingCompleted`

Why this matters:
- the UI can gate whether onboarding should be shown at all
- completion changes propagate instantly (DataStore → Flow → UI)

### 2) Completion write
- When the user finishes onboarding:
  - `OnboardingEvent.CompleteOnboarding`
  - calls `CompleteOnboardingUseCase()` on `dispatchers.io`
  - updates state (`isOnboardingCompleted=true`)
  - emits `OnboardingAction.OnboardingCompleted`

### 3) Consent request
- `OnboardingEvent.RequestConsent(host)`
- calls `RequestConsentUseCase(host)`
- logged with host metadata (`extra = host.activity::class.java.name`)
- no state mutation on failure (by design — onboarding continues)

---

## Pages and UI components

### Page model: `OnboardingPage`
- **DefaultPage**
  - title, description, image vector (offline, deterministic)
- **CustomPage**
  - allows arbitrary composable content:
    - `content: @Composable (isSelected: Boolean) -> Unit`
  - `isSelected` lets custom pages react only when visible (dialogs, animations, etc.)

### Default onboarding visuals
- `DefaultOnboardingPage`
  - scrollable column, centered content
  - `DefaultOnboardingIconContainer`
    - uses a `LoadingIndicator` as a playful animated backdrop
- `OnboardingFooter`
  - back / next / done buttons + page dots
  - spring animations + visibility transitions
- `PageIndicatorDots`
  - animated dot scaling for current page

### Finish page
- `FinishOnbardingPage`
  - enter animation (scale + alpha + translation)
  - Konfetti shown once globally using `FinalOnboardingKonfettiState`

> Note: the "show once globally" behavior means if onboarding is re-entered in the same process,
> konfetti won’t rerun. That’s intentional for delight without spam.

---

## Privacy & diagnostics onboarding (Firebase / Crashlytics page)

### `FirebaseOnboardingPage(isSelected)`
This page integrates two independent ViewModels:
- `OnboardingViewModel` (page/dialog orchestration)
- `UsageAndDiagnosticsViewModel` (consent + toggles)

Main UI:
- icon + title + description
- `UsageAndDiagnosticsToggleCard` (single switch)
- "Show details" button → opens `FirebaseConsentDialog`
- `PrivacyPolicySection` (opens app privacy policy link safely)

Dialog behavior:
- dialog shows only when:
  - `isSelected == true` AND
  - `onboardingUiState.isCrashlyticsDialogVisible == true`
- actions inside the dialog:
  - **Allow all** → enables analytics + all ad-related consents + usage/diagnostics
  - **Allow essentials** → enables analytics + ad storage, disables ad user data & personalization
  - **Confirm choices** → closes dialog without forcing values

Consent detail UI:
- `FirebaseConsentDialog`
  - 3 tabs: Consent / Details / About
  - pager-based content
  - explicit buttons at the bottom (no outside/back dismiss)
- `ConsentExpandableItemCard`
  - expands to show provider card ("Google") with learn-more deep link
  - includes switch for that consent item

---

## Theme onboarding

### `ThemeOnboardingPageTab()`
This tab is focused on "make the app feel like the system":
- theme mode selection: light / dark / follow system
- AMOLED toggle (only allowed when not in light mode)
- palette selection:
  - dynamic palettes (Android 12+) with variant swatches
  - static palettes as fallback
  - seasonal palettes (Christmas/Halloween) supported with badge
  - dedupe + include selected palette even if seasonal filtering changes

Persistence:
- uses shared `commonDataStore` methods:
  - `saveThemeMode`, `saveAmoledMode`
  - `saveDynamicColors`, `saveDynamicPaletteVariant`
  - `saveStaticPaletteId`

---

## Logging and observability

### `LoggedScreenViewModel`
`OnboardingViewModel` uses the same operational logging pattern as other screens:
- `startOperation(action, extra)`
- `launchReport(...)`
- `catchReport(...)`

### Breadcrumb placement (your preferred rule)
You’re already doing the "right place" approach here:

- Domain use case `ObserveOnboardingCompletionUseCase` stays pure (no Firebase).
- ViewModel logs on flow start:

```kotlin
.onStart {
    firebaseController.logBreadcrumb(
        message = "Observe onboarding completion started",
        attributes = mapOf("source" to "ObserveOnboardingCompletionUseCase")
    )
}
````

That matches the guideline you mentioned for Help:
**Firebase logging should live at the VM boundary**, not inside domain.

---

## Integration notes (host apps)

### Showing onboarding

Most host apps will:

1. observe `OnboardingRepository.observeOnboardingCompletion()`
2. show onboarding only if not completed
3. close onboarding on `OnboardingAction.OnboardingCompleted`

### Wiring DataStore implementation

`OnboardingPreferencesDataSource` is intentionally an interface so each host can choose:

* DataStore Preferences
* Proto DataStore
* encrypted storage (if needed)

Only contract requirement:

* `startup: Flow<Boolean>` must reflect the persisted "first time" flag

---

## Customization points

* **Pages**

    * provide a list of `OnboardingPage.DefaultPage` and/or `OnboardingPage.CustomPage`
* **Consent UX**

    * replace `FirebaseOnboardingPage` with your own consent screen
    * keep `UsageAndDiagnosticsViewModel` contract consistent
* **Theme UX**

    * remove dynamic palette UI if the host app doesn't support it
    * adjust seasonal palettes list / filtering rules
* **Finish UX**

    * change konfetti frequency or disable entirely

---

## Suggested doc placement (to match the Help doc style)

* `docs/apptoolkit/screens/onboarding/onboarding.md` (how to launch, state/events/actions, completion flow)
* `docs/apptoolkit/screens/onboarding/pages/pages.md` (DefaultPage vs CustomPage patterns)
* `docs/apptoolkit/screens/onboarding/pages/privacy-consent.md` (FirebaseConsentDialog + rules)
* `docs/apptoolkit/screens/onboarding/pages/theme.md` (theme mode + palettes + seasonal behavior)
