---
name: android-ui-layer
description: >
  Design, implement, review, and migrate App Toolkit screens on the core.ui.screen setup: a
  ScreenViewModel or LoggedScreenViewModel, a feature-owned state data class with Loadable fields,
  an event contract, UiMessage queues shown by MessageHost, ScreenStateHandler, TrackScreenState,
  the split between a stateful XScreen and a stateless, previewable XScreenContent in the same
  file, and the
  mapping of thrown failures to error text. Use when creating a screen or ViewModel, adding
  loading, empty, error or custom statuses, deciding what a failure shows, showing snackbars from
  a ViewModel, wiring a ViewModel in Koin, testing a ViewModel or screen content, or moving a
  screen off core.ui.base, core.ui.states or DataState.
metadata:
  author: Mihai-Cristian Condrea
  last-updated: '2026-10-01'
  keywords:
  - android
  - ui layer
  - viewModel
  - ui state
  - jetpack compose
  - screen content
  - unidirectional data flow
  - snackbar
---

# Android UI Layer

Build every screen from the same parts, so a reader always knows where to look.

The building blocks live in `:library:core:ui`, package `core.ui.screen`, which that module's
README documents. `:library:feature:about` is the reference implementation.

Use the sibling skills for what this one does not cover:

- `android-data-layer` for repositories, data sources, threading and caching.
- `android-domain-layer` for whether a use case should sit between the ViewModel and the data.
- `layered-tree-review` for module and package placement in general.
- `testing-setup` for test frameworks, fakes and mocks.

## References

Read the file for the part you are working on:

| File                                 | Read when                                                      |
|--------------------------------------|----------------------------------------------------------------|
| `references/file-tree.md`            | creating a feature or a screen, or deciding where a file goes  |
| `references/state.md`                | shaping `XUiState`, choosing statuses, adding a custom status  |
| `references/viewmodel.md`            | writing operations, streams, job restarts, or checking reports |
| `references/events-and-messages.md`  | adding events, snackbars, or a ViewModel-triggered effect      |
| `references/errors.md`               | deciding what a failure shows, or adding a screen's own errors |
| `references/screen-and-content.md`   | writing `XScreen` or `XScreenContent`, previews, state slots   |
| `references/testing.md`              | testing a ViewModel, a mapper, or screen content               |
| `references/migration.md`            | moving a screen off `core.ui.base`, `core.ui.states`, `DataState` |

`templates/` holds one starting file per part. Copy the ones the screen needs and rename `X`.

## The parts

| Part             | Type                                      | Owns                                                   |
|------------------|-------------------------------------------|--------------------------------------------------------|
| `XViewModel`     | `LoggedScreenViewModel<XUiState, XEvent>` | state, messages, operations, logging                   |
| `XUiState`       | `@Immutable data class`                   | everything the screen renders                          |
| `XEvent`         | `sealed interface`                        | what the user can ask the ViewModel to do              |
| `XScreen`        | stateful `@Composable`, in `XScreen.kt`   | ViewModel, collection, tracking, messages, navigation  |
| `XScreenContent` | stateless `@Composable`, in `XScreen.kt`  | rendering the state, reporting input through callbacks |

State flows down from the ViewModel, events flow up from the content, and messages leave through
their own queue.

## Rules

### ViewModel

- Extend `LoggedScreenViewModel` for every feature screen. Use the bare `ScreenViewModel` only where
  reporting is unwanted, such as a test double.
- Pass `screenName` and `viewModelName` as string literals. R8 renames classes, so a name read from
  the class is unreadable in release reports.
- Handle events in `handleEvent` with an exhaustive `when`; `onEvent` is final and reports the event
  before calling it.
- Change state only through `setState { copy(...) }`. It is atomic, so no mutex is needed.
- Run work through one of three shapes, never a bare `viewModelScope.launch` or `launchIn`, so
  every operation is logged, every failure reported, and cancellation never mistaken for one:
  `launchReport { }` for a suspend call, `flow.collectReport { }` for a flow whose values go into
  state, and `flow.catchReport { }` only for a flow that has to keep going after a failure. A
  stream that is expensive to keep running while nobody looks opts into `flow.observeReport { }`,
  Google's `WhileSubscribed(5_000)` policy; never use it for a flow that must not miss a value.
