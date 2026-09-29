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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell

import com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome.rememberShellFrameState
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome.ShellFrame
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.core.util.Consumer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome.ShellChrome
import androidx.navigationevent.NavigationEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.BackEdgeStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.LocalShellSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.settings.ShellPreferences
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ContentWidthBox
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.ListPlaceholder
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalTopBarStyleOverride
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.PageScaffold
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalPageKey
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavDisplay
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ShellHomeRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DestinationKind
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalShellLayout
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutPolicy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.rememberShellLayoutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.rememberShellMotion
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.rememberShellNavigator
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ListDetailScene
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ListDetailSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.PageChrome
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.PageSceneStrategy
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.ShellEntryInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes.topShellInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.ScreenTransition
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode
import androidx.navigation3.scene.Scene

/**
 * The whole app below the activity: every tab, page, bar, rail, drawer and transition described
 * by [graph].
 *
 * Call it once, from the activity's `setContent`, inside the app's theme (`AppTheme`), with
 * edge-to-edge enabled. The activity stays the only one the app has; pages replace the activities
 * it would otherwise open, including their animation and the system back gesture.
 *
 * The intents the activity receives, the one it was launched with and any that arrive while it
 * runs, open the destinations the graph's `deepLinks` map them to. An activity that should
 * receive new intents instead of being recreated declares `launchMode="singleTop"`.
 *
 * Work that needs the activity itself, such as a consent form, an in-app review, an update flow,
 * a purchase, or a permission request, is done from the page that needs it, with
 * `LocalActivity.current` and `rememberLauncherForActivityResult`: every page is composed inside
 * the one activity.
 *
 * @param start Where a fresh launch opens, instead of the graph's `start`: a tab, or a page shown
 * as a start screen before the shell.
 * @param resolveStart Decides [start] at launch from stored state, such as a welcome screen until
 * the person has seen it once. Nothing is drawn until it returns; null keeps [start]. Keep the
 * splash screen up until [onReady].
 * @param preferences Where the shell keeps its settings. By default a DataStore of its own; an app
 * with a settings store supplies its own implementation.
 * @param onReady Called once the first frame of the app is composed, the settings and the start
 * decided: the moment to let the splash screen go.
 * @param onDestinationChanged Called with the destination on top whenever it changes, first with
 * the one the app opens on: for analytics and the like.
 * @param layoutPolicy The window widths at which the navigation changes form, and how wide content
 * may grow on large windows.
 */
@Composable
fun ShellHost(
    graph: ShellGraph,
    modifier: Modifier = Modifier,
    start: NavKey? = null,
    resolveStart: (suspend () -> NavKey?)? = null,
    preferences: ShellPreferences? = null,
    onReady: () -> Unit = {},
    onDestinationChanged: (NavKey) -> Unit = {},
    layoutPolicy: ShellLayoutPolicy = ShellLayoutPolicy(),
) {
    val context = LocalContext.current
    val store = preferences ?: remember(context) { ShellPreferences(context) }
    // A cold start waits a frame for the stored settings rather than drawing a layout it would
    // replace a frame later.
    val settings by store.settings.collectAsStateWithLifecycle(initialValue = store.lastKnown)
    // The app's start decision, awaited alongside the settings.
    var decidedStart by remember { mutableStateOf(if (resolveStart == null) StartDecision(start) else null) }
    if (resolveStart != null) {
        LaunchedEffect(Unit) { decidedStart = StartDecision(resolveStart() ?: start) }
    }
    val current = settings ?: return
    val launchStart = decidedStart ?: return

    val activity = LocalActivity.current
    val layoutInfo = rememberShellLayoutInfo(layoutPolicy, current.layoutMode, current.limitContentWidth)
    val motion = rememberShellMotion(
        tabStyle = current.tabTransition,
        durationScale = current.animationSpeed.durationScale,
        followFingerFromRight = current.backEdgeStyle == BackEdgeStyle.FollowFinger,
    )
    // The developer options' choice, then the app's choice at launch, then the graph's.
    val startKey = graph.startOptions.getOrNull(current.startOverride) ?: launchStart.key ?: graph.start
    val navigator = rememberShellNavigator(graph, onExit = { activity?.finish() }, start = startKey)
    val frameState = rememberShellFrameState()
    DeepLinks(graph, navigator)
    val currentOnDestinationChanged by rememberUpdatedState(onDestinationChanged)
    LaunchedEffect(navigator) {
        snapshotFlow { navigator.currentKey }.distinctUntilChanged().collect { currentOnDestinationChanged(it) }
    }
    val currentOnReady by rememberUpdatedState(onReady)
    LaunchedEffect(Unit) { currentOnReady() }
    val sceneStrategies = remember(layoutInfo.listDetail) {
        listOf(ListDetailSceneStrategy<NavKey>(layoutInfo.listDetail), PageSceneStrategy())
    }

    CompositionLocalProvider(
        LocalShellGraph provides graph,
        LocalShellNavigator provides navigator,
        LocalShellPreferences provides store,
        LocalShellSettings provides current,
        LocalShellLayout provides layoutInfo,
        LocalShellMotion provides motion,
        LocalTopBarStyleOverride provides current.topBarOverride.style,
        // Text drawn outside any Material surface takes the theme's colour instead of black.
        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
    ) {
        val entries = rememberDecoratedNavEntries(
            backStack = navigator.pages,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = { key -> pageEntry(graph, navigator, key) },
        )
        // Beside a rail or a permanent drawer, the displays sit next to the navigation, so pages
        // open there; otherwise the frame adds nothing.
        ShellFrame(graph, navigator, frameState) {
            ShellNavDisplay(
                entries = entries,
                sceneStrategies = sceneStrategies,
                onBack = { navigator.popPage() },
                // Seen around the pages while the back gesture shrinks them, as behind closing windows.
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceDim),
                // Pages move as their destination declares, like activities by default; the
                // predictive gesture on anything but the activity transition seeks it instead.
                transitionSpec = { motion.screens.forward(targetState.pageTransition(graph, layoutInfo.mode)) },
                popTransitionSpec = { motion.screens.back(initialState.pageTransition(graph, layoutInfo.mode)) },
                activityBack = {
                    graph.transitionOf(navigator.pages.last(), layoutInfo.mode) == ScreenTransition.Activity
                },
                predictivePopTransitionSpec = { swipeEdge ->
                    motion.screens.back(
                        initialState.pageTransition(graph, layoutInfo.mode),
                        mirrored = motion.followFingerFromRight && swipeEdge == NavigationEvent.EDGE_RIGHT,
                    )
                },
            )
        }
    }
}

