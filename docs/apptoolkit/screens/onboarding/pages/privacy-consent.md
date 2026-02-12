# Firebase consent onboarding page

This document describes the **Firebase consent onboarding page** used to present privacy / consent
choices to the user. The UI is inspired by **Cookiebot-style consent dialogs**, but is implemented
**natively with Jetpack Compose** (no WebView, no embedded third-party UI).

It consists of:
- An onboarding page (`FirebaseOnboardingPage`)
- A toggle card (`UsageAndDiagnosticsToggleCard`)
- A multi-tab dialog (`FirebaseConsentDialog`) with pager pages:
  - `ConsentPage`
  - `DetailsPage`
  - `AboutPage`
- Reusable consent item UI (`ConsentExpandableItemCard`)
- A privacy policy section (`PrivacyPolicySection`)

---

## Goals

The Firebase consent page should:

- Explain why "Usage & diagnostics" exists (Crashlytics / diagnostics / measurement)
- Allow users to **enable/disable usage & diagnostics** quickly
- Provide a **clear, Cookiebot-like** consent dialog:
  - Simple intro (Consent tab)
  - Granular controls (Details tab)
  - More context (About tab)
- Persist consent choices through the diagnostics feature module (`UsageAndDiagnosticsViewModel`)
- Keep onboarding state simple: show/hide dialog and continue onboarding flow

---

## Architecture overview

### ViewModels used

This page composes **two view models**:

- `OnboardingViewModel`
  - Controls onboarding-only UI state (dialog visibility, current tab, completion flow)
  - State: `OnboardingUiState(isCrashlyticsDialogVisible, ...)`

- `UsageAndDiagnosticsViewModel`
  - Owns the actual consent values and persistence
  - State: `UsageAndDiagnosticsUiState(...)`

The onboarding page itself is "dumb":
- It renders UI from both states
- It forwards events to the appropriate ViewModel

---

## FirebaseOnboardingPage

### Entry point

```kotlin
fun FirebaseOnboardingPage(isSelected: Boolean)
````

* `isSelected` is important to avoid showing dialogs for non-visible pages in a pager.
* The consent dialog is displayed only when:

    * the page is currently selected AND
    * onboarding UI state says it should be visible

```kotlin
if (isSelected && onboardingUiState.isCrashlyticsDialogVisible) { ... }
```

### Core UI elements

1. **Icon + title + description** (Crashlytics / diagnostics context)
2. **UsageAndDiagnosticsToggleCard**
3. **"Show details" button** → opens the dialog
4. **PrivacyPolicySection** → opens app privacy policy in a browser

---

## UsageAndDiagnosticsToggleCard

A clickable Material card that toggles the high-level flag:

* Click plays sound + haptic feedback
* Clicking anywhere toggles (not just the switch)
* The switch uses icons (`Analytics` / `Policy`) for checked/unchecked

State is driven by:

* `diagnosticsUiState.usageAndDiagnostics`

Persistence is done via:

```kotlin
diagnosticsViewModel.onEvent(UsageAndDiagnosticsEvent.SetUsageAndDiagnostics(isChecked))
```

---

## FirebaseConsentDialog

A custom Compose `Dialog` that behaves like a Cookiebot-style consent surface:

* Not dismissible by outside click or back press:

  ```kotlin
  DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
  ```
* Height adapts to window size (≈ 78% of the available height), with a minimum:

    * Ensures consistent “modal sheet” feel across devices

### Header

* Security icon (`Icons.Outlined.Security`)
* Title: `onboarding_crashlytics_dialog_privacy_choices_title`

### Tabs + pager

The dialog uses:

* `PrimaryTabRow`
* `HorizontalPager` with `rememberPagerState`

Tabs:

* Consent
* Details
* About

Pages:

* `ConsentPage()` (intro + learn more message)
* `DetailsPage(...)` (granular toggles)
* `AboutPage()` (explainer copy)

---

## ConsentPage (intro)

A simple scrollable column that:

* Explains what the dialog is about
* Shows an info section (`InfoMessageSection`) with:

    * a “Learn more” affordance
    * a link to Google app consent guidance

This page is meant to mirror Cookiebot’s first tab: “what is this” + “why do we ask”.

---

## DetailsPage (granular controls)

This page exposes the granular consent switches from `UsageAndDiagnosticsUiState`:

* Analytics storage
* Ad storage
* Ad user data
* Ad personalization

Each option is rendered with:

* `ConsentExpandableItemCard`

Each option:

* has a short summary + detailed explanation
* has a switch controlling its specific consent flag
* supports “Learn more” via `openUrl(url)`

State values come from:

* `UsageAndDiagnosticsUiState`:

    * `analyticsConsent`
    * `adStorageConsent`
    * `adUserDataConsent`
    * `adPersonalizationConsent`

Callbacks are directly mapped to events in `UsageAndDiagnosticsViewModel`.

---

## ConsentExpandableItemCard

This is the reusable “Cookiebot-like” item row:

* A compact row with:

    * expand/collapse chevron (ExpandLess/ExpandMore)
    * title
    * switch (with icon + blocked icon)
* Below the row:

    * combined summary + details text (two-sentence format)
* When expanded:

    * an `OutlinedCard` “provider” card (“Google”) with open-in-new icon
    * clicking opens Google privacy page

Interaction details:

* Expand tap: click sound + haptics
* Switch toggle: click sound + haptics
* Expansion state is `rememberSaveable`

---

## Footer actions

The dialog provides actions similar to consent platforms:

* **Allow all**

    * Grants all granular consents + enables usage & diagnostics
    * Closes the dialog

* **Allow essentials**

    * Enables essentials + disables user-data/personalization
    * Enables usage & diagnostics
    * Closes the dialog

* **Confirm choices**

    * Keeps current selection as-is
    * Closes the dialog

These are wired in `FirebaseOnboardingPage` by dispatching multiple `UsageAndDiagnosticsEvent`s.

---

## PrivacyPolicySection

Displayed at the bottom of the onboarding page (outside the dialog):

* Shows a short privacy message
* Provides a button to open the app privacy policy (`AppLinks.PRIVACY_POLICY`)
* Uses `startActivitySafely` and shows a Toast on failure

---

## UX notes (Cookiebot inspiration, Compose-native)

* The dialog mirrors the familiar structure of cookie consent UIs:

    * Intro / Consent
    * Granular toggles / Details
    * Explanation / About
* Implementation is **100% Compose-native**:

    * `Dialog`, `TabRow`, `HorizontalPager`, custom cards/switches
    * No WebView, no HTML-based consent layer
* This approach keeps:

    * consistent Material3 styling
    * full control over animations and accessibility
    * predictable state and persistence

---

## Accessibility and behavior

Recommended guarantees (and currently aligned with the UI patterns in code):

* Dialog is modal and blocks dismissal until a user action is chosen
* Controls are reachable and readable on smaller screens due to scrolling pages
* Switches are operable both via switch tap and row tap where applicable
* Icons are decorative where appropriate (contentDescription = null)

---

## Related docs

* `pages.md` – onboarding pages and selection rules (`isSelected`)
* `theme.md` – theme onboarding page and DataStore persistence
* `settings/privacy/ads.md` – ad consent and ad-related settings (if you surface them elsewhere)
