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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.first
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FrameTint

/**
 * A style that replaces every destination's own, set from the developer options to try one bar
 * across the whole app. Null keeps each destination's.
 */
val LocalTopBarStyleOverride = staticCompositionLocalOf<TopBarStyle?> { null }

/**
 * Returns the scroll behaviour that suits [style]: large bars collapse until the content is back
 * at the top, the others stay pinned and only change colour. A [TopBarStyle.LargeCollapsed] bar
 * starts collapsed, once, and keeps whatever the person scrolls it to after that.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTopBarScrollBehavior(style: TopBarStyle): TopAppBarScrollBehavior {
    val pinned = TopAppBarDefaults.pinnedScrollBehavior()
    val collapsing = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    if (!style.isLarge) return pinned
    if (style == TopBarStyle.LargeCollapsed) {
        var collapsed by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(collapsing) {
            if (!collapsed) {
                collapsing.resetTo(collapsed = true)
                collapsed = true
            }
        }
    }
    return collapsing
}

/**
 * How far the navigation's shared colour should follow this bar, the way Material tints the bar
 * itself: a large bar blends as it collapses, the others switch once content scrolls under them.
 * Read it inside `FollowScrollWithFrameTint`.
 */
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior.frameTint(style: TopBarStyle): FrameTint = when {
    style.isLarge -> FrameTint(amount = state.collapsedFraction, snap = true)
    state.overlappedFraction > 0.01f -> FrameTint.Full
    else -> FrameTint.None
}

/**
 * Moves the bar back to where a newly opened destination starts: fully expanded, or fully
 * collapsed once the bar has measured how far it can collapse.
 */
@OptIn(ExperimentalMaterial3Api::class)
suspend fun TopAppBarScrollBehavior.resetTo(collapsed: Boolean) {
    state.contentOffset = 0f
    if (!collapsed) {
        state.heightOffset = 0f
        return
    }
    // The limit is unknown, a huge negative number, until the bar has been laid out once.
    val limit = snapshotFlow { state.heightOffsetLimit }.first { it < 0f && it > -Float.MAX_VALUE }
    state.heightOffset = limit
}

/**
 * The insets an app bar keeps clear of: the status bar and any display cutout, on the top and on
 * the sides the bar reaches. A bar beside a rail or drawer only reaches the end side.
 */
@Composable
fun topBarInsets(reachesStart: Boolean = true): WindowInsets = WindowInsets.safeDrawing.only(
    WindowInsetsSides.Top + if (reachesStart) WindowInsetsSides.Horizontal else WindowInsetsSides.End,
)

/**
 * Draws [style]. Draws nothing for [TopBarStyle.Hidden].
 *
 * @param colors The bar's colours; null keeps Material's, which tint the bar once content scrolls
 * under it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShellTopAppBar(
    style: TopBarStyle,
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    windowInsets: WindowInsets = topBarInsets(),
    search: TopBarSearch? = null,
    colors: TopAppBarColors? = null,
) {
    // A large bar draws its title twice, once expanded and once collapsed; a field cannot be.
    val resolvedStyle = if (search != null && style != TopBarStyle.Hidden) TopBarStyle.Small else style
    // A centred bar centres whatever its title slot measures, so the slot wraps its title there;
    // elsewhere it spans the bar, so the search field can.
    val centred = resolvedStyle == TopBarStyle.CenterAligned
    val titleAlignment = if (centred) Alignment.Center else Alignment.CenterStart
    val titleContent: @Composable () -> Unit = {
        // One slot of one height for the title and the search field, so the bar, its navigation
        // button and its actions stay put while one crossfades into the other.
        AnimatedContent(
            targetState = search to title,
            // Every search field shares one key: moving between two tabs that search keeps the
            // field in place, and only its hint and query change, rather than fading one field
            // out and another in, a flicker.
            contentKey = { (field, text) -> if (field != null) SearchFieldKey else text },
            // A centred title grows and shrinks between titles instead of snapping, as App
            // Toolkit's app bars do.
            modifier = Modifier
                .then(if (centred) Modifier.animateContentSize() else Modifier.fillMaxWidth())
                .height(SearchFieldHeight),
            contentAlignment = titleAlignment,
            transitionSpec = {
                (fadeIn(tween(200, delayMillis = 60)) + scaleIn(initialScale = 0.96f))
                    .togetherWith(fadeOut(tween(90)) + scaleOut(targetScale = 0.98f))
                    .using(sizeTransform = null)
            },
            label = "TopBarTitle",
        ) { (field, text) ->
            if (field != null) {
                TopBarSearchField(field)
            } else {
                Box(
                    modifier = if (centred) Modifier.fillMaxHeight() else Modifier.fillMaxSize(),
                    contentAlignment = titleAlignment,
                ) {
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.animateContentSize(),
                    )
                }
            }
        }
    }
    when (resolvedStyle) {
        TopBarStyle.Small -> TopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = windowInsets,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        TopBarStyle.CenterAligned -> CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = windowInsets,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        TopBarStyle.Large, TopBarStyle.LargeCollapsed -> LargeTopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = windowInsets,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(),
            scrollBehavior = scrollBehavior,
        )

        TopBarStyle.Hidden -> Unit
    }
}

/** The title slot's content key for a search field, whichever tab's it is. */
private object SearchFieldKey
