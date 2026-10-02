# ViewModel

How `XViewModel` runs its work and what it reports. Template: `templates/XViewModel.kt.txt`.

## The base classes

| Class                   | Adds                                                                 |
|-------------------------|----------------------------------------------------------------------|
| `ScreenViewModel<S, E>` | `state`, `messages`, `onEvent`, `setState`, `showMessage`, `restart` |
| `LoggedScreenViewModel` | breadcrumbs, GA4 events and Crashlytics reports, `launchReport`, `collectReport`, `catchReport` |

`onEvent` is final: it calls `onEventReceived`, which `LoggedScreenViewModel` uses for the
`vm_event` breadcrumb, then `handleEvent`. A feature implements only `handleEvent`.

## Three shapes

Every piece of work runs through one of these, so it is logged, failures are reported, and
cancellation is never mistaken for a failure:

| Work                                              | Shape                                                    |
|---------------------------------------------------|----------------------------------------------------------|
| a suspend call: load, save, copy                  | `launchReport(action, onError = { }) { ... }`            |
| a flow whose values go into state                 | `flow.collectReport(action, onError = { }) { value -> }` |
| a flow that goes on: combined, shared, a fallback | `flow.catchReport(action) { error -> emit(...) }`        |

`launchReport` and `collectReport` take the same arguments, return the `Job`, and end the same
way: the block or the collection stops at the first failure, which is reported and handed to
`onError`. `catchReport` is the operator under them, for a flow that has to keep going.

## One-shot operations

```kotlin
private fun load() {
    loadJob = loadJob.restart {
        launchReport(
            action = Actions.LOAD,
            onError = { error -> setState { copy(items = error.toFailed(fallback = LoadFailedText)) } },
        ) {
            setState { copy(items = Loadable.Loading) }
            val items = repository.getItems().toXItems()
            setState { copy(items = if (items.isEmpty()) Loadable.Empty() else Loadable.Ready(items)) }
        }
    }
}
```

- `launchReport` logs the start, runs the block in `viewModelScope`, and on a throw reports it and
  calls `onError`. Cancellation passes through and is never reported.
- `onError` receives the failure and maps it into state or a message with `toFailed` or
  `toErrorMessage` (`errors.md`). `LoadFailedText` is the screen's fallback text, a
  `UiTextHelper` constant. Leaving it empty is right only when the user has nothing to see
  or do, as with the About easter egg write.
- `restart` cancels the previous job before starting the next, so a retry never races the load it
  replaces. An operation where every call must complete, such as a copy or a save, launches without
  it.
- `extra` adds attributes to the operation's breadcrumbs and events. Keep its values free of user
  text.

## Streams

Collect a repository stream with `collectReport`, the flow counterpart of `launchReport`:

```kotlin
private fun observe() {
    observeJob = observeJob.restart {
        repository.observeItems().collectReport(
            action = Actions.OBSERVE,
            onError = { error -> setState { copy(items = error.toFailed(fallback = LoadFailedText)) } },
        ) { items ->
            setState { copy(items = Loadable.Ready(items.toXItems())) }
        }
    }
}
```

- It logs the start once, then sets state for every value.
- On a failure the collection ends, as a flow's does. Retry by sending `Load` again, which
  restarts the job, exactly like a one-shot load.
- Start it from the `Load` event, sent in `init`, so the first collection and a retry are the same
  path.

Several sources combine before they reach the state; the combined flow is still collected once:

```kotlin
combine(repository.items, preferences.sortOrder) { items, order -> items.sortedBy(order).toXItems() }
    .collectReport(action = Actions.OBSERVE, onError = { error -> /* ... */ }) { items ->
        setState { copy(items = Loadable.Ready(items)) }
    }
```

Use `catchReport` only when the flow has to keep going after the failure, by emitting a fallback
into a chain that continues, for example before `stateIn`:

```kotlin
val summary: StateFlow<Loadable<Summary>> = repository.observeSummary()
    .map<Summary, Loadable<Summary>> { Loadable.Ready(it) }
    .catchReport(action = Actions.OBSERVE_SUMMARY) { error -> emit(error.toFailed(fallback = LoadFailedText)) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Loadable.Loading)
```

The explicit type on `map` lets the fallback emit a `Failed` into a flow of `Ready`.
`catchReport` does not log a start; call `startOperation(action)` first when the dashboards should
count it.

## What is reported

| When                 | Breadcrumb    | GA4 event     | Crashlytics            |
|----------------------|---------------|---------------|------------------------|
| created              | `vm_init`     |               |                        |
| every event          | `vm_event`    |               |                        |
| operation starts     | `vm_op_start` | `vm_op_start` |                        |
| operation throws     | `vm_op_error` | `vm_op_error` | `reportViewModelError` |

Breadcrumb keys are `screen`, `viewModel`, `event`, `action`, `step` and `error`. The GA4 events
carry `screen`, `view_model`, `action`, and `error_class` on failures. These names are what the
dashboards and Crashlytics filters read, so a migration keeps each ViewModel's `screenName`,
`viewModelName` and action names unchanged.

`breadcrumb(message, attributes)` logs an extra step inside an operation when a crash report needs
to show how far it got.

## Dependencies

Google's rule is not a fixed list of types. A ViewModel holds no `Context` or lifecycle-bound
object, and reaches data only through the data layer (or the optional domain layer), never a data
source directly. In practice:

| A ViewModel may take                                   | Because                                          |
|--------------------------------------------------------|--------------------------------------------------|
| repositories                                           | the data layer's entry points                    |
| use cases, where `android-domain-layer` says they earn it | reusable business operations                  |
| `TelemetryRepository`                                   | analytics and crash reporting; `LoggedScreenViewModel` requires it |
| `DispatcherProvider`                                   | only for CPU-heavy work the ViewModel does itself |

| A ViewModel never takes                                | Instead                                          |
|--------------------------------------------------------|--------------------------------------------------|
| `Context`, `Activity`, `Resources`                     | `UiTextHelper` for text; a repository for the platform |
| a data source, a DAO, an `HttpClient`                  | the repository that owns it                      |
| a bare platform wrapper (clipboard, package manager)   | a repository over that platform API              |

- The platform is a data source like any other. A platform call the ViewModel needs goes through a
  repository, as `AboutViewModel` copies through `ClipboardRepository`, which a test replaces
  with a fake.
- Platform rules (what the system shows on which Android version) belong in that repository, as
  `ClipboardRepository.confirmsCopies` does, not in a `Build.VERSION` check in the ViewModel.
- Repositories and use cases return data or throw; neither returns a status wrapper.
- Do not take a `DispatcherProvider` only to move repository calls; repositories are main-safe.
