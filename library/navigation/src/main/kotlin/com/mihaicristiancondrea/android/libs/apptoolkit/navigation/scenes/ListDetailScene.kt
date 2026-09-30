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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.coroutineScope
import androidx.navigationevent.NavigationEvent
import androidx.compose.ui.graphics.graphicsLayer
import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ContentCardShape
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FollowScrollWithFrameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FrameTint
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.LocalBesideNavigation
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.isTopLevelPage
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.motion.LocalShellMotion
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A list page with the detail opened from it beside it, or with a placeholder until one is.
 *
 * The scene draws one app bar across both panes, the list's title over the list and the detail's
 * over the detail, instead of letting each page draw its own. Between the panes sits a separator
 * the person can drag to resize them. Dragging it most of the way to the end closes the detail,
 * and the back gesture on a detail does the same thing under the finger: the separator follows the
 * gesture toward the end, the detail sliding out with it, and settles back once the detail closes.
 *
 * The scene is keyed by the list, so opening, switching or closing details never moves the page as
 * a whole. Entering and leaving the list still uses the page transitions, like any other page.
 *
 * The scene, its list and detail metadata and its width-gated strategy are adapted from the
 * list-detail scene recipe in [android/nav3-recipes](https://github.com/android/nav3-recipes),
 * Apache License 2.0. The shared app bar, the separator and slide to pop are the shell's; the
 * separator follows the "slide to pop" animation in *3 unique predictive back animations you can
 * create with the Navigation Events library* (tunjid.com).
 */
data class ListDetailScene<T : Any>(
    override val key: Any,
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>?,
    override val previousEntries: List<NavEntry<T>>,
    private val onBack: () -> Unit,
    /**
     * Whether the scene is a window of its own, with the shared app bar, as pages are; false
     * inside a tab, under the shell's app bar.
     */
    private val framed: Boolean = true,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOfNotNull(listEntry, detailEntry)

    override val content: @Composable () -> Unit = {
        val layout: @Composable () -> Unit = {
            ListDetailLayout(
                listEntry = listEntry,
                detailEntry = detailEntry,
                closeDetail = onBack,
                // Back pops one page at a time; the list's back arrow closes the detail with it.
                closeList = { repeat(entries.size) { onBack() } },
                framed = framed,
            )
        }
        if (framed) PageSurface(layout) else layout()
    }

    object DetailPlaceholderKey : NavMetadataKey<@Composable () -> Unit>
}

/** The back gesture on a detail as last seen, kept for the moment back completes. */
private class ListDetailGesture {
    var fromDetailSide: Boolean = false
    var progress: Float = 0f

    fun clear() {
        fromDetailSide = false
        progress = 0f
    }
}

/** How small the detail gets under a back gesture from its own side, as a closing window does. */
private const val DetailBackScale = 0.9f

/** How far the detail leans toward the list under that gesture. */
private val DetailBackLean = 24.dp

/** The corners the shrinking detail takes. */
private val DetailBackCorner = 24.dp

@Composable
private fun <T : Any> ListDetailLayout(
    listEntry: NavEntry<T>,
    detailEntry: NavEntry<T>?,
    closeDetail: () -> Unit,
    closeList: () -> Unit,
    framed: Boolean,
) {
    val motion = LocalShellMotion.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val direction = if (LocalLayoutDirection.current == LayoutDirection.Rtl) -1f else 1f
    val hasDetail = detailEntry != null

    // Where the separator rests, as the list's share of the width. The person moves it.
    var restingFraction by rememberSaveable { mutableFloatStateOf(DefaultListFraction) }
    // Inside a tab the list fills the width until a detail opens; the detail then slides in
    // beside it rather than appearing at once.
    val fraction = remember { Animatable(if (framed) restingFraction else 1f) }
    if (!framed) {
        LaunchedEffect(Unit) {
            fraction.animateTo(restingFraction, tween((350 * motion.durationScale).roundToInt()))
        }
    }

    // The back gesture on a detail follows the finger. From the list's side, the separator slides
    // to the end with it. From the detail's own side the finger moves toward the list, where a
    // separator sliding the other way felt wrong, so the detail shrinks in place and leans after
    // the finger instead, as a window does under the system's gesture from that edge.
    val backState = rememberNavigationEventState(NavigationEventInfo.None)
    val gesture = backState.transitionState
    val latestEvent = (gesture as? NavigationEventTransitionState.InProgress)?.latestEvent
    val gestureProgress = latestEvent?.progress ?: 0f
    val detailEdge = if (direction > 0f) NavigationEvent.EDGE_RIGHT else NavigationEvent.EDGE_LEFT
    val fromDetailSide = latestEvent?.swipeEdge == detailEdge
    val shownFraction = if (fromDetailSide) fraction.value else lerp(fraction.value, 1f, gestureProgress)
    // How far the detail has shrunk in place, and how visible it still is, once released.
    val inPlace = remember { Animatable(0f) }
    val inPlaceAlpha = remember { Animatable(1f) }
    // The gesture as last seen, for the moment back completes: the state may be idle by then.
    val lastGesture = remember { ListDetailGesture() }
    if (latestEvent != null) {
        lastGesture.fromDetailSide = fromDetailSide
        lastGesture.progress = gestureProgress
    }
    val detailShrink = if (fromDetailSide) gestureProgress else inPlace.value
    val slideToPop: suspend () -> Unit = {
        fraction.animateTo(1f, tween((200 * motion.durationScale).roundToInt()))
        closeDetail()
        fraction.animateTo(restingFraction, tween((350 * motion.durationScale).roundToInt()))
    }
    val shrinkToPop: suspend (from: Float) -> Unit = { from ->
        val duration = (200 * motion.durationScale).roundToInt()
        inPlace.snapTo(from)
        coroutineScope {
            launch { inPlaceAlpha.animateTo(0f, tween(duration)) }
            inPlace.animateTo(1f, tween(duration))
        }
        closeDetail()
        inPlace.snapTo(0f)
        inPlaceAlpha.snapTo(1f)
    }
    NavigationBackHandler(
        state = backState,
        isBackEnabled = hasDetail,
        onBackCancelled = { lastGesture.clear() },
        onBackCompleted = {
            val fromDetail = lastGesture.fromDetailSide
            val progress = lastGesture.progress
            lastGesture.clear()
            scope.launch {
                if (fromDetail) {
                    shrinkToPop(progress)
                } else {
                    fraction.snapTo(shownFraction)
                    slideToPop()
                }
            }
        },
    )

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val panesWidth = maxWidth - SeparatorWidth
        val listWidth = panesWidth * shownFraction
        // The detail keeps the width it rests at while it slides out, rather than squeezing.
        val detailWidth = panesWidth * (1f - minOf(shownFraction, restingFraction))
        val widthPx = with(density) { panesWidth.toPx() }.coerceAtLeast(1f)
        val separatorInteractions = remember { MutableInteractionSource() }
        val dragState = rememberDraggableState { delta ->
            val max = if (hasDetail) 1f else MaxRestingFraction
            scope.launch { fraction.snapTo((fraction.value + direction * delta / widthPx).coerceIn(MinFraction, max)) }
        }

        // The list the navigation beside it opened stands in for a tab, and is drawn like one.
        val beside = LocalBesideNavigation.current
        val topLevel = framed && isTopLevelPage(listEntry.pageChrome?.key)
        val carded = topLevel && beside?.tinted == true
        // The bar spans both panes, so the navigation's shared colour follows whichever of them
        // has content scrolled under it. A new detail starts at its top.
        val listScroll = remember { PaneScrollOffset() }
        val detailScroll = remember { PaneScrollOffset() }
        LaunchedEffect(detailEntry?.contentKey) { detailScroll.offset = 0f }
        if (framed) {
            FollowScrollWithFrameTint {
                if (listScroll.scrolledUnder || detailScroll.scrolledUnder) FrameTint.Full else FrameTint.None
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .then(if (carded && beside != null) Modifier.drawBehind { drawRect(beside.frameColor()) } else Modifier),
        ) {
            if (framed) {
                ListDetailTopBar(
                    listChrome = listEntry.pageChrome,
                    detailChrome = detailEntry?.pageChrome,
                    listWidth = listWidth,
                    onCloseList = closeList,
                    showsBack = !topLevel,
                    // Over the shared frame colour, the bar draws no colour of its own.
                    containerColor = if (carded) Color.Transparent else MaterialTheme.colorScheme.surface,
                )
            }
            Row(
                Modifier
                    .weight(1f)
                    .then(if (carded) Modifier.clip(ContentCardShape).background(MaterialTheme.colorScheme.surface) else Modifier),
            ) {
                Box(Modifier.width(listWidth).fillMaxHeight().nestedScroll(listScroll)) {
                    CompositionLocalProvider(
                        LocalPaneRole provides PaneRole.List,
                        LocalSelectedDetail provides detailEntry?.contentKey,
                    ) {
                        listEntry.Content()
                    }
                }
                PaneSeparator(
                    interactions = separatorInteractions,
                    modifier = Modifier.draggable(
                        state = dragState,
                        orientation = Orientation.Horizontal,
                        interactionSource = separatorInteractions,
                        onDragStopped = {
                            if (hasDetail && fraction.value > PopFraction) {
                                slideToPop()
                            } else {
                                restingFraction = fraction.value.coerceIn(MinFraction, MaxRestingFraction)
                                fraction.animateTo(restingFraction)
                            }
                        },
                    ),
                )
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .graphicsLayer {
                            if (detailShrink <= 0f) return@graphicsLayer
                            val scale = 1f - (1f - DetailBackScale) * detailShrink
                            scaleX = scale
                            scaleY = scale
                            // Toward the list, after the finger.
                            translationX = -direction * DetailBackLean.toPx() * detailShrink
                            alpha = inPlaceAlpha.value
                            shape = RoundedCornerShape(DetailBackCorner * detailShrink)
                            clip = true
                        }
                        .clipToBounds(),
                ) {
                    Box(
                        Modifier
                            .wrapContentWidth(Alignment.Start, unbounded = true)
                            .requiredWidth(detailWidth)
                            .fillMaxHeight()
                            .nestedScroll(detailScroll),
                    ) {
                        CompositionLocalProvider(LocalPaneRole provides PaneRole.Detail) {
                            AnimatedContent(
                                targetState = detailEntry,
                                contentKey = { entry -> entry?.contentKey },
                                transitionSpec = {
                                    val duration = (300 * motion.durationScale).roundToInt()
                                    (fadeIn(tween(duration)) + slideInVertically(tween(duration)) { it / 16 }) togetherWith
                                        fadeOut(tween(duration / 2))
                                },
                                label = "DetailPane",
                            ) { entry ->
                                if (entry != null) {
                                    entry.Content()
                                } else {
                                    listEntry.metadata[ListDetailScene.DetailPlaceholderKey]?.invoke()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * How far a pane's content has scrolled from its top, as Material's pinned app bar counts it: what
 * the content consumed, back to zero once it is pulled past its top.
 */
private class PaneScrollOffset : NestedScrollConnection {
    var offset: Float by mutableFloatStateOf(0f)

    /** Whether content has scrolled under the app bar. */
    val scrolledUnder: Boolean
        get() = offset < -1f

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        offset = if (consumed.y == 0f && available.y > 0f) 0f else offset + consumed.y
        return Offset.Zero
    }
}

/** One small app bar over both panes, each half titled after the page under it. */
@Composable
private fun ListDetailTopBar(
    listChrome: PageChrome?,
    detailChrome: PageChrome?,
    listWidth: Dp,
    onCloseList: () -> Unit,
    showsBack: Boolean,
    containerColor: Color,
) {
    val view = LocalView.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .width(listWidth)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // A list standing in for a tab has no way back but the navigation beside it.
            if (showsBack) {
                // The Toolkit's press feedback: this module sits below `:library:core:ui`, so it
                // cannot use `AnimatedIconButtonDirection`, but it gives the same bounce and sound.
                IconButton(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onCloseList()
                    },
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .bounceClick(),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.shell_navigate_back))
                }
            }
            Text(
                text = listChrome?.title?.invoke().orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = if (showsBack) 4.dp else 16.dp, end = 16.dp),
            )
        }
        Spacer(Modifier.width(SeparatorWidth))
        AnimatedContent(
            targetState = detailChrome,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.weight(1f),
            label = "DetailTitle",
        ) { chrome ->
            Row(
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.End)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = chrome?.title?.invoke().orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                )
                chrome?.actions?.invoke(this)
            }
        }
    }
}

