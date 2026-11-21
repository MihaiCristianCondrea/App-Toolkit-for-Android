> Documentation index: https://developer.android.com/topic/libraries/architecture/viewmodel/llms.txt

# Saved State module for ViewModel
Part of [Android Jetpack](https://developer.android.com/jetpack.md.txt).

As mentioned in [Saving UI States](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt#use_onsaveinstancestate_as_backup_to_handle_system-initiated_process_death), [`ViewModel`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel.md.txt) objects can handle
configuration changes, so you don't need to worry about state in rotations
or other cases. However, if you need to handle system-initiated process
death, you might want to use the `SavedStateHandle` API as backup.

UI state is usually stored or referenced in `ViewModel` objects, so using
`rememberSaveable` in Compose requires some boilerplate that the
[saved state module](https://developer.android.com/jetpack/androidx/releases/savedstate.md.txt) can handle for you.

When using this module, `ViewModel` objects receive a [`SavedStateHandle`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt)
object through its constructor. This object is a key-value map that lets you
write and retrieve objects to and from the saved state. These values
persist after the process is killed by the system and remain available
through the same object.

Saved state is tied to your task stack. If your task stack goes away, your saved
state also goes away. This can occur when force stopping an app, removing the
app from the recents menu, or rebooting the device. In such cases, the task
stack disappears and you can't restore the information in saved state. In
[User-initiated UI state dismissal](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt#ui-dismissal-system) scenarios, saved state isn't restored. In
[system-initiated](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt#ui-dismissal-system) scenarios, it is.

> [!IMPORTANT]
> **Key Point:** Usually, data stored in saved instance state is transient state that depends on user input or navigation. Examples of this can be the scroll position of a list, the ID of the item the user wants more detail about, the in-progress selection of user preferences, or input in text fields.

> [!IMPORTANT]
> **Important:** the API to use depends on where the state is held and the logic that it requires. For state that is used in [business logic](https://developer.android.com/topic/architecture/ui-layer/stateholders.md.txt#logic), hold it in a ViewModel and save it using `SavedStateHandle`. For state that is used in [UI logic](https://developer.android.com/topic/architecture/ui-layer/stateholders.md.txt#logic), use `rememberSaveable` in Compose.

> [!NOTE]
> **Note:** State must be simple and lightweight. For complex or large data, you should use [local persistence](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt#local).

## Setup

To use `SavedStateHandle`, accept it as a constructor argument to your
`ViewModel`.

```kotlin
class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() { ... }
```

You can then retrieve an instance of your `ViewModel` within your composables
without any additional configuration. The default `ViewModel` factory provides
the appropriate `SavedStateHandle` to your `ViewModel`.


```kotlin
class MyViewModel : ViewModel() { /*...*/ }

// import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun MyScreen(
    viewModel: MyViewModel = viewModel()
) {
    // use viewModel here
}
```

<br />

When providing a custom [`ViewModelProvider.Factory`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModelProvider.Factory.md.txt) instance, you can
enable usage of `SavedStateHandle` by using [`CreationExtras`](https://developer.android.com/reference/kotlin/androidx/lifecycle/viewmodel/CreationExtras.md.txt) and the
[`viewModelFactory`](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories.md.txt) DSL.

## Working with SavedStateHandle

The `SavedStateHandle` class is a key-value map that lets you write and
retrieve data to and from the saved state through the [`set()`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#set(kotlin.String,kotlin.Any)) and
[`get()`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#get(kotlin.String)) methods.

By using `SavedStateHandle`, the query value is retained across process death,
ensuring that the user sees the same set of filtered data before and after
recreation without the activity or fragment needing to manually save, restore,
and forward that value back to the `ViewModel`.

> [!CAUTION]
> **Caution:** In Compose, `SavedStateHandle` only saves data written to it when the host `Activity` is stopped (such as when the app is sent to the background). Writes to `SavedStateHandle` while the `Activity` is stopped aren't saved unless the `Activity` receives `onStart` followed by `onStop` again (such as when the app is foregrounded, then backgrounded again).

`SavedStateHandle` also has other methods you might expect when interacting
with a key-value map:

- [`contains(String key)`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#contains(kotlin.String)) - Checks if there is a value for the given key.
- [`remove(String key)`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#remove(kotlin.String)) - Removes the value for the given key.
- [`keys()`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#keys()) - Returns all keys contained within the `SavedStateHandle`.

Additionally, you can retrieve values from `SavedStateHandle` using an
observable data holder. The list of supported types includes the following:

- [`StateFlow`](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow.md.txt).
- [Compose's State APIs](https://developer.android.com/jetpack/compose/state).

> [!WARNING]
> **Warning:** These integrations with observable data holders make it more convenient to display UI with state persisted by [saved instance state](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt#onsaveinstancestate). However, these integrations are also saved and restored using the same mechanisms as the basic `get()` and `set()` methods. In Compose, state is captured only when the application transitions to the background. This means that while you can continue to update observable data holders from a `SavedStateHandle` while the app is in the background, all state updates might be lost if the app process is killed before becoming foregrounded again.

### StateFlow

You can retrieve values from `SavedStateHandle` wrapped in a `StateFlow`
observable. Depending on whether you need to mutate the value directly, you can
choose between a read-only or mutable stream:

- `getStateFlow()`: Use this if you only need to read the state. When you update the key's value elsewhere in the `SavedStateHandle`, the StateFlow receives the new value. This is ideal when you want to expose a read-only stream and transform it using Flow operators.
- `getMutableStateFlow()`: Use this if you need both read and write access. Updating the `.value` of the returned `MutableStateFlow` automatically updates the underlying `SavedStateHandle`, saving you from needing to manually set the key.

Most often, you update these values due to user interactions, such as entering
a query to filter a list of data.

> [!NOTE]
> **Note:** `getMutableStateFlow` support on `SavedStateHandle` was added in lifecycle version 2.9.0.

```kotlin
class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // Use getMutableStateFlow to read and write the query directly
    private val _query = savedStateHandle.getMutableStateFlow("query", "")
    val query: StateFlow = _query.asStateFlow()

    // Use getStateFlow if you only need a read-only stream to react to changes
    val filteredData: StateFlow<List> =
        query.flatMapLatest {
            repository.getFilteredData(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setQuery(newQuery: String) {
        // Updating the MutableStateFlow automatically updates the SavedStateHandle
        _query.value = newQuery
    }
}
```

### KotlinX Serialization support

For complex UI state, you can use the `saved` property delegate alongside
KotlinX Serialization. This delegate lets you persist custom `@Serializable`
data classes directly into the `SavedStateHandle`. This preserves your
ViewModel's state across process death, so your Compose UI can seamlessly
restore its state upon recreation.

To use it, annotate your data class with `@Serializable` and use the `saved`
delegate in your ViewModel:

```kotlin
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
// Ensure you have the savedstate-ktx dependency
import androidx.savedstate.serialization.saved
import kotlinx.serialization.Serializable

@Serializable
data class UserFilterState(
    val searchQuery: String,
    val minAge: Int,
    val includeInactive: Boolean
)

class FilterViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // The state is automatically serialized to a Bundle on process death,
    // and deserialized upon recreation.
    var filterState by savedStateHandle.saved {
        UserFilterState(searchQuery = "", minAge = 18, includeInactive = false)
    }

    fun updateQuery(newQuery: String) {
        // Mutating the property automatically updates the underlying SavedStateHandle
        filterState = filterState.copy(searchQuery = newQuery)
    }
}
```

> [!NOTE]
> **Note:** The `saved` delegate evaluates lazily. It doesn't call the initialization lambda or save anything into `SavedStateHandle` until the property is accessed for the first time.

### Compose State support

If your state relies on Compose's [`Saver`](https://developer.android.com/jetpack/compose/state#restore-ui-state) APIs instead of on KotlinX
Serialization, the `lifecycle-viewmodel-compose` artifact provides the
[`saveable`](https://developer.android.com/reference/kotlin/androidx/lifecycle/viewmodel/compose/package-summary.md.txt#(androidx.lifecycle.SavedStateHandle).saveable(kotlin.String,androidx.compose.runtime.saveable.Saver,kotlin.Function0)) delegate. This allows interoperability between
`SavedStateHandle` and Compose's [`Saver`](https://developer.android.com/jetpack/compose/state#restore-ui-state) so that any `State` that you can
save via [`rememberSaveable`](https://developer.android.com/jetpack/compose/state#restore-ui-state) with a custom `Saver` can also be saved with
`SavedStateHandle`.

```kotlin
class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var filteredData: List<String> by savedStateHandle.saveable {
        mutableStateOf(emptyList())
    }

    fun setQuery(query: String) {
        withMutableSnapshot {
            filteredData += query
        }
    }
}
```

## Supported types

Data kept within a `SavedStateHandle` is saved and restored as a [`Bundle`](https://developer.android.com/reference/kotlin/android/os/Bundle.md.txt),
along with the rest of the [`savedInstanceState`](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt) for your app.

### Directly supported types

By default, you can call `set()` and `get()` on a `SavedStateHandle` for the
same data types as a `Bundle`, as shown below:

|---|---|
| **Type/Class support** | **Array support** |
| `double` | `double[]` |
| `int` | `int[]` |
| `long` | `long[]` |
| `String` | `String[]` |
| `byte` | `byte[]` |
| `char` | `char[]` |
| `CharSequence` | `CharSequence[]` |
| `float` | `float[]` |
| `Parcelable` | `Parcelable[]` |
| `Serializable` | `Serializable[]` |
| `short` | `short[]` |
| `SparseArray` |   |
| `Binder` |   |
| `Bundle` |   |
| `ArrayList` |   |
| `Size (only in API 21+)` |   |
| `SizeF (only in API 21+)` |   |

If the class does not extend one of those in the above list, consider making the
class parcelable by adding the [`@Parcelize`](https://developer.android.com/kotlin/parcelize.md.txt) Kotlin annotation or
implementing [`Parcelable`](https://developer.android.com/reference/kotlin/android/os/Parcelable.md.txt) directly.

### Saving non-parcelable classes

If a class does not implement `Parcelable` or `Serializable` and cannot be
modified to implement one of those interfaces, then it is not possible to
directly save an instance of that class into a `SavedStateHandle`.

Beginning with [Lifecycle 2.3.0-alpha03](https://developer.android.com/jetpack/androidx/releases/lifecycle.md.txt#2.3.0-alpha03), `SavedStateHandle` lets you save
any object by providing your own logic for saving and restoring your object as
a [`Bundle`](https://developer.android.com/reference/kotlin/android/os/Bundle.md.txt) using the [`setSavedStateProvider()`](https://developer.android.com/reference/kotlin/androidx/lifecycle/SavedStateHandle.md.txt#setSavedStateProvider(kotlin.String,androidx.savedstate.SavedStateRegistry.SavedStateProvider)) method.
[`SavedStateRegistry.SavedStateProvider`](https://developer.android.com/reference/kotlin/androidx/savedstate/SavedStateRegistry.SavedStateProvider.md.txt) is an interface that defines a
single [`saveState()`](https://developer.android.com/reference/kotlin/androidx/savedstate/SavedStateRegistry.SavedStateProvider.md.txt#saveState()) method that returns a `Bundle` containing the state
you want to save. When `SavedStateHandle` is ready to save its state, it calls
`saveState()` to retrieve the `Bundle` from the `SavedStateProvider` and saves
the `Bundle` for the associated key.

Consider an example of an app that requests an image from the camera app via
the [`ACTION_IMAGE_CAPTURE`](https://developer.android.com/reference/kotlin/android/provider/MediaStore.md.txt#action_image_capture) intent, passing in a temporary file for where
the camera should store the image. The `TempFileViewModel` encapsulates the
logic for creating that temporary file.

```kotlin
class TempFileViewModel : ViewModel() {
    private var tempFile: File? = null

    fun createOrGetTempFile(): File {
        return tempFile ?: File.createTempFile("temp", null).also {
            tempFile = it
        }
    }
}
```

To ensure the temporary file is not lost if the activity's process is killed
and later restored, `TempFileViewModel` can use the `SavedStateHandle` to
persist its data. To allow `TempFileViewModel` to save its data, implement
`SavedStateProvider` and set it as a provider on the `SavedStateHandle` of
the `ViewModel`:

```kotlin
private fun File.saveTempFile() = bundleOf("path", absolutePath)

class TempFileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var tempFile: File? = null
    init {
        savedStateHandle.setSavedStateProvider("temp_file") { // saveState()
            if (tempFile != null) {
                tempFile.saveTempFile()
            } else {
                Bundle()
            }
        }
    }

    fun createOrGetTempFile(): File {
        return tempFile ?: File.createTempFile("temp", null).also {
            tempFile = it
        }
    }
}
```

To restore the `File` data when the user returns, retrieve the `temp_file`
`Bundle` from the `SavedStateHandle`. This is the same `Bundle` provided by
`saveTempFile()` that contains the absolute path. The absolute path can then
be used to instantiate a new `File`.

```kotlin
private fun File.saveTempFile() = bundleOf("path", absolutePath)

private fun Bundle.restoreTempFile() = if (containsKey("path")) {
    File(getString("path"))
} else {
    null
}

class TempFileViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var tempFile: File? = null
    init {
        val tempFileBundle = savedStateHandle.get<Bundle>("temp_file")
        if (tempFileBundle != null) {
            tempFile = tempFileBundle.restoreTempFile()
        }
        savedStateHandle.setSavedStateProvider("temp_file") { // saveState()
            if (tempFile != null) {
                tempFile.saveTempFile()
            } else {
                Bundle()
            }
        }
    }

    fun createOrGetTempFile(): File {
      return tempFile ?: File.createTempFile("temp", null).also {
          tempFile = it
      }
    }
}
```

## SavedStateHandle in tests

To test a `ViewModel` that takes a `SavedStateHandle` as a dependency, create
a new instance of `SavedStateHandle` with the test values it requires and pass
it to the `ViewModel` instance you are testing.

```kotlin
class MyViewModelTest {

    private lateinit var viewModel: MyViewModel

    @Before
    fun setup() {
        val savedState = SavedStateHandle(mapOf("someIdArg" to testId))
        viewModel = MyViewModel(savedState = savedState)
    }
}
```

## Additional resources

For further information about the Saved State module for `ViewModel`, see the
following resources.

### Views content

- [Saved State module for ViewModel (Views)](https://developer.android.com/topic/libraries/architecture/views/viewmodel/viewmodel-savedstate-views.md.txt)

## Recommended for you

- Note: link text is displayed when JavaScript is off
- [Save UI states](https://developer.android.com/topic/libraries/architecture/saving-states.md.txt)
- [Work with observable data objects](https://developer.android.com/topic/libraries/data-binding/observability.md.txt)
- [Create ViewModels with dependencies](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories.md.txt)