# Migrating from core.ui.base and core.ui.states

The old types were removed in 3.0.0; every Toolkit feature and the sample are on `core.ui.screen`.
This file is for an app built on an older Toolkit: it maps each old type to its replacement and
lists the steps for one screen. Reference: the About migration in `:library:feature:about`.

## Finding what is left

Search the app's sources for the old names; each match is a screen still to migrate, and none of
them compiles against 3.0.0:

```text
core.ui.base
core.ui.states
DefaultSnackbarHandler
observeActions
DataState
asUiText
toError(
```

## Old to new

| Old                                              | New                                                   |
|--------------------------------------------------|-------------------------------------------------------|
| `core.ui.base.BaseViewModel`                     | `core.ui.screen.ScreenViewModel<S, E>`                |
| `core.ui.base.ScreenViewModel<T, E, A>`          | `core.ui.screen.ScreenViewModel<S, E>`                |
| `core.ui.base.LoggedScreenViewModel<T, E, A>`    | `core.ui.screen.LoggedScreenViewModel<S, E>`          |
| `UiState` marker                                 | nothing; the state is a plain data class              |
| `UiEvent` marker                                 | nothing; `E : Any`                                    |
| `ActionEvent` and the action flow                | `UiMessage` for snackbars; callbacks or state for navigation |
| `UiStateScreen<T>` and `ScreenState`             | `Loadable<T>` fields in the feature's state           |
| `core.ui.views.layouts.ScreenStateHandler`       | `core.ui.screen.ScreenStateHandler(Loadable)`         |
| `DefaultSnackbarHandler` and dismiss events      | `MessageHost(viewModel)`                              |
| `setState` under a `Mutex`                       | `setState { copy(...) }`                              |

## DataState and Errors

`DataState`, `Errors`, `toError()` and `asUiText()` were removed from `:library:core:network` in
3.0.0, with `core.ui.base` and `core.ui.states`. An app that still has them moves each repository
off them with its screen:

| Old                                          | New                                                    |
|----------------------------------------------|--------------------------------------------------------|
| `Flow<DataState<T, E>>`, `suspend ... DataState<T, E>` | `Flow<T>`, `suspend ... T`, throwing on failure |
| `DataState.Loading` emitted by a repository  | nothing; the ViewModel sets `Loadable.Loading`         |
| `DataState.Error(data = cached, error)`      | `Loadable.Ready(cached, stale = true)` and a message   |
| `throwable.toError(default)`                 | `networkCall { }` or `storageCall { }` around the call |
| `Errors.Network.*`, `Errors.Database.*`      | `NetworkException`, `StorageException` with a `reason` |
| `Errors.UseCase.NO_DATA`                     | `Loadable.Empty`                                       |
| `Errors.UseCase.CANCELLED`                   | nothing; cancellation passes through                   |
| `ILLEGAL_ARGUMENT`, `INVALID_STATE`, `UNSUPPORTED_OPERATION` | thrown as they are; the screen's fallback shows |
| `Errors.UseCase.FAILED_TO_*`                 | the screen's fallback string, in the feature           |
| `error.asUiText()`                           | `error.toUiText(fallback)`, `toFailed`, `toErrorMessage` |
| a feature's own `Error` sealed type          | the feature's exceptions and an `XErrorMappers.kt`     |

## Steps for one feature

Steps 1 to 4 change the ViewModel and its contract together, so the module does not compile
between them.

1. Rewrite `XUiState` as a data class with `Loadable` fields where something loads, and delete the
   `toUiState` mappers that built a `UiStateScreen`.
2. Drop the `UiEvent` marker and any dismiss event from `XEvent`, and delete `XAction`.
3. Move the ViewModel to `core.ui.screen.LoggedScreenViewModel`. Keep its `screenName`,
   `viewModelName` and action names unchanged, so the reports keep their history.
4. Replace action emissions with `showMessage(UiMessage(...))` for snackbars, and with a callback
   or a state flag for navigation (`events-and-messages.md`).
5. Split the screen into `XScreen` and `XScreenContent`, both in `XScreen.kt`, and add the
   previews below the content.
6. Remove a `DispatcherProvider` from the ViewModel and its Koin binding when it only moved
   repository calls. Move the repository off `DataState` (see above), and map its failures in
   `onError`.
7. Rewrite the ViewModel tests against `state.value` and `messages.value`, adding the failure and
   retry cases the old screen often lacked.
8. Update the module README and record the change under `# Unreleased` in its `CHANGELOG.md`.

## Behavior to keep

- **Reports.** Breadcrumb messages and keys, GA4 event names and parameters, and Crashlytics
  reports are unchanged in `LoggedScreenViewModel`, so nothing on the dashboards moves as long as
  the names above stay.
- **Failures.** A screen that went blank or kept spinning on failure now shows `Loadable.Failed`
  with Retry. Note it under `Fixed` in the changelog, as About did.
- **Messages.** A snackbar the old screen dismissed through an event now leaves on its own, and one
  raised while the screen was hidden now waits instead of being dropped.

When the last feature has moved, delete the old types and the lines in `SKILL.md` that mention
them ("There is no shared base state type", "there is no `Action` type").