- Take repositories, use cases where they earn their place, `TelemetryRepository`, and a
  `DispatcherProvider` only for the ViewModel's own CPU work. Never a `Context`, a data source or a
  bare platform wrapper: the platform is reached through a repository, as `ClipboardRepository`.
- Keep action names in a `private object Actions` of `const val`s. They are the `action` values on
  dashboards, so renaming one breaks its history.

### State

- The state is the feature's own `@Immutable data class` in `ui/states/`. There is no shared base
  state type.
- Give each independently loading part its own `Loadable<T>` field. Content present from the start
  needs no status.
- Choose the starting value on purpose: `Loading` when a spinner is right, `Empty` when the screen
  should show nothing until the data arrives.
- When `Loadable` does not fit, declare a feature sealed type implementing `TrackedStatus`, so
  `TrackScreenState` still reports it.
- Use `ImmutableList` and the other `kotlinx.collections.immutable` types, so Compose treats the
  state as stable and skips rows that did not change.
- Hold text the app writes as `UiTextHelper`, so it resolves in the UI against the current locale
  and configuration. Text the user typed stays a `String`.

### Events and messages

- One `sealed interface XEvent` in `ui/contracts/`, made of `data object` and `data class`. The
  breadcrumb reads the event's source name from `toString()`, which R8 keeps; field values are
  never logged.
- Events go in only; there is no `Action` type. Snackbars go out through
  `showMessage(UiMessage(...))`, and `MessageHost` removes each one after it shows, so no screen
  needs a dismiss event.
- Navigation the screen can decide on its own stays a callback. For navigation the ViewModel has to
  trigger, read `references/events-and-messages.md` first.

### Errors

- The data layer returns its data or throws: `NetworkException` and `StorageException` with a
  `reason`, or the feature's own exception. The UI layer maps the failure; read
  `android-data-layer` for the throwing side and `android-domain-layer` before adding a use case
  for it.
- Map in `onError`: `error.toFailed(fallback)` when the failure replaces the content,
  `error.toErrorMessage(fallback)` when an action on shown content failed. The fallback says what
  the screen was doing, because a bug's own message means nothing to the user.
- A screen's own failures go in a mapper in `ui/mappers/` that handles them first and passes the
  rest to `toFailed` or `toUiText`. Text for a shared `reason` goes into `toUiText`, for every
  screen.
- Repositories never return a status wrapper (`DataState` and `Result` are gone), and never emit a
  loading value.

### Screen and ScreenContent

- `XScreen` and `XScreenContent` are two composables in one file, `XScreen.kt`: the screen first,
  then the content, then the content's private helpers and previews. Never give the content a file
  of its own.
- `XScreen` is the public entry point. It gets the ViewModel, collects `state` with
  `collectAsStateWithLifecycle()`, calls `TrackScreenView` and `TrackScreenState`, hosts
  `MessageHost(viewModel)`, owns navigation, and calls `XScreenContent`.
- `XScreenContent` takes the state, `onEvent`, one callback per navigation target, `modifier` and
  `contentPadding`. It has no ViewModel, no injection, no navigator and no flow collection, so it
  renders in a preview and a test with plain values.
- `XScreenContent` may hold visual element state with `remember` or `rememberSaveable`, such as tap
  counters, animations, or scroll position.
- Make `XScreenContent` `internal` in library features unless a host needs to reuse it.
- Render each `Loadable` field through `ScreenStateHandler`.
- Preview `XScreenContent` in its Ready state. The default empty and failure screens inject an ad
  unit through Koin and do not render in a preview.

### Wiring and tests

- Bind the ViewModel with Koin's `viewModel { XViewModel(...) }` in `di/XModule.kt`, using named
  arguments.
- Test the ViewModel through `state.value` and `messages.value`, covering at least the first load,
  a failure, and a retry after the failure.
- Record the change under `# Unreleased` in the owning `CHANGELOG.md`.
