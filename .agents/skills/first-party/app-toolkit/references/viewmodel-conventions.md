# ViewModel conventions

## Required behavior

- Use the feature's typed event contract for user actions. Keep dispatch exhaustive and delegate
  work to named private methods. Prefer direct events such as `SetLayout`, `Load` and `Reset`.
  A second event hierarchy needs a distinct consumer or purpose.
- Use the Toolkit's state update API to apply related changes together. In `setState { copy(...) }`,
  copy the current state so concurrent updates to other fields survive. Replace the whole state
  only when the operation owns every field.
- Run screen work in the ViewModel lifecycle. Preserve coroutine cancellation and keep it out of
  failure messages and telemetry. Give a failed observation a recovery path. Verify whether the
  repository propagates the error, retries it, or emits a fallback and completes. An error
  callback in the ViewModel cannot catch a failure the repository already handled.
- Keep valid state on failed writes and on refresh failures where the existing content is usable.
  Put a first-load failure in the content state when the screen has no usable data. Only report a
  successful settings change after persistence succeeds.

## Preferred declaration order

Declare `XViewModel` with constructor injection and a typed state and event superclass.
Keep dependencies used by the class as `private val`. Pass telemetry through to the logged
base class without a second property. Keep stable screen and ViewModel identifiers when the
base class reports them. Prefer internal declarations for implementation details. If the ViewModel
is already a public contract, preserve its constructor and exposed types unless the task includes
a migration.

Order the class as constructor and superclass arguments, private job fields, optional
initialization, event dispatch, private operation methods, then named constants. Put the first
load on the same event path as retry. Dispatch it from `init` for eager loading, or from the
screen for a screen-owned start, as Settings does. Choose one startup owner.

This excerpt matches the Toolkit's Changelog implementation. The state starts with
`markdown = Loadable.Loading`, and the event contract has one `Load` object.

```kotlin
private var loadJob: Job? = null

init {
    onEvent(ChangelogEvent.Load)
}

override fun handleEvent(event: ChangelogEvent) {
    when (event) {
        ChangelogEvent.Load -> loadChangelog()
    }
}
```

## Choose the job policy for the operation

| Operation | Toolkit pattern |
| --- | --- |
| One-shot fetch | Changelog uses `launchReport` inside `loadJob.ifIsActive { ... }` so duplicate loads share the active fetch. Once it finishes, another request can start. |
| Replace the previous request | Settings uses `restart` for its config load. The helper cancels the previous job and starts a replacement. |
| Observe preferences | Theme and Developer Options use `collectReport` and keep a job they can restart on `Load`. Theme uses a content error with retry. Developer Options keeps usable settings and routes the snackbar action to `Load`. |
| Save a setting or reset | Use a named `launchReport` operation. Keep independent writes eligible to complete, as Theme does. Choose write ordering according to the repository contract. |
| Observe an expensive stream | Consider `observeReport` when work should track active state subscribers and emissions may be missed while nobody collects. |

Developer Options attaches retry to the read error. Its screen sends `Load` when the action
is pressed. The observer updates settings only on a successful emission.

```kotlin
private fun observeSettings() {
    observeJob = observeJob.restart {
        preferences.settings.collectReport(
            action = Actions.OBSERVE_SETTINGS,
            onError = { error ->
                showMessage(
                    error.toErrorMessage().copy(
                        actionLabel = UiText.StringResource(CoreUiR.string.try_again),
                    ),
                )
            },
        ) { settings -> setState { copy(settings = settings) } }
    }
}
```

Use `LoggedScreenViewModel` for feature screens. Use `ScreenViewModel` where the project
intentionally has no operation telemetry. Use named telemetry actions
such as `Actions.SAVE_OPTION`. When it helps diagnose failures, use a stable bounded value such
as the option key in `launchReport(extra = ...)`. Keep selected values, user text and identifiers
out of diagnostic context. Follow the shared analytics contract for product events.

The code excerpt was checked against Toolkit source. Helper names in this reference describe
that implementation. Verify availability before using them with a published dependency. Keep the
same behavior with the project's available APIs when a helper is absent.
