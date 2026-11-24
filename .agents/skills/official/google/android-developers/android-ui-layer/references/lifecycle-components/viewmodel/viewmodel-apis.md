# ViewModel Scoping APIs
Part of [Android Jetpack](https://developer.android.com/jetpack).

Scope is key to using ViewModels effectively. Each ViewModel is scoped to an
object that implements the [`ViewModelStoreOwner`](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModelStoreOwner) interface. There are
several APIs that allow you to more easily manage the scope of your ViewModels.
This document outlines some of the key techniques you should know.

> [!NOTE]
> **Note:** For more information on scope and ViewModel lifecycle, see the [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel#scope).

The `ViewModelProvider.get()` method lets you obtain an instance of a ViewModel
scoped to any `ViewModelStoreOwner`. For Kotlin users, there are different
extension functions available for the most common use cases. All Kotlin
extension function implementations use the ViewModelProvider API under the hood.

## ViewModels scoped to the closest ViewModelStoreOwner

You can scope a ViewModel to a [composable](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-apis#vm-api-composable), Activity, or
destination of a Navigation graph. The
`viewModel()` function in Compose allows you to get an instance of the ViewModel
scoped to the closest `ViewModelStoreOwner`.

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
    // ViewModel API available in lifecycle.lifecycle-viewmodel-compose
    // The ViewModel is scoped to the closest ViewModelStoreOwner provided
    // via the LocalViewModelStoreOwner CompositionLocal. In order of proximity,
    // this could be the destination of a Navigation graph
    // or the host Activity.
    viewModel: MyViewModel = viewModel()
) { /* ... */ }
```

> [!NOTE]
> **Note:** If you're using Hilt and Jetpack Compose, replace the `viewModel()` calls with `hiltViewModel()` as explained in the [Compose + Hilt documentation](https://developer.android.com/jetpack/compose/libraries#hilt).

## ViewModels scoped to any ViewModelStoreOwner

The `viewModel()` function takes an optional
`viewModelStoreOwner` parameter that you can use to specify which
`ViewModelStoreOwner` the instance of the ViewModel is scoped to.

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelStoreOwner

@Composable
fun MyScreen(
    // A custom owner passed in, such as a parent NavBackStackEntry
    customOwner: ViewModelStoreOwner,
    // The ViewModel is now scoped to the provided customOwner
    viewModel: MyViewModel = viewModel(viewModelStoreOwner = customOwner)
) {
    /* ... */
}
```

> [!NOTE]
> **Note:** If you're using Hilt and Jetpack Compose, replace the `viewModel()` calls with `hiltViewModel()` as explained in the [Compose + Hilt documentation](https://developer.android.com/jetpack/compose/libraries#hilt).

## ViewModels scoped to a composable

You can use `rememberViewModelStoreOwner()` to scope a ViewModel directly to
the call site of a composable. This is especially useful for UI components that
are dynamically added to or removed from the screen based on state, such as the
items of a page or lazy list. When the composable that owns the
`ViewModelStoreOwner` leaves the composition, the associated `ViewModelStore`
is cleared and the ViewModel is destroyed.

Use `rememberViewModelStoreOwner()` to create a lifecycle-aware store that
survives configuration changes.

    @Composable
    fun RememberViewModelStoreOwnerSample() {
        // Create a ViewModelStoreOwner scoped to this specific call site.
        // When this composable leaves the composition,
        // the associated ViewModelStore will be cleared.
        val scopedOwner = rememberViewModelStoreOwner()

        CompositionLocalProvider(LocalViewModelStoreOwner provides scopedOwner) {
            // This ViewModel is scoped to `scopedOwner`.
            // It will survive configuration changes but will be cleared when
            // the composable is removed from the UI tree.
            val viewModel = viewModel { TestViewModel("scoped_data") }
            // Use the ViewModel
        }
    }

For more complex implementations, such as a `HorizontalPager` or cases
requiring multiple independent scopes, use `rememberViewModelStoreProvider()`.
This lets you generate distinct `ViewModelStoreOwner` instances for different
keys (like page indices). This way, each page maintains its own independent
ViewModel state.

    @Composable
    fun RememberViewModelStoreProviderSample() {
        val storeProvider = rememberViewModelStoreProvider()
        val pages = listOf("Page 1", "Page 2", "Page 3")

        HorizontalPager(pageCount = pages.size) { page ->
            // Create a ViewModelStoreOwner for the specific page using the provider.
            val pageOwner = rememberViewModelStoreOwner(provider = storeProvider, key = page)

            CompositionLocalProvider(LocalViewModelStoreOwner provides pageOwner) {
                val pageViewModel = viewModel { TestViewModel(pages[page]) }
                // Use pageViewModel
            }
        }
    }

## ViewModels scoped to the Navigation graph

Navigation graphs are also ViewModel store owners. If you're using
[Navigation Compose](https://developer.android.com/jetpack/compose/navigation), you can get an instance of a
ViewModel scoped to a Navigation graph with the
[`getBackStackEntry()`](https://developer.android.com/reference/kotlin/androidx/navigation/NavController#getBackStackEntry(kotlin.String)) function.

`viewModel()` retrieves the instance from the nearest `ViewModelStoreOwner`
provided by the `LocalViewModelStoreOwner` `CompositionLocal`. In a typical
Compose application using Jetpack Navigation, this owner is the current
Navigation back stack entry. This means the ViewModel remains in memory as long
as that destination is present in the back stack.

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyAppNavHost() {
    // ...
    composable("myScreen") { backStackEntry ->
        // Retrieve the NavBackStackEntry of "parentNavigationRoute"
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("parentNavigationRoute")
        }
        // Get the ViewModel scoped to the `parentNavigationRoute` Nav graph
        val parentViewModel: SharedViewModel = viewModel(parentEntry)
        // ...
    }
}
```

If you're using Hilt in addition to Jetpack Navigation, you can use the
[`hiltNavGraphViewModels(graphId)`](https://developer.android.com/reference/kotlin/androidx/hilt/navigation/fragment/package-summary#hiltnavgraphviewmodels)
API as follows.

```kotlin
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyAppNavHost() {
    // ...
    composable("myScreen") { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("parentNavigationRoute")
        }

        // ViewModel API available in hilt.hilt-navigation-compose
        // The ViewModel is scoped to the `parentNavigationRoute` Navigation graph
        // and is provided using the Hilt-generated ViewModel factory
        val parentViewModel: SharedViewModel = hiltViewModel(parentEntry)
        // ...
    }
}
```

## Additional resources

To learn more about ViewModels and scope, see the following additional
resources:

### Documentation

- [ViewModel overview](https://developer.android.com/topic/libraries/architecture/viewmodel)

### Views content

- [ViewModel Scoping APIs (Views)](https://developer.android.com/topic/libraries/architecture/views/viewmodel/viewmodel-apis-views)