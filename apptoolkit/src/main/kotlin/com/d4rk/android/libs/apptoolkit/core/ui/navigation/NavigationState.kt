/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.d4rk.android.libs.apptoolkit.core.ui.model.navigation.StableNavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf

/**
 * Remembers navigation state for a set of top-level destinations.
 *
 * Generic + type-safe: keeps your NavBackStack typed as T instead of collapsing to NavKey.
 */
@Composable
fun <T : StableNavKey> rememberNavigationState(
    startRoute: T,
    topLevelRoutes: ImmutableSet<T>,
): NavigationState<T> {
    val stableStartRoute by rememberUpdatedState(newValue = startRoute)
    val topLevelRouteState: MutableState<T> = rememberSaveable(
        stableStartRoute,
        topLevelRoutes
    ) {
        mutableStateOf(stableStartRoute)
    }

    val topLevelHistory = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { mutableStateListOf<T>().apply { addAll(it) } }
        )
    ) {
        mutableStateListOf<T>()
    }

    val backStacks: Map<T, NavBackStack<T>> = buildMap {
        for (route in topLevelRoutes) {
            put(route, rememberTypedNavBackStack(persistentListOf(route)))
        }
    }

    return remember(stableStartRoute, topLevelRoutes, topLevelRouteState.value, topLevelHistory) {
        NavigationState(
            startRoute = stableStartRoute,
            topLevelRoute = topLevelRouteState,
            backStacks = backStacks,
            topLevelHistory = topLevelHistory
        )
    }
}

/**
 * Remembers a type-safe [NavBackStack] that is preserved across process death and configuration changes.
 *
 * This function uses [rememberSaveable] with Parcelable route keys to persist the back stack,
 * ensuring that navigation history of type [T] is restored correctly without JSON serialization.
 *
 * @param T The type of the navigation keys, which must implement [StableNavKey].
 * @param initialElements The initial list of destinations to populate the back stack with.
 * @return A [NavBackStack] instance initialized with the provided elements.
 */
@Composable
fun <T : StableNavKey> rememberTypedNavBackStack(
    initialElements: ImmutableList<T> = persistentListOf(),
): NavBackStack<T> {
    return rememberSaveable(
        saver = navBackStackSaver()
    ) {
        buildNavBackStack(initialElements)
    }
}

/**
 * Saves and restores [NavBackStack] using Bundle-compatible Parcelable route keys.
 */
private fun <T : StableNavKey> navBackStackSaver(): Saver<NavBackStack<T>, List<T>> =
    Saver(
        save = { backStack -> backStack.toList() },
        restore = { savedRoutes -> buildNavBackStack(savedRoutes) }
    )

private fun <T : StableNavKey> buildNavBackStack(routes: List<T>): NavBackStack<T> =
    NavBackStack<T>().apply {
        routes.forEach(::add)
    }

@Stable
class NavigationState<T : StableNavKey>(
    val startRoute: T,
    topLevelRoute: MutableState<T>,
    val backStacks: Map<T, NavBackStack<T>>,
    val topLevelHistory: SnapshotStateList<T>
) {
    var topLevelRoute: T by topLevelRoute

    val currentBackStack: NavBackStack<T>
        get() = backStacks[topLevelRoute] ?: error("Stack for $topLevelRoute not found")

    val currentRoute: T
        get() = currentBackStack.last()

    /**
     * Returns the top-level routes that should be visible to a content-area `NavDisplay`.
     *
     * The list mirrors the tab history maintained by [Navigator]: previous top-level roots are
     * listed first and the selected top-level route is last, so Navigation 3 can treat bottom-bar
     * back navigation as a real pop while each tab keeps its own independent stack.
     */
    val topLevelRoutesInUse: List<T>
        get() = (topLevelHistory + topLevelRoute).distinct()

    /**
     * Converts each active top-level root into a decorated [NavEntry] for content-only NavDisplays.
     *
     * This follows the Navigation 3 multiple-back-stacks recipe by decorating entries per top-level
     * stack. Only the root entry from historical stacks is exposed here because pushed destinations
     * are still rendered by the outer activity-like NavDisplay.
     */
    @Composable
    fun toDecoratedTopLevelEntries(
        entryProvider: (T) -> NavEntry<T>,
    ): List<NavEntry<T>> {
        val decoratedEntriesByStack = backStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = rememberNavigationEntryDecorators(),
                entryProvider = entryProvider,
            )
        }

        return topLevelRoutesInUse.mapNotNull { route ->
            decoratedEntriesByStack[route]?.firstOrNull()
        }
    }
}

@Stable
class Navigator<T : StableNavKey>(val state: NavigationState<T>) {
    /**
     * Navigates by mutating the app-owned Navigation 3 back stack.
     *
     * Top-level destinations select their own stack and clear sub-routes so bottom navigation and
     * rail taps always reveal the requested root destination. Other destinations are pushed onto the
     * active stack unless they are already current.
     */
    fun navigate(route: T) {
        if (route == state.currentRoute) return

        if (route in state.backStacks.keys) {
            if (state.topLevelRoute != route) {
                state.topLevelHistory.remove(route)
                state.topLevelHistory.add(state.topLevelRoute)
                state.topLevelRoute = route
            } else {
                state.currentBackStack.popToRoot()
            }
            return
        }

        state.currentBackStack.add(route)
    }

    /**
     * Returns true when the current back stack can handle a back action.
     */
    fun canGoBack(): Boolean {
        val currentRoute = state.currentBackStack.lastOrNull() ?: return false

        return if (currentRoute == state.topLevelRoute) {
            state.topLevelHistory.isNotEmpty() || state.topLevelRoute != state.startRoute
        } else {
            true
        }
    }

    /**
     * Handles a back action, returning true when navigation consumed the request.
     */
    fun goBack(): Boolean {
        val currentBackStack = state.currentBackStack
        val currentRoute = currentBackStack.lastOrNull() ?: return false

        if (currentRoute == state.topLevelRoute) {
            while (state.topLevelHistory.isNotEmpty()) {
                val previousTopLevelRoute = state.topLevelHistory.removeLastOrNull()
                if (previousTopLevelRoute != null && previousTopLevelRoute != state.topLevelRoute) {
                    state.topLevelRoute = previousTopLevelRoute
                    return true
                }
            }

            if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
                return true
            }
        } else {
            currentBackStack.removeLastOrNull()
            return true
        }

        return false
    }
}

private fun <T : StableNavKey> NavBackStack<T>.popToRoot() {
    while (size > 1) {
        removeLastOrNull()
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
