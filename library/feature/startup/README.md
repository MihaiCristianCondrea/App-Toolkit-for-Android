# `:library:feature:startup` Logic Graph

## Purpose

Owns the startup screen, the first start screen of a first launch: it asks for the host's runtime
permissions and the consent form, then hands over to onboarding. It is a start screen of the
shell, drawn before its tabs.

## Owns

- `startupPage()`, which registers `StartupRoute` as a page without a title and offers it as a
  start screen (`startScreens`).
- `StartupScreen`, `StartupViewModel`, their state, event and action contracts, and the page glue
  that performs their actions: the permission request, the consent form, and handing over with
  `continueStart(OnboardingRoute)`.
- `StartupProvider`, the host extension contract: the runtime permissions to ask for.
- `startupModule(startupProviderFactory)`, which binds the host's provider and the ViewModel.
- The startup illustration and animation, and the welcome, agree, learn more and terms strings, in
  every supported locale.

## Does not own

- Deciding whether the first launch runs. The app starts on `StartupRoute` from
  `ShellHost(resolveStart = ...)` while its startup flag is set; see [Using it](#using-it).
- The onboarding pages that follow, owned by [`:library:feature:onboarding`](../onboarding/README.md).
  This module opens `OnboardingRoute` by key and does not depend on it.
- The host's provider, owned by the app (`:sample:feature:startup` in the sample).
- Consent SDK orchestration, owned by `:library:integration:consent`.

## Depends on

- `:library:core:common`, `:library:core:network` and `:library:core:ui` for shared contracts,
  data states and UI.
- [`:library:navigation`](../../navigation/README.md) for the keys, the graph builder and the
  shell navigator.
- [`:library:integration:consent`](../../integration/consent/README.md) for the consent form, run
  through `ConsentHost(activity)`.
- Lottie, for the welcome animation.
- No other feature module.

## Used by

- [`:library:apptoolkit`](../../apptoolkit/README.md), which calls `startupPage()` from
  `toolkitPages()` and `startupModule` from `appToolkitModules`, and `:sample:feature:startup`,
  which provides the permissions.

## Using it

```kotlin
ShellHost(
    graph = graph,
    resolveStart = { if (dataStore.startup.first()) StartupRoute else startKeyFor(storedTab) },
    onReady = { keepSplashVisible = false },
)
```

The screen has no shell under it, so back leaves the app. Continuing replaces it with
`OnboardingRoute`, whose completion enters the shell; the next launch then resolves past
`StartupRoute`.

## Flow chart

```mermaid
flowchart TD
    Host[ShellHost resolveStart] -->|startup flag set| Startup[StartupRoute page]
    Host -->|otherwise| Tabs[Shell tabs]
    Startup --> Permissions[StartupProvider.requiredPermissions, once]
    Startup --> ConsentForm[ConsentHost: consent form]
    Startup -->|continue| Continue[continueStart: OnboardingRoute]
    Continue --> Onboarding[":library:feature:onboarding"]
```

## Architectural decisions

- Its own module, not part of onboarding. Asking for permissions and consent is a different job
  from the onboarding pages, an app can replace one without the other, and the sample keeps the
  same split (`:sample:feature:startup`).
- A start screen, not an activity: consent and permission requests go through
  `LocalActivity.current`.
- Permissions are requested once per screen instance, saved across the resume the system dialog
  causes, so a person who declined is not asked again on the spot. Consent is asked for on each
  resume until it has resolved.
- Each action reaches a single collector, the page glue in `StartupPages.kt`; the screen only
  sends events.
- On large screens the content keeps to a column at most 640dp wide, centred, as onboarding's
  pages do.

## Public contracts

- `startupPage()`, `startupModule`, `StartupProvider`, and the presentation entry points.

## Internal implementations

- The page glue and the screen's layout.

## Current risks

- The app decides when the first launch runs. An app that forgets the `resolveStart` check never
  shows startup, and one that never clears its startup flag shows it on every launch.
- Handing over by key means an app that registers `StartupRoute` without `OnboardingRoute` sends
  the person to a key the graph does not know.