/**
 * The handle between the panes. It widens while dragged or hovered, the way Material's pane
 * expansion handle does, so the person can see what they are holding.
 */
@Composable
private fun PaneSeparator(interactions: MutableInteractionSource, modifier: Modifier) {
    val dragged by interactions.collectIsDraggedAsState()
    val hovered by interactions.collectIsHoveredAsState()
    val handleWidth by animateDpAsState(if (dragged || hovered) 12.dp else 4.dp, label = "HandleWidth")
    val description = stringResource(R.string.shell_pane_separator)
    Box(
        modifier = modifier
            .width(SeparatorWidth)
            .fillMaxHeight()
            .hoverable(interactions)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(width = handleWidth, height = 48.dp)
                .background(
                    if (dragged) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                    CircleShape,
                ),
        )
    }
}

/**
 * Shows list and detail pages side by side while [enabled], which follows the window width.
 *
 * With [framed] false, for the tabs inside the shell, the scene draws no app bar and no window
 * frame, and a list with nothing open beside it stays a single, full-width pane.
 */
class ListDetailSceneStrategy<T : Any>(
    private val enabled: Boolean,
    private val framed: Boolean = true,
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!enabled) return null
        val last = entries.last()
        return when (last.shellInfo?.paneRole) {
            PaneRole.List -> if (!framed) null else ListDetailScene(
                key = last.contentKey,
                listEntry = last,
                detailEntry = null,
                previousEntries = entries.dropLast(1),
                onBack = onBack,
            )

            PaneRole.Detail -> {
                val list = entries.getOrNull(entries.lastIndex - 1)
                    ?.takeIf { it.shellInfo?.paneRole == PaneRole.List }
                    ?: return null
                ListDetailScene(
                    key = list.contentKey,
                    listEntry = list,
                    detailEntry = last,
                    previousEntries = entries.dropLast(1),
                    onBack = onBack,
                    framed = framed,
                )
            }

            else -> null
        }
    }
}

/**
 * The pane the current page is drawn in. A page inside a pane leaves its app bar to the scene,
 * and a detail beside its list has no back button, since the list is already on screen.
 */
val LocalPaneRole = compositionLocalOf { PaneRole.None }

/** The content key of the detail open beside a list, so the list can highlight its row. */
val LocalSelectedDetail = compositionLocalOf<Any?> { null }

private val SeparatorWidth = 24.dp
private const val DefaultListFraction = 0.38f
private const val MinFraction = 0.25f
private const val MaxRestingFraction = 0.6f
private const val PopFraction = 0.8f
