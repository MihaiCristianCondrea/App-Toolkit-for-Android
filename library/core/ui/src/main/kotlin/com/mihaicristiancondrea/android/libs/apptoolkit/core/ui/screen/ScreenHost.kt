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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.AnalyticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

/**
 * The boundary of one screen: reports each visit to it, reports its [status], and shows its
 * ViewModel's messages. Every screen, tab, page, sheet or pane with a screen name of its own is
 * wrapped in one.
 *
 * ```
 * ScreenHost(screen = AppScreens.NEWS, status = state.articles, viewModel = viewModel) {
 *     NewsScreenContent(state = state, onEvent = viewModel::onEvent)
 * }
 * ```
 *
 * It adds no layout and draws nothing of its own: no scaffold, no app bar, no padding, and it does
 * not render [status]; its [fabs] are drawn by the scaffold around it. [content] renders the
 * state, each `Loadable` section through [ScreenStateHandler]. Navigation and the ViewModel stay
 * with the caller.
 *
 * A screen that draws its own `PageScaffold` puts `ScreenHost` inside it, as the page's content.
 * Wrapped around the page instead, its [fabs] and messages go to the scaffold around the page,
 * where a page has none, so they never show.
 *
 * **Visits.** A visit is reported as a GA4 screen view when the screen becomes visible and
 * active: when its [LocalLifecycleOwner] reaches `RESUMED`. In the shell that is once its
 * transition settles and nothing covers it, so a screen that is only animating in or out, kept for
 * a back gesture, or under an overlay is not reported. The visit lasts until the screen leaves
 * composition (another tab, a page over it, popped) or the app goes to the background, and the
 * next `RESUMED` starts a new one. A configuration change keeps the visit, so rotating reports
 * nothing; after process death, the return is a new visit. Both panes of a list-detail layout are
 * separate destinations, so each reports its own visits.
 *
 * **Status.** [status], when given, is reported as a `screen_state` event at the start of each
 * visit and whenever its [TrackedStatus.trackingLabel] changes, never twice in a row for the same
 * label. A screen with nothing that loads passes none, and a screen with several loading sections
 * passes the one that says how the screen is doing, or a status of its own.
 *
 * @param screen the stable name and class reported for this screen.
 * @param status what the screen shows now, such as its main `Loadable`, or null.
 * @param viewModel whose queued messages are shown through the surrounding Toolkit scaffold. A
 * screen that shows its messages in a snackbar host of its own calls [MessageHost] itself.
 * @param onMessageAction called when a message's action is pressed.
 * @param fabs the screen's floating action buttons for its state now, drawn by the surrounding
 * Toolkit scaffold like [ScaffoldFabs]; null declares none, leaving the destination's own. Declared
 * here, once for the screen, they follow its state without the cases of a [StateContent] both
 * declaring buttons while one fades into the other.
 *
 * **Entrances.** The screen's `animateVisibility` elements cascade in once it settles, in the
 * order they are shown: once it is resumed, or earlier, while it is started, when its
 * [LocalEntranceCue] is reached, which the shell does well into the transition bringing it in, so
 * its content arrives as it lands. A loading case of [ScreenStateHandler] stays until then. They play each
 * time the screen is opened, not on going back to it, unless `LocalEntranceReplay` says every
 * visit (`EntranceReplay.EveryVisit`) or only the first copy in a run (`EntranceReplay.FirstVisit`);
 * coming back from the background, rotating or scrolling within the visit never replays them.
 */
@Composable
fun ScreenHost(
    screen: TrackedScreen,
    status: TrackedStatus? = null,
    viewModel: ScreenViewModel<*, *>? = null,
    onMessageAction: (UiMessage) -> Unit = {},
    fabs: List<ToolkitFab>? = null,
    content: @Composable () -> Unit,
) {
    val tracker: ScreenVisitTracker = rememberScreenVisitTracker(screen)
    val view = LocalView.current
    val opening: Int = LocalScreenOpening.current
    val entrance: ScreenEntrance = remember(tracker.entranceId, view, opening) {
        ScreenEntrance(tracker.entranceId, view, tracker.firstInRun, tracker.openedAt(opening))
    }
    TrackScreenVisits(tracker = tracker, entrance = entrance, status = status)
    if (fabs != null) {
        ScaffoldFabs(fabs)
    }
    CompositionLocalProvider(LocalScreenEntrance provides entrance) {
        // Watches the screen's scrolling, so rows a scroll brings in appear rather than enter.
        Box(Modifier.nestedScroll(entrance.scrollWatcher), propagateMinConstraints = true) {
            content()
        }
    }
    if (viewModel != null) {
        MessageHost(viewModel = viewModel, onAction = onMessageAction)
    }
}

@Composable
private fun TrackScreenVisits(tracker: ScreenVisitTracker, entrance: ScreenEntrance, status: TrackedStatus?) {
    val telemetry: AnalyticsReporter = LocalTelemetry.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity: Activity? = LocalActivity.current
    val label: String? = status?.trackingLabel
    val currentLabel by rememberUpdatedState(label)
    val currentTelemetry by rememberUpdatedState(telemetry)

    DisposableEffect(lifecycleOwner, tracker) {
        val changingConfigurations = { activity?.isChangingConfigurations == true }
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    tracker.onResumed(currentLabel, currentTelemetry)
                    entrance.resumed = true
                    entrance.settled = true
                }
                Lifecycle.Event.ON_PAUSE -> entrance.settled = false
                Lifecycle.Event.ON_STOP -> tracker.onLeft(changingConfigurations())
                else -> Unit
            }
        }
        // Adding the observer replays the events up to the current state, so a screen composed
        // while already resumed starts its visit here.
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            tracker.onLeft(changingConfigurations())
            tracker.onDisposed(changingConfigurations())
        }
    }
    LaunchedEffect(tracker, label) { tracker.onStatus(label, currentTelemetry) }
    val cue: EntranceCue? = LocalEntranceCue.current
    if (cue != null) {
        // The entrance may begin before the transition ends: once the screen is started and the
        // cue reached, its content arrives as the screen lands. After the first resume, resuming
        // alone settles it, so a screen under an overlay does not start entering.
        LaunchedEffect(lifecycleOwner, cue, entrance) {
            lifecycleOwner.lifecycle.currentStateFlow.collectLatest { state ->
                if (!entrance.resumed && state.isAtLeast(Lifecycle.State.STARTED)) {
                    snapshotFlow { cue.reached() }.first { it }
                    entrance.settled = true
                }
            }
        }
    }
}

/**
 * The [ScreenVisitTracker] for [screen] in the current destination. It lives in the destination's
 * `ViewModelStoreOwner`, so it survives a configuration change and is gone after process death or
 * once the destination is popped. Without an owner, as in a preview, it lives in the composition.
 */
@Composable
private fun rememberScreenVisitTracker(screen: TrackedScreen): ScreenVisitTracker {
    val owner = LocalViewModelStoreOwner.current
    return if (owner != null) {
        val holder: ScreenVisits = viewModel(viewModelStoreOwner = owner, key = SCREEN_VISITS_KEY) {
            ScreenVisits()
        }
        remember(holder, screen) { holder.trackerFor(screen) }
    } else {
        remember(screen) { ScreenVisitTracker(screen) }
    }
}

private const val SCREEN_VISITS_KEY: String =
    "com.mihaicristiancondrea.android.libs.apptoolkit.ScreenVisits"
