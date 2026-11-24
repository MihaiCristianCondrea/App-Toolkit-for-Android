This guide discusses user expectations about UI state, and the options available
for preserving state.

Saving and restoring UI state quickly after the
system destroys the host activity or application process is essential for a good
user experience. Users expect the UI state to stay the same, but the system
might destroy the activity hosting the screen and its stored state.

To bridge the gap between user expectations and system behavior, use a
combination of the following methods:

- [`ViewModel`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel) objects.
- Saved state within the following contexts:
    - Composables: [`rememberSerializable`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary#rememberSerializable(kotlin.Array,androidx.savedstate.serialization.SavedStateConfiguration,kotlin.Function0)) and [`rememberSaveable`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary#rememberSaveable(kotlin.Array,kotlin.Function0)).
    - ViewModels: [`SavedStateHandle`](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate).
- Local storage to persist the UI state during app and screen transitions.

The optimal solution depends on your UI data's complexity, your app's use
cases, and finding a balance between data access speed and memory use.

Make sure your app meets users' expectations and offers a fast, responsive
interface. Avoid delays when loading data into the UI, particularly after common
configuration changes like rotation.

## User expectations and system behavior

Depending upon the action a user takes, they expect the UI state
to be either cleared or preserved. In some cases the system
automatically does what the user expects. In other cases the system does
the opposite.

### User-initiated UI state dismissal

The user expects that when they navigate to a screen, its transient UI state
remains the same until they completely dismiss it.
The user can completely dismiss a screen or app by doing the following:

- Swiping the app off of the Overview (Recents) screen.
- Killing or force-quitting the app from the Settings screen.
- Rebooting the device.
- Completing some sort of "finishing" action (which is backed by `Activity.finish()`).

The user's assumption in these complete dismissal cases is that they have
permanently navigated away from the screen, and if they return,
they expect the screen to start from a clean state. The underlying system
behavior for these dismissal scenarios matches the user expectation - the
host activity instance will get destroyed and removed from memory, along with
any state stored in it and any saved state record associated with it.

There are some exceptions to this rule about complete dismissal---for example a
user might expect a browser to take them to the exact webpage they were looking
at before they exited the browser using the back button.

### System-initiated UI state dismissal

A user expects a screen's UI state to remain the same throughout a
configuration change, such as rotation or switching into multi-window mode.
However, by default the system destroys the host activity when such a
configuration change occurs, wiping away any UI state stored in it. To
learn more about device configurations, see
[React to configuration changes in Jetpack Compose](https://developer.android.com/guide/topics/resources/runtime-changes#react-changes-compose).

Note, it is possible (though not recommended)
to override the default behavior for configuration changes. See [Handling the
configuration change](https://developer.android.com/guide/topics/resources/runtime-changes#HandlingTheChange) for more details.

A user also expects your app's UI state to remain the same if they
temporarily switch to a different app and then come back to your app later. For
example, the user performs a search on a screen and then presses the
home button or answers a phone call - when they return to the search screen
they expect to find the search keyword and results still there, exactly as
before.

In this scenario, your app is placed in the background, and the system does its
best to keep your app process in memory. However, the system may destroy the
application process while the user is away interacting with other apps. In such
a case, the host activity is destroyed, along with any state stored in it.
When the user relaunches the app, the screen is unexpectedly in a clean state.
To learn more about process death, see [Processes and app lifecycle](https://developer.android.com/guide/components/activities/process-lifecycle).

## Options for preserving UI state

When the user's expectations about UI state don't match default system
behavior, you must save and restore the user's UI state to ensure that the
system-initiated destruction is transparent to the user.

Each of the options for preserving UI state vary along the following dimensions
that impact the user experience:

|   | ViewModel | Saved state | Persistent storage |
|---|---|---|---|
| Storage location | in memory | in memory | on disk or network |
| Survives configuration change | Yes | Yes | Yes |
| Survives system-initiated process death | No | Yes | Yes |
| Survives user complete screen dismissal/`finish()` | No | No | Yes |
| Data limitations | complex objects are fine, but space is limited by available memory | only for primitive types and simple, small objects such as `String` | only limited by disk space or cost / time of retrieval from the network resource |
| Read/write time | quick (memory access only) | slow (requires serialization/deserialization) | slow (requires disk access or network transaction) |

> [!NOTE]
> **Note:** Saved state in the above table includes the `rememberSerializable` API for composables (or `rememberSaveable` if you are not using KTX serialization) and `SavedStateHandle` for ViewModels.

## Use ViewModel to handle configuration changes

ViewModel is ideal for storing and managing UI-related data while the user is
actively using the application. It allows quick access to UI data and helps you
avoid refetching data from network or disk across rotation, window resizing, and
other commonly occurring configuration changes. To learn how to implement a
ViewModel, see the [ViewModel guide](https://developer.android.com/topic/libraries/architecture/viewmodel).

ViewModel retains the data in memory, which means it is cheaper to retrieve than
data from the disk or the network. A ViewModel is associated with a lifecycle
owner, such as a Navigation destination or an activity. It stays in memory
during a configuration change and the system automatically associates the
ViewModel with the new lifecycle owner instance that results from the
configuration change.

Unlike saved state, ViewModels are destroyed during a system-initiated process
death. To reload data after a system-initiated process death in a ViewModel, use
the [`SavedStateHandle` API](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate). Alternatively, if the data is related to the UI
and doesn't need to be held in the ViewModel, use `rememberSerializable`. For
primitive data types or scenarios where you don't want to use `@Serializable`,
use `rememberSaveable`. If the data is *application data*, then it might be
better to persist it to disk.

If you already have an in-memory solution in place for storing your UI state
across configuration changes, you may not need to use ViewModel.

## Use saved state as backup to handle system-initiated process death

APIs like [`rememberSerializable`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/rememberSerializable.composable) and [`rememberSaveable`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/saveable/package-summary#rememberSaveable(kotlin.Array,kotlin.Function0)) in Compose and
[`SavedStateHandle`](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate) in ViewModels store the data needed to reload the UI
state if the system destroys and later recreates a component. To handle complex
data structures more efficiently, `SavedStateHandle` supports Kotlinx
Serialization via the `saved {}` extension, allowing you to seamlessly persist
and restore type-safe objects alongside standard primitive types. To learn how
to implement saved state using `rememberSaveable`, see [State and Jetpack
Compose](https://developer.android.com/develop/ui/compose/state).

Saved state bundles persist through both configuration changes and
process death but are limited by storage and speed, because the different APIs
serialize data. Serialization can consume a lot of memory if the objects being
serialized are complicated. Because this process happens on the main thread
during a configuration change, long-running serialization can cause dropped
frames and visual stutter.

> [!IMPORTANT]
> **Key Point:** Saved state APIs only save data written to it when the `Activity` is stopped. Writing into it in between this lifecycle state defers the save operation till the next stopped lifecycle event.

Don't use saved state to store large amounts of data, such as bitmaps,
nor complex data structures that require lengthy serialization or
deserialization. Instead, store only primitive types and simple, small objects
such as `String`. As such, use saved state to store a minimal amount of
data necessary, such as an ID, to recreate the data necessary to restore the UI
back to its previous state should the other persistence mechanisms fail. Most
apps should implement this to handle system-initiated process death.

> [!NOTE]
> **Note:** Android keeps a serialized copy of the data in memory, outside of your process. Depending on various factors, the system might try to optimize this process and leave the same `Bundle` object in memory without serialization for quicker access. These behaviors however, may change across Android API versions.

> [!WARNING]
> **Warning:** The system can potentially store the data to disk if you use [`PersistableBundle`](https://developer.android.com/reference/android/os/PersistableBundle) instead of [`Bundle`](https://developer.android.com/reference/android/os/Bundle). However, Jetpack Compose and other AndroidX libraries don't support it. Thus, use `Bundle` instead.

Depending on your app's use cases, you might not need to use saved
state at all. For example, a browser might take the user back to the exact
webpage they were looking at before they exited the browser. If your activity
behaves this way, you can forgo using saved state and instead persist
everything locally.

> [!IMPORTANT]
> **Key Point:** Usually, data stored in saved state is transient state that depends on user input or navigation. Examples of this can be the scroll position of a list, the ID of the item the user wants more detail about, the in-progress selection of user preferences, or input in text fields.

Additionally, when you open an activity from an intent, the bundle of extras is
delivered to the activity both when the configuration changes and when the
system restores the activity. If a piece of UI state data, such as a search
query, were passed in as an intent extra when the activity was launched, you
could use the extras bundle instead of the saved state bundle. To learn
more about intent extras, see [Intent and Intent Filters](https://developer.android.com/guide/components/intents-filters).

In either of these scenarios, you should still use a [`ViewModel`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel) to avoid
wasting cycles reloading data from the database during a configuration change.

In cases where the UI data to preserve is simple and lightweight, you might use
saved state APIs alone to preserve your state data.

> [!IMPORTANT]
> **Key Point:** The API to use depends on where the state is held and the logic that it requires. For state that is used in [business logic](https://developer.android.com/topic/architecture/ui-layer/stateholders#logic), hold it in a ViewModel and save it using `SavedStateHandle`. For state that is used in [UI
> logic](https://developer.android.com/develop/ui/compose/state-saving#ui-logic), use `rememberSerializable` or `rememberSaveable`.

### Hook into saved state using SavedStateRegistry

Beginning with [Fragment 1.1.0](https://developer.android.com/jetpack/androidx/releases/fragment#1.1.0) or its transitive dependency [Activity
1.0.0](https://developer.android.com/jetpack/androidx/releases/activity#version_100_3), UI components, such as [`ComponentActivity`](https://developer.android.com/reference/androidx/activity/ComponentActivity), implement
[`SavedStateRegistryOwner`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistryOwner) and provide a [`SavedStateRegistry`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistry) that is
bound to that component. `SavedStateRegistry` allows components to hook into
your saved state to consume or contribute to it. For example,
the [Saved State module for ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate) uses `SavedStateRegistry` to create a
`SavedStateHandle` and provide it to your `ViewModel` objects. You can retrieve
the `SavedStateRegistry` from within your lifecycle owner by calling
[`savedStateRegistry`](https://developer.android.com/reference/kotlin/androidx/savedstate/SavedStateRegistryOwner#savedStateRegistry()).

Components that contribute to saved state must implement
[`SavedStateRegistry.SavedStateProvider`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistry.SavedStateProvider), which defines a single method
called [`saveState()`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistry.SavedStateProvider#saveState()). The `saveState()` method allows your component to
return a `Bundle` containing any state that should be saved from that component.
`SavedStateRegistry` calls this method during the saving state phase of the
lifecycle owner's lifecycle.

> [!IMPORTANT]
> **Important:** The saved state registry only saves data written to it when the host `Activity` is stopped. Writes that occur while the `Activity` is stopped aren't saved unless the `Activity` receives `onStart` followed by `onStop` again.

> [!NOTE]
> **Note:** If your goal is to save custom data objects in Compose UI rather than writing custom lifecycle-aware components, you don't need to hook into the `SavedStateRegistry` directly. Instead, use `@Parcelize` or a custom `Saver`. For more information, see [Save UI state in Compose](https://developer.android.com/develop/ui/compose/state-saving).

      class SearchManager : SavedStateRegistry.SavedStateProvider {
          companion object {
              private const val QUERY = "query"
          }

          private val query: String? = null

          ...

          override fun saveState(): Bundle {
              return bundleOf(QUERY to query)
          }
      }

To register a `SavedStateProvider`, call [`registerSavedStateProvider()`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistry#registerSavedStateProvider(java.lang.String,%20androidx.savedstate.SavedStateRegistry.SavedStateProvider)) on
the `SavedStateRegistry`, passing a key to associate with the provider's data as
well as the provider. The previously saved data for the provider can be
retrieved from the saved state by calling [`consumeRestoredStateForKey()`](https://developer.android.com/reference/androidx/savedstate/SavedStateRegistry#consumeRestoredStateForKey(java.lang.String))
on the `SavedStateRegistry`, passing in the key associated with the provider's
data.

Within a `ComponentActivity`, you can register a `SavedStateProvider` in
`onCreate()` after calling `super.onCreate()`. Alternatively, you can set a
[`LifecycleObserver`](https://developer.android.com/reference/androidx/lifecycle/LifecycleObserver) on a `SavedStateRegistryOwner`, which implements
[`LifecycleOwner`](https://developer.android.com/reference/androidx/lifecycle/LifecycleOwner), and register the `SavedStateProvider` once the
`ON_CREATE` event occurs. By using a `LifecycleObserver`, you can decouple the
registration and retrieval of the previously saved state from the
`SavedStateRegistryOwner` itself.

      class SearchManager(registryOwner: SavedStateRegistryOwner) : SavedStateRegistry.SavedStateProvider {
          companion object {
              private const val PROVIDER = "search_manager"
              private const val QUERY = "query"
          }

          private val query: String? = null

          init {
              // Register a LifecycleObserver for when the Lifecycle hits ON_CREATE
              registryOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                  if (event == Lifecycle.Event.ON_CREATE) {
                      val registry = registryOwner.savedStateRegistry

                      // Register this object for future calls to saveState()
                      registry.registerSavedStateProvider(PROVIDER, this)

                      // Get the previously saved state and restore it
                      val state = registry.consumeRestoredStateForKey(PROVIDER)

                      // Apply the previously saved state
                      query = state?.getString(QUERY)
                  }
              }
          }

          override fun saveState(): Bundle {
              return bundleOf(QUERY to query)
          }

          ...
      }

      class SearchActivity : ComponentActivity() {
        private var searchManager = SearchManager(this)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Set up your Compose UI here
            setContent {
                // ...
            }
        }
      }

> [!NOTE]
> **Note:** `SavedStateRegistry` stores data in the same `Bundle` as the other saved state APIs, therefore the same considerations and data limitations apply.

## Use local persistence to handle process death for complex or large data

Persistent local storage, such as a database or DataStore, will survive
for as long as your application is installed on the user's device (unless the
user clears the data for your app). While such local storage survives
system-initiated application process death, it can be expensive to
retrieve because it will have to be read from local storage into memory. Often
this persistent local storage may already be a part of your application
architecture to store all data you don't want to lose if you open and close the
app.

Neither ViewModel nor state saved using `rememberSerializable`,
`rememberSaveable`, or `SavedStateHandle` are long-term storage solutions and
thus are not replacements for local storage, such as a database. Instead you
should use these mechanisms for temporarily storing transient UI state only and
use persistent storage for other app data. See [Guide to App Architecture](https://developer.android.com/topic/libraries/architecture/guide)
for more details about how to leverage local storage to persist your app model
data long term (e.g. across restarts of the device).

## Managing UI state: divide and conquer

You can efficiently save and restore UI state by dividing the work among the
various types of persistence mechanisms. In most cases, each of these mechanisms
should store a different type of data used in the app, based on the
trade-offs of data complexity, access speed, and lifetime:

- Local persistence: Stores all the application data you don't want to lose if you open and close the app.
    - Example: A collection of song objects, which could include audio files and metadata.
- [`ViewModel`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel): Stores in memory all the data needed to display the associated UI, the [screen UI state](https://developer.android.com/topic/architecture/ui-layer/stateholders#ui-state).
    - Example: The song objects of the most recent search and the most recent search query.
- Saved state (`rememberSerializable`, `rememberSaveable`, and `SavedStateHandle`): Stores a small amount of data needed to reload UI state if the system stops and then recreates the UI. Instead of storing complex objects here, persist the complex objects in local storage and store a unique ID for these objects in the saved state APIs.
    - Example: Storing the most recent search query.

As an example, consider an app that lets you search through your
library of songs. Here's how different events should be handled:

When the user adds a song, the [`ViewModel`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel) immediately delegates persisting
this data locally. If this newly added song should be shown in the UI, you
should also update the data in the `ViewModel` object to reflect the addition of
the song. Remember to do all database inserts off of the main thread.

When the user searches for a song, whatever complex song data you load from the
database, it should be immediately stored in the `ViewModel` object as part of
the screen UI state.

When the app goes into the background and the system saves the state,
the search query should be stored using saved state APIs,
in case the process recreates. Since the information is necessary to load
application data persisted in this, store the search query in the ViewModel
`SavedStateHandle`, or use `rememberSerializable` or `rememberSaveable` in your
composables. This is all the information you need to load the data and
get the UI back into its current state.

## Restore complex states: reassembling the pieces

When it is time for the user to return to the app, there are two possible
scenarios for recreating the UI:

- The UI is recreated after the system ends the application process. The system has the query saved using saved state APIs. The `ViewModel` (using `SavedStateHandle`) or the composable (using `rememberSerializable` or `rememberSaveable`) automatically restores the query. If the composable restores the query, it passes the query to the `ViewModel`. The `ViewModel` sees that it has no search results cached and delegates loading the search results using the given search query.
- The UI is recreated after a configuration change. Since the `ViewModel` instance hasn't been destroyed, the `ViewModel` has all the information cached in memory and it doesn't need to re-query the database.

> [!NOTE]
> **Note:** When a screen is initially created, the saved state contains no data, and the `ViewModel` object is empty. When you create the `ViewModel` object, you pass an empty query, which tells the `ViewModel` object that there's no data to load yet. Therefore, the UI starts in an empty state.

## Additional resources

To learn more about saving UI states, see the following resources.

- [UI layer documentation](https://developer.android.com/topic/architecture/ui-layer)
- [Saving UI State in Compose](https://developer.android.com/develop/ui/compose/state-saving)
- [State and Jetpack Compose documentation](https://developer.android.com/jetpack/compose/state)

### Codelabs

- [State in Jetpack Compose](https://developer.android.com/codelabs/jetpack-compose-state#0)
- [Advanced State and Side Effects in Jetpack Compose](https://developer.android.com/codelabs/jetpack-compose-advanced-state-side-effects#0)

### Views content

- [Save UI states (Views)](https://developer.android.com/topic/libraries/architecture/views/saving-states-views)

## Recommended for you

- Note: link text is displayed when JavaScript is off
- [Saved State module for ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)
- [Handling Lifecycles with Lifecycle-Aware Components](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [ViewModel overview](https://developer.android.com/topic/libraries/architecture/viewmodel)