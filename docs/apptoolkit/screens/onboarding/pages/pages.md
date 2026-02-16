# Onboarding pages

This document describes how onboarding content is modeled and rendered in AppToolkit using
`OnboardingPage`. It covers the two page types (**DefaultPage** and **CustomPage**), how the
selection lifecycle works (`isSelected`), and recommended patterns for performant, predictable pages.

---

## Page model

Onboarding content is represented by a sealed class:

- `OnboardingPage.DefaultPage` – simple, deterministic pages (title/description/icon).
- `OnboardingPage.CustomPage` – fully custom composable pages that can react to being selected.

```kotlin
sealed class OnboardingPage {

    data class DefaultPage(
        val key: String,
        val title: String,
        val description: String,
        val imageVector: ImageVector,
        val isEnabled: Boolean = true
    ) : OnboardingPage()

    data class CustomPage(
        val key: String,
        val content: @Composable (isSelected: Boolean) -> Unit,
        val isEnabled: Boolean = true
    ) : OnboardingPage()
}
````

### Keys

Each page must have a stable `key`:

* used for stability in lists/pagers
* used for analytics / logging / debugging
* used to preserve per-page UI state if you key compositions

Recommended format:

* `"welcome"`, `"theme"`, `"privacy"`, `"finish"`
* or namespaced keys for libraries: `"apptoolkit:privacy"` / `"hostapp:welcome"`

---

## Default pages

`DefaultPage` is intended for "classic onboarding" content:

* offline-friendly (no network required)
* deterministic (no side effects)
* fast to render
* consistent look across host apps

### Rendering

Default pages are typically rendered with:

* `DefaultOnboardingPage(page: OnboardingPage.DefaultPage)`
* `DefaultOnboardingIconContainer(imageVector)`

**Behavior:**

* vertically scrollable content (so long translations don't clip)
* centered layout and large typography
* icon container uses `LoadingIndicator` as an animated background layer

Example usage:

```kotlin
val pages = listOf(
    OnboardingPage.DefaultPage(
        key = "welcome",
        title = stringResource(R.string.onboarding_welcome_title),
        description = stringResource(R.string.onboarding_welcome_desc),
        imageVector = Icons.Outlined.AutoAwesome
    ),
    OnboardingPage.DefaultPage(
        key = "permissions",
        title = stringResource(R.string.onboarding_permissions_title),
        description = stringResource(R.string.onboarding_permissions_desc),
        imageVector = Icons.Outlined.Security
    )
)
```

---

## Custom pages

`CustomPage` is used when a page needs more than text + icon:

* consent / dialogs (Crashlytics / ads consent)
* theme pickers with multiple controls and previews
* animated or interactive pages
* pages that depend on view models and flows

### Selection-aware content

A custom page receives `isSelected: Boolean`.

This is important because onboarding pages are usually inside a pager:

* multiple pages may be kept alive in composition
* side effects should run only when the page is active

Recommended rule:

> Only show dialogs, run expensive animations, or start "active" work when `isSelected == true`.

Example:

```kotlin
OnboardingPage.CustomPage(
    key = "privacy",
    content = { isSelected ->
        FirebaseOnboardingPage(isSelected = isSelected)
    }
)
```

---

## Enabling and filtering pages

Both page types expose:

* `isEnabled: Boolean`

This allows:

* feature-flagging pages
* device-dependent pages (e.g., dynamic color only on Android 12+)
* build variant pages (debug-only)

Common approach:

```kotlin
val enabledPages = pages.filter { page ->
    when (page) {
        is OnboardingPage.DefaultPage -> page.isEnabled
        is OnboardingPage.CustomPage -> page.isEnabled
    }
}
```

Recommended: filter **before** you create your pager to keep indices stable.

---

## Pager coordination

### Tracking the current page

The onboarding flow typically keeps:

* `currentTabIndex` in `OnboardingUiState`
* updates it via `OnboardingEvent.UpdateCurrentTab(index)`

This allows:

* footer / next/back to stay consistent
* analytics and debugging (“which page is user on?”)
* host logic to react to specific pages if needed

### Footer controls

`OnboardingFooter` expects:

* `PagerState`
* `pageCount`
* `onNextClicked()`
* `onBackClicked()`

Recommended behavior:

* on last page: Next becomes Done (check icon) and triggers completion
* block “Done” if required choices aren’t made (optional host logic)

---

## State ownership

### Default pages

Keep them stateless. They should render from params only.

### Custom pages

Prefer state in view models when:

* choices are persisted (DataStore)
* choices affect app configuration (theme/consent)
* multiple components need the same state

Prefer local Compose state when:

* it’s purely visual and not needed elsewhere (expanded card state, temporary UI toggles)
* it doesn’t need to survive process death

---

## Best practices

### 1) Keep pages pure by default

* Avoid network calls from pages.
* Avoid doing work in composition that can be done once in a VM.

### 2) Use `isSelected` for side effects

* dialogs
* analytics impressions (optional)
* expensive animations
* permission prompts

### 3) Make keys stable and human-readable

This makes logs and crash reports useful.

### 4) Don’t couple pages tightly

Custom pages can use their own view models (like diagnostics/theme), but the onboarding flow should
stay a thin coordinator.

### 5) Keep long content scrollable

Both default and complex pages should handle small screens and large font sizes gracefully.

---

## Examples used in AppToolkit

* **Default page**:

    * `DefaultOnboardingPage` + `DefaultOnboardingIconContainer`
* **Privacy/consent custom page**:

    * `FirebaseOnboardingPage(isSelected)`
    * shows `FirebaseConsentDialog` only when selected
* **Theme custom page**:

    * `ThemeOnboardingPageTab()`
    * theme mode + AMOLED + palette selection

---

## Related docs

* `../onboarding.md` – feature overview, architecture, and event/action contracts
* `privacy-consent.md` – Crashlytics/ads consent UI and rules
* `theme.md` – theme onboarding behavior and persistence
* `../onboarding.md` – host app integration, launch flow, and completion handling