/** Where a launch opens, as the app decided it: null leaves it to the graph. */
private class StartDecision(val key: NavKey?)

/**
 * Opens the destinations the activity's intents ask for: the launch intent once, not again when
 * the activity is recreated, and every new intent while it runs.
 */
@Composable
private fun DeepLinks(graph: ShellGraph, navigator: ShellNavigator) {
    val activity = LocalActivity.current
    var launchHandled by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(navigator) {
        if (!launchHandled) {
            launchHandled = true
            activity?.intent?.let(graph::keyFor)?.let(navigator::navigate)
        }
    }
    val intents = activity as? ComponentActivity ?: return
    DisposableEffect(intents, navigator) {
        val listener = Consumer<Intent> { intent -> graph.keyFor(intent)?.let(navigator::navigate) }
        intents.addOnNewIntentListener(listener)
        onDispose { intents.removeOnNewIntentListener(listener) }
    }
}

private fun Scene<*>.pageTransition(graph: ShellGraph, layout: ShellLayoutMode): ScreenTransition {
    val info = topShellInfo ?: return graph.transitions.pages
    return graph.transitions.resolve(info.kind, info.transition, layout)
}

private fun pageEntry(graph: ShellGraph, navigator: ShellNavigator, key: NavKey): NavEntry<NavKey> {
    if (key == ShellHomeRoute) {
        return NavEntry(
            key = key,
            contentKey = key.toString(),
            metadata = ShellEntryInfo(DestinationKind.Page, tabIndex = -1, PaneRole.None).toMetadata(),
        ) {
            ShellChrome(graph, navigator)
        }
    }
    val destination = graph.destination(key)
    val placeholder: Map<String, Any> = if (destination.paneRole == PaneRole.List) {
        // The page's own placeholder, or the shell's.
        val content: @Composable () -> Unit = destination.placeholder ?: { ListPlaceholder() }
        metadata { put(ListDetailScene.DetailPlaceholderKey, content) }
    } else {
        emptyMap()
    }
    val title = destination.title
    val chrome: Map<String, Any> = if (title != null) {
        PageChrome(
            title = { title(key) },
            actions = destination.actions?.let { actions -> { actions(this, key) } },
            key = key,
        ).toMetadata()
    } else {
        emptyMap()
    }
    return NavEntry(
        key = key,
        contentKey = key.toString(),
        metadata = ShellEntryInfo(DestinationKind.Page, tabIndex = -1, destination.paneRole, destination.transition).toMetadata() +
            placeholder + chrome,
    ) { pageKey ->
        CompositionLocalProvider(LocalPageKey provides pageKey) {
            if (destination.scaffold && title != null) {
                PageScaffold(
                    title = title(pageKey),
                    style = destination.topBar,
                    actions = { destination.actions?.invoke(this, pageKey) },
                    floatingActionButton = { destination.floatingActionButton?.invoke(pageKey) },
                    fabs = destination.floatingActionButtons?.invoke(pageKey).orEmpty(),
                ) {
                    ContentWidthBox(maxWidth = LocalShellLayout.current.maxWidthFor(destination.contentWidth)) {
                        destination.content(pageKey)
                    }
                }
            } else {
                destination.content(pageKey)
            }
        }
    }
}
