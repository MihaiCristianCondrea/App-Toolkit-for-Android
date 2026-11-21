# ViewModel conventions

## Shared rules

- Declare concrete feature ViewModels as public Kotlin classes (`class XViewModel`). Types in
  their public signatures, including constructor dependencies, state and events, need compatible
  visibility. Keep implementation members private. Review API impact before widening visibility
  in an existing published library.
- Order members as constructor and properties, optional `init`, event entry point and
  `handleEvent`, private operations, `private object Actions` when reporting operations,
  then other necessary class-owned constants.
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

Keep stable operation names in the ViewModel's private `Actions` object. Keep shared identifiers
with their owning contract. Inline single-use expressions when their meaning is clear at the call
site. Keep constants that name a policy, stabilize an identifier, or prevent duplication.

## App Toolkit pattern

Changelog is the reference for a one-shot load. Its public ViewModel takes its use case and
telemetry through the constructor. It forwards telemetry to `LoggedScreenViewModel` without
a duplicate property. Its state starts at `Loadable.Loading`.

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

Avoid a single-use field such as the former FAQ fallback.

```kotlin
private companion object {
    val LoadFailedText = UiText.StringResource(R.string.error_failed_to_load_faq)
}
```

Prefer the resource at the point where the failure is mapped.

```kotlin
onError = { error ->
    setState {
        copy(
            questions = error.toFailed(
                fallback = UiText.StringResource(R.string.error_failed_to_load_faq),
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
| Replace the previous request | Settings uses `restart` for its config load. The helper cancels the previous job and starts a replacement. |
| Observe preferences | Theme and Developer Options use `collectReport` and keep a job they can restart on `Load`. Theme uses a content error with retry. Developer Options keeps usable settings and routes the snackbar action to `Load`. |
| Save a setting or reset | Use a named `launchReport` operation. Keep independent writes eligible to complete, as Theme does. Choose write ordering according to the repository contract. |
| Observe an expensive stream | Consider `observeReport` when work should track active state subscribers and emissions may be missed while nobody collects. |

These helper names come from Toolkit source. Verify them against the resolved dependency when
working in a consumer app. The Developer Options retry only catches failures the repository
propagates. Check the production data flow as well as a throwing fake.

## Standalone AndroidX pattern

Use the project's existing AndroidX and coroutine dependencies. This example uses a main-safe
FAQ repository, one load at a time, and a typed retryable read failure. The screen collects
`state` with lifecycle awareness and sends `FaqEvent.Load` for Retry. It maps `ReadFailed` to
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

interface FaqRepository {
    suspend fun loadQuestions(): List<String>
}

sealed interface FaqEvent {
    data object Load : FaqEvent
}

enum class FaqFailure { ReadFailed }

data class FaqUiState(
    val questions: List<String> = emptyList(),
    val loading: Boolean = true,
    val failure: FaqFailure? = null,
)

class FaqViewModel(private val repository: FaqRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(FaqUiState())
    val state = mutableState.asStateFlow()
    private var loadJob: Job? = null

    init {
        handleEvent(FaqEvent.Load)
    }

    fun handleEvent(event: FaqEvent) {
        when (event) {
            FaqEvent.Load -> loadFaq()
        }
    }

    private fun loadFaq() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            mutableState.update { it.copy(loading = true, failure = null) }
            try {
                val questions = repository.loadQuestions()
                mutableState.update { it.copy(questions = questions, loading = false) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: IOException) {
                mutableState.update { it.copy(loading = false, failure = FaqFailure.ReadFailed) }
            }
        }
    }
}
```

Use the failure types declared by the actual repository. Declare `Actions` when the ViewModel
uses real operation reporting. A small screen needs no reporting base class or reducer to
follow these conventions.
