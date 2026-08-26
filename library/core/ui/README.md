# `:library:core:ui` Logic Graph

## Purpose

Provides the reusable Compose presentation foundation: screen/ViewModel contracts, Navigation 3
entry helpers, state handling, analytics hooks, and shared components.

## Owns

- `ScreenViewModel`, `LoggedScreenViewModel`, event/action bases, and `UiStateScreen` handling.
- Navigation entry builders and UI state built on stable keys owned by `:library:navigation`.
- Reusable buttons, fields, preferences, layouts, dialogs, snackbars, ads slots, effects, and
  adaptive-window helpers.
- Render models such as `AppVersionInfo` and `AdsConfig`.
- The shared theme-mode preview composables used by both the onboarding and settings theme UI.

## Does not own

- Business rules, repositories, DTOs, persistence, or HTTP behavior.
- Color palette and root theme construction, owned by [
  `:library:core:designsystem`](../designsystem/README.md).
- Feature screens.

## Depends on

- [`:library:core:common`](../common/README.md) for common models, Firebase contracts, and platform
  helpers.
- [`:library:core:datastore`](../datastore/README.md) for remaining persistence-backed UI adapters;
  reusable modifiers and ad slots consume design-system-provided values rather than DataStore.
- [`:library:core:designsystem`](../designsystem/README.md) for theme primitives.
- [`:library:navigation`](../../navigation/README.md) for shared navigation models and transitions.

## Used by

- `:sample` and `:library:apptoolkit`.
- `:library:feature:about`, `:library:feature:help`, `:library:feature:issuereporter`,
  `:library:feature:onboarding`, `:library:feature:permissions`, `:library:feature:settings`, and
  `:library:feature:support`.
- `:library:integration:ads` for its settings screen and ad presentation.

## Flow chart

```mermaid
flowchart TD
    User[User interaction] --> Screen[Feature composable]
    Screen --> Event[UiEvent]
    Event --> VM[ScreenViewModel]
    VM -->|persistent render state| State[StateFlow of UiStateScreen]
    VM -->|one-off effect| Action[ActionEvent flow]
    State --> Handler[ScreenStateHandler]
    Handler --> Loading[Loading / no-data / error / success]
    Loading --> Screen
    Action --> Host[Navigation, intent, or transient UI handler]
    Theme[AppTheme CompositionLocals] --> Components[Reusable components and ad slots]
    Components --> Screen
    Nav[Navigation entry helpers] --> Screen
```

## Architectural decisions

- Screen state and one-off actions use separate streams so recomposition cannot repeat navigation
  or transient effects.
- `ScreenViewModel` owns unidirectional event-to-state processing; feature composables render data
  and forward user intent rather than reaching repositories.
- `ScreenStateHandler` centralizes loading/no-data/error/success rendering, while feature content
  remains responsible for its successful state.
- Global UI preferences arrive through the design-system root. Reusable components must not start
  their own persistence collectors unless a documented adapter still requires it.

## Public contracts

- All new ViewModels must extend `ScreenViewModel`, or `LoggedScreenViewModel` when Firebase
  breadcrumbs/error reporting are required.
- ViewModels receive events through `onEvent`, expose immutable `UiStateScreen<T>`, and emit one-off
  actions separately.
- Initialization is represented by an event sent from `init`; long-running work is owned and
  cancelled by the ViewModel. Flow pipelines use `catch` and dispatcher selection rather than
  `runCatching` in ViewModels.
- Shared navigation types, state/render models, reusable composables, lifecycle effects, and
  analytics APIs are intentional cross-module contracts.

## Internal implementations

- Component rendering, animation, native-ad hosting, snackbar orchestration, and default
  state-handler behavior.

## Current risks

Feature-specific theme, onboarding-preview, and display-dialog code lives in this generic core
module. The native-ad UI also exposes an advertising concern from the shared UI foundation.

## Migration notes

### Ad surfaces must fail closed and remain host-styleable

Native and banner ad requests previously assumed the Mobile Ads SDK had already initialized. When
the preference/UI path disagreed with initialization, SDK calls could throw synchronously from a
Compose effect and kill the host process — reported as
`IllegalStateException: MobileAds.initialize must be called before using the Google Mobile Ads SDK`
with `NativeAdLoader.load` under `DisposableEffectImpl.onRemembered`. Preserve the current behavior:

- Ad slots consult no preference. The ads-enabled preference that `rememberAdsEnabled` observed is
  gone, and with it the possibility of the UI and `AdsCoreManager` disagreeing about it.
- `rememberNativeAd` and `AdBanner` wait for `AdsSdkState`, retry when readiness changes, and treat
  a
  synchronous SDK exception as a failed/empty ad slot rather than a fatal UI error.
- Loaded native ads are destroyed when their unit ID changes, when a caller passes
  `rememberNativeAd(enabled = false)`, or when the composable leaves composition.
- `NativeAdSlot.containerColor` is a call-site override. Its default remains an unstyled Material
  card, while exceptional toolkit screens and consumer apps may match their own surfaces without
  changing every native ad.
- The shared native-ad CTA view wraps its label and padding; do not restore a toolkit-wide custom
  minimum height merely to align one screen.
- Featured/no-data presentations retain the sponsored-label container, a clipped 16:9 media frame,
  and an end-aligned CTA. Grid presentations keep their content centered.

These are compatibility safeguards for host applications, not incidental styling details.
