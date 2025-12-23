package com.d4rk.android.libs.apptoolkit.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.StableNavKey
import kotlinx.collections.immutable.ImmutableSet

/**
 * Remembers navigation state for a set of top-level destinations.
 *
 * This implementation is generic so that any module can provide its own
 * [StableNavKey] implementations while reusing the same state management.
 */
@Composable
fun <T : StableNavKey> rememberNavigationState(
    startRoute: T, // FIXME: Parameter 'startRoute' has runtime-determined stability
    topLevelRoutes: ImmutableSet<T>,
): NavigationState<T> {
    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(key).apply {
            if (isEmpty()) add(key)
        }
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks // FIXME: Argument type mismatch: actual type is 'Map<T (of fun <T : StableNavKey> rememberNavigationState), NavBackStack<NavKey>>', but 'Map<T (of fun <T : StableNavKey> rememberNavigationState), NavBackStack<T (of fun <T : StableNavKey> rememberNavigationState)>>' was expected.
        )
    }
}

@Stable
class NavigationState<T : StableNavKey>(
    val startRoute: T,
    topLevelRoute: MutableState<T>,
    val backStacks: Map<T, NavBackStack<T>>
) {
    var topLevelRoute: T by topLevelRoute
    val stacksInUse: List<T>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

@Stable
class Navigator<T : StableNavKey>(val state: NavigationState<T>) {
    fun navigate(route: T) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

@Composable
fun <T : StableNavKey> NavigationState<T>.toEntries(
    entryProvider: (T) -> NavEntry<T>
): SnapshotStateList<NavEntry<T>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<T>(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
