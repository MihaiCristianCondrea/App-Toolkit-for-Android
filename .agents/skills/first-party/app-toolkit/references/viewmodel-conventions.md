# ViewModel conventions

## Shared rules

- Declare concrete feature ViewModels as public Kotlin classes (`class XViewModel`). Types in
  their public signatures, including constructor dependencies, state and events, need compatible
  visibility. Keep implementation members private. Review API impact before widening visibility
  in an existing published library.
- Put job fields first among private ViewModel properties, then other necessary state or
  dependencies, optional `init`, `handleEvent` and private operation methods. For a load
  started in `init`, dispatch its typed event. Use the same event for Retry when it runs
  the same operation.
- Use typed events and exhaustive dispatch. The initial load and retry should send the same
  event when they run the same operation. Pick one startup owner. Changelog starts in `init`;
  Settings starts from the screen. Restoring state or starting a lifetime observer with no load
  or retry event can have its own initialization.
- Delegate substantial work to named private operations. A direct repository setter passed to
  a shared save method or a simple state update can stay in a branch. Keep a single expression
  on one line when it fits the project's formatting.
- Update related state fields together. An ordinary `setState { copy(...) }` or
  `MutableStateFlow.update { it.copy(...) }` is a local update. Extract a reducer when a
  distinct pure transition needs its own contract. Place that reducer in the owning layer's
  reducers package.
- Run work in the ViewModel lifecycle. Preserve cancellation. Make failed observations
  recoverable and verify whether the repository propagates errors or handles them itself.
  Keep usable state on failed writes or refreshes when possible. Report successful persistence
  only after the write completes.

Put stable ViewModel operation names in a concern-specific file under `ui/constants/`.
Keep GA4 event names, parameters and builders in `ui/analytics/` or their shared analytics contract. Inline single-use expressions when their meaning is clear at the call
site. Keep constants that name a policy, stabilize an identifier, or prevent duplication.

## App Toolkit pattern

A catalog with a one-shot load starts at `Loadable.Loading`. Its ViewModel forwards telemetry
to `LoggedScreenViewModel` without a duplicate property.

```kotlin
private var loadJob: Job? = null

init {
    onEvent(CatalogEvent.Load)
}

override fun handleEvent(event: CatalogEvent) {
    when (event) {
        CatalogEvent.Load -> loadCatalog()
    }
}
```

Avoid a single-use property for the error fallback.

```kotlin
private companion object {
    val LoadFailedText = UiText.StringResource(R.string.catalog_load_failed)
}
```

Prefer the resource at the point where the failure is mapped.

```kotlin
onError = { error ->
    setState {
        copy(
            items = error.toFailed(
                fallback = UiText.StringResource(R.string.catalog_load_failed),
            ),
        )
    }
},
```

Use `LoggedScreenViewModel` for feature screens with Toolkit reporting, or `ScreenViewModel`
where the project intentionally has no operation telemetry. Add stable, bounded diagnostic
context where useful, as Developer Options does with its option key. Keep user-entered text,
selected values and identifiers out of diagnostic context.

| Operation | Toolkit pattern |
| --- | --- |
| One-shot fetch | Changelog uses `launchReport` inside `loadJob.ifIsActive { ... }` so duplicate loads share the active fetch. Once it finishes, another request can start. |
| Replace the previous request | A new search query uses `restart` to request cancellation and begin replacement work. It does not join the previous job. Guard result publication if the old operation can still return. |
| Observe preferences | Theme and Developer Options use `collectReport` and keep a job they can restart on `Load`. Theme uses a content error with retry. Developer Options keeps usable settings and routes the snackbar action to `Load`. |
| Save a setting or reset | Use a named `launchReport` operation. Keep independent writes eligible to complete, as Theme does. Choose write ordering according to the repository contract. |
| Observe an expensive stream | Consider `observeReport` when work should track active state subscribers and emissions may be missed while nobody collects. |

These helper names come from Toolkit source. Verify them against the resolved dependency when
working in a consumer app. The Developer Options retry only catches failures the repository
propagates. Check the production data flow as well as a throwing fake.

## Standalone AndroidX pattern

Use the project's existing AndroidX and coroutine dependencies. This example uses a main-safe
catalog repository, one load at a time, and a typed retryable read failure. The screen collects
`state` with lifecycle awareness and sends `CatalogEvent.Load` for Retry. It maps `ReadFailed` to
localized copy, showing a content error on first load and retaining usable items on refresh.

```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface CatalogRepository {
    suspend fun loadItems(): List<String>
}

sealed interface CatalogEvent {
    data object Load : CatalogEvent
}

enum class CatalogFailure { ReadFailed }

data class CatalogUiState(
    val items: List<String> = emptyList(),
    val loading: Boolean = true,
    val failure: CatalogFailure? = null,
)

class CatalogViewModel(private val repository: CatalogRepository) : ViewModel() {
    private var loadJob: Job? = null
    private val mutableState = MutableStateFlow(CatalogUiState())
    val state = mutableState.asStateFlow()

    init {
        handleEvent(CatalogEvent.Load)
    }

    fun handleEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.Load -> loadCatalog()
        }
    }

    private fun loadCatalog() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            mutableState.update { it.copy(loading = true, failure = null) }
            try {
                val items = repository.loadQuestions()
                mutableState.update { it.copy(items = items, loading = false) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: IOException) {
                mutableState.update { it.copy(loading = false, failure = CatalogFailure.ReadFailed) }
            }
        }
    }
}
```

Use the failure types declared by the actual repository. Add operation identifiers only when
used by the project's reporting. A small screen needs no reporting base class or reducer to
follow these conventions.
