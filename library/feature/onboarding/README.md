# `:library:feature:onboarding` Logic Graph

## Purpose

Owns the multi-page onboarding flow of a first launch, including theme choice and completion
persistence. It is a start screen of the shell, drawn before its tabs, and follows the startup
screen of [`:library:feature:startup`](../startup/README.md).

## Owns

- `onboardingPages()`, which registers `OnboardingRoute` as a page without a title and offers it
  as a start screen (`startScreens`).
- The onboarding screen, its ViewModel and state, event and action contracts, and the page glue
  that performs their actions: the consent check and `enterShell()` on completion.
- `onboardingModule`, which binds `OnboardingThemeViewModel`.
- `OnboardingThemeViewModel`, which keeps the theme onboarding page independent from DataStore and
  exposes the shared immutable theme-preferences model.
- The `OnboardingProvider` host extension contract.
- Onboarding repository, page models, controls, and the theme and finish pages.

## Does not own

- The startup screen before it, `StartupProvider` and the permission request, owned by
  [`:library:feature:startup`](../startup/README.md), which hands over to `OnboardingRoute` by key.
- Deciding whether the first launch runs. The app starts on `StartupRoute` from
  `ShellHost(resolveStart = ...)` while its startup flag is set; see [Using it](#using-it).
- Host-specific onboarding provider implementations, owned by `:sample:feature:onboarding`.
- Consent SDK orchestration, owned by `:library:integration:consent`.
- The diagnostics page and the privacy choices dialog it opens, owned by
  [`:library:feature:diagnostics`](../diagnostics/README.md) (`FirebaseOnboardingPage`) along with
  the state they read and write. A host lists it among its pages through `OnboardingProvider`.
- Theme implementation and settings repositories, owned by core design system and DataStore.

## Depends on

- `:library:core:common`, `:library:core:datastore`, `:library:core:network`, and `:library:core:ui`
  for shared contracts, completion persistence, errors, and UI.
- [`:library:navigation`](../../navigation/README.md) for the keys, the graph builder and the
  shell navigator.
- [`:library:integration:consent`](../../integration/consent/README.md) for the consent form, run
  through `ConsentHost(activity)`.
- No other feature module.

## Used by

- [`:library:apptoolkit`](../../apptoolkit/README.md), which calls `onboardingPages()` from
  `toolkitPages()`, and `:sample:feature:onboarding`, which provides the pages and permissions.

## Using it

```kotlin
ShellHost(
    graph = graph,
    resolveStart = { if (dataStore.startup.first()) StartupRoute else startKeyFor(storedTab) },
    onReady = { keepSplashVisible = false },
)
```

The startup screen hands over to onboarding. Neither has the shell under it, so back from either
leaves the app. Finishing onboarding
writes completion and enters the shell on its start tab; the next launch then resolves past
`StartupRoute`.

## Flow chart

```mermaid
flowchart TD
    Host[ShellHost resolveStart] -->|startup flag set| Startup[StartupRoute page, feature:startup]
    Host -->|otherwise| Tabs[Shell tabs]
    Startup -->|continueStart| Onboarding[OnboardingRoute page]
    Onboarding --> ConsentCheck[Consent check on each resume]
    Onboarding --> Pages[Provider-defined ordered pages]
    Pages --> Theme[OnboardingThemeViewModel]
    Theme --> ThemeRepo[ThemePreferencesRepository]
    Pages --> Diagnostics[FirebaseOnboardingPage from feature:diagnostics]
    Onboarding --> VM[OnboardingViewModel]
    VM -->|final confirmation| Completion[OnboardingRepository]
    Completion --> Store[Preferences DataStore]
    VM -->|completed| Enter[enterShell]
    Enter --> Tabs
```

## Architectural decisions

- Start screens, not activities. The first launch runs inside the one activity, so consent and
  permission requests go through `LocalActivity.current`, and the shell's start is decided once in
  `resolveStart` instead of by a launcher activity that forwarded to the main one.
- Startup and onboarding are separate modules: startup asks for permissions and consent, while
  onboarding owns page progress and completion. They meet only at `OnboardingRoute`, a key in
  `:library:navigation`.
- Each action reaches a single collector, the page glue in `OnboardingPages.kt`; the screens only
  send events.
- The host supplies page/routing extension points, but toolkit state holders persist confirmed
  choices. Presentation callbacks do not write DataStore directly.
- Theme and consent pages use their owning repositories/ViewModels so onboarding does not become a
  second implementation of settings behavior.
- On large screens the pages keep to a column at most 640dp wide, centred, so a tablet shows them
  at a readable width instead of stretched across the window.
- Completion is written only after the final confirmed action; navigation is emitted separately as
  a one-off effect.

## Public contracts

- `onboardingPages()`, `onboardingModule`, the onboarding provider contract, repository and
  models, and the presentation entry points.

## Internal implementations

- Page ordering/rendering, completion persistence adapter, and celebration state.

## Current risks

- The app decides when the first launch runs. An app that forgets the `resolveStart` check never
  shows onboarding, and one that never clears its startup flag shows it on every launch.
- The module coordinates consent, persisted theme state and the host's pages; changes require
  checking several module contracts together.
