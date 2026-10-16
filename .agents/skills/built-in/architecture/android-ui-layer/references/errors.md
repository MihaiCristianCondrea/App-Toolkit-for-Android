# Errors

How a failure travels from the data layer to the screen. The data layer throws; the UI layer
decides what the user sees.

## The flow

```text
data source ──► repository ──throws──► ViewModel ──onError──► Loadable.Failed / UiMessage
 (Ktor, DataStore)  networkCall { }        launchReport        toFailed / toErrorMessage
                    storageCall { }        collectReport       (core.ui.screen)
                                           catchReport
```

- **Data layer.** A repository returns its data or throws. It wraps remote calls in `networkCall { }`
  (`:library:core:network`) and storage calls in `storageCall { }` (`:library:core:datastore`), so
  what reaches the ViewModel is a `NetworkException` or `StorageException` with a `reason`, or the
  repository's own exception. `android-data-layer` covers this side, including when a failure
  deserves a custom exception.
- **Domain layer.** A use case passes failures through, or throws its own exception when the
  business rule is what failed (an expired subscription, a missing account). Errors are not a
  reason to add a domain layer; `android-domain-layer` decides that.
- **UI layer.** `launchReport` and `collectReport` report the failure and hand it to `onError`,
  where the ViewModel maps it into state or a message; `catchReport` hands it to a block that emits
  a fallback. This file covers that side.

## Default texts

`toUiText(fallback)` in `core.ui.screen` gives every screen the same text for the failures a user
can act on:

| Failure                                   | Text                                  | Retry |
|-------------------------------------------|---------------------------------------|-------|
| `NetworkException` `NO_INTERNET`          | `screen_error_no_internet`            | yes   |
| `NetworkException` `CONNECTION`           | `screen_error_connection`             | yes   |
| `NetworkException` `TIMEOUT`              | `screen_error_timeout`                | yes   |
| `NetworkException` `RATE_LIMITED`         | `screen_error_rate_limited`           | yes   |
| `NetworkException` `SERVER`               | `screen_error_server`                 | yes   |
| `NetworkException` `SSL`                  | the screen's fallback                 | yes   |
| `NetworkException` `CLIENT`, `UNEXPECTED_RESPONSE`, `SERIALIZATION` | the screen's fallback | no |
| `StorageException` `FULL`                 | `screen_error_storage_full`           | yes   |
| `StorageException` `BUSY`                 | `screen_error_storage_busy`           | yes   |
| `StorageException` `CORRUPT`              | the screen's fallback                 | no    |
| `StorageException` `UNAVAILABLE`, `FAILED` | the screen's fallback                | yes   |
| anything else, bugs included              | the screen's fallback                 | yes   |

The fallback is what the screen was doing ("Could not load the FAQ"). A bug's own message would
mean nothing to the user, and `launchReport` already sent it to Crashlytics.

## In the ViewModel

```kotlin
private fun load() {
    loadJob = loadJob.restart {
        launchReport(
            action = Actions.LOAD,
            onError = { error -> setState { copy(items = error.toFailed(fallback = LoadFailedText)) } },
        ) {
            setState { copy(items = Loadable.Loading) }
            setState { copy(items = Loadable.Ready(repository.getItems().toXItems())) }
        }
    }
}

private fun save() {
    launchReport(
        action = Actions.SAVE,
        onError = { error -> showMessage(error.toErrorMessage(fallback = SaveFailedText)) },
    ) {
        repository.save(currentState.draft)
    }
}

private companion object {
    val LoadFailedText = UiTextHelper.StringResource(R.string.x_load_failed)
    val SaveFailedText = UiTextHelper.StringResource(R.string.x_save_failed)
}
```

- A failure that replaces the content goes into state with `toFailed`, which also sets
  `retryable`.
- A failure of an action on content already shown goes out as a message with `toErrorMessage`.
- Override `retryable` only when the screen knows better:
  `error.toFailed(fallback, retryable = false)`.

## Screen-specific failures

A screen whose data layer throws its own exceptions handles them first and passes the rest to the
defaults:

```kotlin
// ui/mappers/XErrorMappers.kt
internal fun Throwable.toXFailed(): Loadable.Failed = when (this) {
    is XUnavailableException -> Loadable.Failed(UiTextHelper.StringResource(R.string.x_unavailable), retryable = false)
    else -> toFailed(fallback = UiTextHelper.StringResource(R.string.x_load_failed))
}
```

- The exception lives where it is thrown: `data/exceptions/` in the feature for a repository's
  failure, `domain/` only when a use case throws it.
- The mapper lives in `ui/mappers/`, next to the screen's other mappers, and is tested like them.
- A screen never maps `NetworkException` or `StorageException` reasons itself; adding a reason's
  text to `toUiText` gives it to every screen.

## What not to do

- Do not return `DataState` or `Result` from a new repository, and do not emit a loading value from
  one. Loading is the screen's `Loadable`.
- Do not catch inside the repository to hide a failure behind a default value. Falling back to
  another source is fine, as the FAQ does with its bundled questions; a swallowed failure cannot
  reach the user or Crashlytics.
- Do not turn an empty result into an exception. Empty data is `Loadable.Empty`.
- Do not catch `CancellationException`. `launchReport`, `collectReport`, `catchReport`,
  `networkCall` and `storageCall` already pass it through.
- Do not show `throwable.message` to the user.

## Tests

Throw the failure from a fake repository and assert on the state or message, as
`AboutViewModelTest` does: one case with a failure that has a default text (it shows that text),
one with a failure that has none (it shows the fallback), and one that is not retryable.
