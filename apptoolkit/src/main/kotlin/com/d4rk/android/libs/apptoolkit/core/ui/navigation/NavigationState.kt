package com.d4rk.android.libs.apptoolkit.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.serializer

/**
 * Remembers navigation state for a set of top-level destinations.
 *
 * Generic + type-safe: keeps your NavBackStack typed as T instead of collapsing to NavKey.
 */
@Composable
inline fun <reified T : StableNavKey> rememberNavigationState(
    startRoute: T,
    topLevelRoutes: ImmutableSet<T>,
): NavigationState<T> {
    val stableStartRoute by rememberUpdatedState(newValue = startRoute)
    val topLevelRouteState: MutableState<T> = rememberSerializable(
        stableStartRoute,
        topLevelRoutes,
        serializer = MutableStateSerializer(serializer<T>())
    ) {
        mutableStateOf(stableStartRoute)
    }

    val backStacks: Map<T, NavBackStack<T>> = buildMap {
        for (route in topLevelRoutes) {
            put(route, rememberTypedNavBackStack(persistentListOf(route)))
        }
    }

    return remember(stableStartRoute, topLevelRoutes, topLevelRouteState.value) {
        NavigationState(
            startRoute = stableStartRoute,
            topLevelRoute = topLevelRouteState,
            backStacks = backStacks
        )
    }
}

/**
 * Remembers a type-safe [NavBackStack] that is preserved across process death and configuration changes.
 *
 * This function uses [rememberSerializable] to persist the back stack, ensuring that the
 * navigation history of type [T] is restored correctly.
 *
 * @param T The type of the navigation keys, which must implement [StableNavKey].
 * @param initialElements The initial list of destinations to populate the back stack with.
 * @return A [NavBackStack] instance initialized with the provided elements.
 */
@Composable
inline fun <reified T : StableNavKey> rememberTypedNavBackStack(
    initialElements: ImmutableList<T> = persistentListOf(),
): NavBackStack<T> {
    return rememberSerializable(
        serializer = NavBackStackSerializer(elementSerializer = serializer<T>())
    ) {
        NavBackStack(*initialElements.toTypedArray())
    }
}

@Stable
class NavigationState<T : StableNavKey>(
    val startRoute: T,
    topLevelRoute: MutableState<T>,
    val backStacks: Map<T, NavBackStack<T>>
) {
    var topLevelRoute: T by topLevelRoute

    val currentBackStack: NavBackStack<T>
        get() = backStacks[topLevelRoute] ?: error("Stack for $topLevelRoute not found")

    val currentRoute: T
        get() = currentBackStack.last()
}

@Stable
class Navigator<T : StableNavKey>(val state: NavigationState<T>) {
    fun navigate(route: T) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.currentBackStack.add(route)
        }
    }

    fun goBack() {
        val currentRoute = state.currentBackStack.lastOrNull() ?: return

        if (currentRoute == state.topLevelRoute) {
            if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
            }
        } else {
            state.currentBackStack.removeLastOrNull()
        }
    }
}

/**
 * Default decorators used across the toolkit to mirror Navigation 3 guidance:
 * - rememberSaveableStateHolderNavEntryDecorator to keep state across process death.
 * - rememberViewModelStoreNavEntryDecorator to scope ViewModels to each NavBackStack entry.
 */
@Composable
fun <T : StableNavKey> rememberNavigationEntryDecorators(): List<NavEntryDecorator<T>> {
    val saveableStateHolderDecorator = rememberSaveableStateHolderNavEntryDecorator<T>()
    val viewModelStoreDecorator = rememberViewModelStoreNavEntryDecorator<T>()

    return remember(saveableStateHolderDecorator, viewModelStoreDecorator) {
        listOf(saveableStateHolderDecorator, viewModelStoreDecorator)
    }
}
