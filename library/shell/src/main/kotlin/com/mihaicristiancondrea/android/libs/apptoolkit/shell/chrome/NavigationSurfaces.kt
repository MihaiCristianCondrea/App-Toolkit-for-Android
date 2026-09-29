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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.navigation3.runtime.NavKey
import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellTab
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.R
import kotlin.math.roundToInt

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/** What a navigation surface does when one of its rows is chosen. */
internal class NavigationCallbacks(
    val onTabClick: (Int) -> Unit,
    val onEntryClick: (DrawerEntry) -> Unit,
)

/** The drawer's entries before its [DrawerEntry.Spacer], and the pinned footer after it. */
internal fun List<DrawerEntry>.splitAtSpacer(): Pair<List<DrawerEntry>, List<DrawerEntry>> {
    val spacer = indexOf(DrawerEntry.Spacer)
    return if (spacer < 0) this to emptyList() else take(spacer) to drop(spacer + 1).filterNot { it == DrawerEntry.Spacer }
}

/**
 * The bottom navigation: Material 3's standard 80dp bar, or, with [short], the 64dp bar of
 * Material 3 Expressive. [alwaysShowLabels] is the person's Display setting: every item's label,
 * or only the selected one's.
 *
 * A click plays the click sound and a light haptic, as the Toolkit's bars always have, and plays
 * the tab's icon when it animates.
 */
@Composable
internal fun ShellNavigationBar(
    tabs: List<ShellTab>,
    selectedIndex: Int,
    callbacks: NavigationCallbacks,
    short: Boolean,
    alwaysShowLabels: Boolean,
) {
    val feedback = rememberNavigationFeedback(haptic = true)
    val label: (ShellTab) -> @Composable () -> Unit = { tab ->
        { Text(stringResource(tab.shortLabel ?: tab.label), maxLines = 1, overflow = TextOverflow.Ellipsis) }
    }
    if (short) {
        ShortNavigationBar {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedIndex
                var clicks by remember(tab.key) { mutableIntStateOf(0) }
                ShortNavigationBarItem(
                    selected = selected,
                    onClick = {
                        clicks++
                        feedback()
                        callbacks.onTabClick(index)
                    },
                    icon = { TabIcon(tab, selected, clicks) },
                    label = if (alwaysShowLabels || selected) label(tab) else null,
                )
            }
        }
    } else {
        NavigationBar {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedIndex
                var clicks by remember(tab.key) { mutableIntStateOf(0) }
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        clicks++
                        feedback()
                        callbacks.onTabClick(index)
                    },
                    icon = { TabIcon(tab, selected, clicks) },
                    // Material's own switch keeps the label's space and animates it in and out
                    // for the selected-only case.
                    label = label(tab),
                    alwaysShowLabel = alwaysShowLabels,
                )
            }
        }
    }
}

/**
 * The rail for the rail and expanded rail layouts.
 *
 * Unlike Material's own rail it holds the drawer entries too, below the tabs, and scrolls when
 * they do not fit, which a short landscape window needs. Entries after the drawer's spacer stay
 * pinned to the bottom while everything fits.
 */
@Composable
internal fun ShellRail(
    graph: ShellGraph,
    selectedIndex: Int,
    expanded: Boolean,
    callbacks: NavigationCallbacks,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    /** The drawer entry whose page is open beside the rail, drawn as selected. */
    selectedEntry: NavKey? = null,
) {
    val feedback = rememberNavigationFeedback(haptic = true)
    val width by animateDpAsState(if (expanded) ExpandedRailWidth else CollapsedRailWidth, label = "RailWidth")
    // How far the rail is between collapsed (0) and expanded (1), following its animated width.
    // Alignment and padding move with it; switching them at once made the menu button jump to
    // the middle of the still-wide rail and then slide back as the width caught up.
    val openness = ((width - CollapsedRailWidth) / (ExpandedRailWidth - CollapsedRailWidth)).coerceIn(0f, 1f)
    val alignment = BiasAlignment.Horizontal(-openness)
    val (top, footer) = remember(graph.drawer) { graph.drawer.splitAtSpacer() }
    // The rail's own width comes after the start inset, so a display cutout or a three-button
    // navigation bar on that side widens the surface instead of squeezing the items.
    Surface(modifier = modifier.fillMaxHeight(), color = containerColor) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Top))
                .width(width),
            horizontalAlignment = alignment,
        ) {
            // The header lines up with the app bar beside it. Expanded, it names the app next to the
            // button, which frees the app bar to name the destination instead. The button keeps one
            // place in both states, centred in the collapsed rail, so neither the width nor the
            // title appearing beside it can move it.
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                    .height(64.dp)
                    .padding(start = (CollapsedRailWidth - MenuButtonSize) / 2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedIconButtonDirection(
                    icon = ToolkitIcon.Vector(if (expanded) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Outlined.Menu),
                    contentDescription = stringResource(
                        if (expanded) R.string.shell_collapse_navigation else R.string.shell_expand_navigation,
                    ),
                    onClick = onMenuClick,
                    feedback = ButtonFeedback(hapticFeedbackType = null),
                )
                AnimatedVisibility(visible = expanded, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = stringResource(graph.appTitle),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                    )
                }
            }
            val itemModifier = Modifier.padding(horizontal = 12.dp * openness)
            PinnedFooterColumn(
                horizontalAlignment = alignment,
                spacing = 4.dp * (1f - openness),
                top = {
                    graph.tabs.forEachIndexed { index, tab ->
                        val selected = index == selectedIndex
                        var clicks by remember(tab.key) { mutableIntStateOf(0) }
                        WideNavigationRailItem(
                            selected = selected,
                            onClick = {
                                clicks++
                                feedback()
                                callbacks.onTabClick(index)
                            },
                            icon = { TabIcon(tab, selected, clicks) },
                            label = { RailLabel(if (expanded) tab.label else tab.shortLabel ?: tab.label) },
                            railExpanded = expanded,
                            modifier = itemModifier,
                        )
                    }
                    if (top.isNotEmpty()) RailDivider(expanded)
                    top.forEach { entry -> RailEntry(entry, expanded, itemModifier, callbacks, entry.opens(selectedEntry)) }
                },
                footer = {
                    footer.forEach { entry -> RailEntry(entry, expanded, itemModifier, callbacks, entry.opens(selectedEntry)) }
                    Spacer(Modifier.size(8.dp))
                },
            )
        }
    }
}

/**
 * The contents of the modal drawer ([showTabs] false: the navigation bar already has the tabs)
 * and of the permanent drawer ([showTabs] true).
 */
@Composable
internal fun ColumnScope.ShellDrawerContent(
    graph: ShellGraph,
    selectedIndex: Int,
    showTabs: Boolean,
    callbacks: NavigationCallbacks,
    /** The drawer entry whose page is open beside the drawer, drawn as selected. */
    selectedEntry: NavKey? = null,
) {
    val (top, footer) = remember(graph.drawer) { graph.drawer.splitAtSpacer() }
    DrawerHeader(graph)
    PinnedFooterColumn(
        modifier = Modifier.weight(1f),
        top = {
            if (showTabs) {
                graph.tabs.forEachIndexed { index, tab ->
                    DrawerRow(
                        label = tab.label,
                        icon = tab.icon,
                        selectedIcon = tab.selectedIcon,
                        selected = index == selectedIndex,
                        badge = tab.badge,
                        onClick = { callbacks.onTabClick(index) },
                    )
                }
                if (top.isNotEmpty()) {
                    HorizontalDivider(Modifier.padding(horizontal = 28.dp, vertical = 8.dp))
                }
            }
            top.forEach { entry -> DrawerEntryRow(entry, callbacks, entry.opens(selectedEntry)) }
        },
        footer = {
            footer.forEach { entry -> DrawerEntryRow(entry, callbacks, entry.opens(selectedEntry)) }
            Spacer(Modifier.size(12.dp))
        },
    )
}

/**
 * Keeps the bottom bar in step with [scrollBehavior], sliding it down out of the window as the
 * content scrolls down and back up as it scrolls up. The reported height shrinks with it, so the
 * content above grows into the space instead of leaving a gap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HideOnScrollBottomBar(
    scrollBehavior: BottomAppBarScrollBehavior?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val offset = scrollBehavior?.state?.let { state ->
                state.heightOffsetLimit = -placeable.height.toFloat()
                state.heightOffset
            } ?: 0f
            val height = (placeable.height + offset).roundToInt().coerceAtLeast(0)
            layout(placeable.width, height) { placeable.place(0, 0) }
        },
    ) {
        content()
    }
}

/**
 * A tab's icon: [ShellTab.icon], or [ShellTab.selectedIcon] while selected, played on every click
 * when it animates, and pressed in with the Toolkit's bounce.
 */
@Composable
private fun TabIcon(tab: ShellTab, selected: Boolean, clicks: Int) {
    val icon: @Composable () -> Unit = {
        AnimatedToolkitIcon(
            icon = tab.icon,
            selectedIcon = tab.selectedIcon,
            selected = selected,
            clickCount = clicks,
            contentDescription = null,
            modifier = Modifier
                .size(SizeConstants.TwentyFourSize)
                .bounceClick(),
        )
    }
    val badge = tab.badge
    if (badge != null) {
        BadgedBox(badge = { Badge { Text(badge) } }) { icon() }
    } else {
        icon()
    }
}

@Composable
private fun RailLabel(label: Int) {
    Text(stringResource(label), maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun RailDivider(expanded: Boolean) {
    HorizontalDivider(Modifier.padding(horizontal = if (expanded) 28.dp else 24.dp, vertical = 8.dp))
}

@Composable
private fun RailEntry(
    entry: DrawerEntry,
    expanded: Boolean,
    modifier: Modifier,
    callbacks: NavigationCallbacks,
    selected: Boolean,
) {
    val (fullLabel, icon) = entry.labelAndIcon() ?: return
    val label = if (expanded) fullLabel else entry.shortLabel ?: fullLabel
    val feedback = rememberNavigationFeedback(haptic = false)
    var clicks by remember(entry) { mutableIntStateOf(0) }
    WideNavigationRailItem(
        selected = selected,
        onClick = {
            clicks++
            feedback()
            callbacks.onEntryClick(entry)
        },
        icon = {
            AnimatedToolkitIcon(
                icon = icon,
                clickCount = clicks,
                contentDescription = null,
                modifier = Modifier
                    .size(SizeConstants.TwentyFourSize)
                    .bounceClick(),
            )
        },
        label = { RailLabel(label) },
        railExpanded = expanded,
        modifier = modifier,
    )
}

@Composable
private fun DrawerEntryRow(entry: DrawerEntry, callbacks: NavigationCallbacks, selected: Boolean) {
    val (label, icon) = entry.labelAndIcon() ?: return
    DrawerRow(label = label, icon = icon, selected = selected, onClick = { callbacks.onEntryClick(entry) })
}

/** Whether this entry opens [key], the page shown beside the navigation. */
private fun DrawerEntry.opens(key: NavKey?): Boolean = key != null && this is DrawerEntry.Link && this.key == key

/** One drawer row, as the Toolkit's drawers draw it: animated icon, click sound, press bounce. */
@Composable
private fun DrawerRow(
    label: Int,
    icon: ToolkitIcon,
    onClick: () -> Unit,
    selectedIcon: ToolkitIcon = icon,
    selected: Boolean = false,
    badge: String? = null,
) {
    val feedback = rememberNavigationFeedback(haptic = false)
    var clicks by remember(label) { mutableIntStateOf(0) }
    NavigationDrawerItem(
        label = { Text(stringResource(label)) },
        selected = selected,
        onClick = {
            clicks++
            feedback()
            onClick()
        },
        icon = {
            AnimatedToolkitIcon(
                icon = icon,
                selectedIcon = selectedIcon,
                selected = selected,
                clickCount = clicks,
                contentDescription = null,
                modifier = Modifier.size(SizeConstants.TwentyFourSize),
            )
        },
        badge = badge?.let { text -> { Badge { Text(text) } } },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .bounceClick(),
    )
}

/**
 * The app's mark and name at the top of a drawer. The mark is sized to the title's line height so
 * the pair stays balanced as the person scales their font, and keeps its own colours.
 */
@Composable
private fun DrawerHeader(graph: ShellGraph) {
    val titleStyle = MaterialTheme.typography.titleLarge
    val logoSize = with(LocalDensity.current) { titleStyle.lineHeight.toDp() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .padding(horizontal = SizeConstants.MediumSize, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        graph.appIcon?.let { logo ->
            ToolkitIconContent(
                icon = logo,
                contentDescription = null,
                modifier = Modifier.size(logoSize),
                tint = Color.Unspecified,
            )
            Spacer(Modifier.width(SizeConstants.MediumSize))
        }
        Text(
            text = stringResource(graph.appTitle),
            style = titleStyle,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

/**
 * Lays out [top] and [footer] with the footer on the bottom edge while both fit, and scrolls the
 * two together once they do not.
 *
 * The column reaches the bottom of the window. The footer rests above the system navigation bar,
 * and once the rows scroll they pass behind it rather than stopping at its edge.
 */
@Composable
private fun PinnedFooterColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    spacing: Dp = 0.dp,
    top: @Composable ColumnScope.() -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = horizontalAlignment,
        ) {
            Column(
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = Arrangement.spacedBy(spacing),
                content = top,
            )
            Column(
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = Arrangement.spacedBy(spacing),
                content = footer,
            )
        }
    }
}

/**
 * The click sound, and with [haptic] a light haptic, that the Toolkit's navigation plays on every
 * click: the bars and rail both, the drawer the sound only.
 */
@Composable
private fun rememberNavigationFeedback(haptic: Boolean): () -> Unit {
    val view = LocalView.current
    val haptics = LocalHapticFeedback.current
    return remember(view, haptics, haptic) {
        {
            view.playSoundEffect(SoundEffectConstants.CLICK)
            if (haptic) haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
        }
    }
}

private fun DrawerEntry.labelAndIcon(): Pair<Int, ToolkitIcon>? = when (this) {
    is DrawerEntry.Link -> label to icon
    is DrawerEntry.Action -> label to icon
    DrawerEntry.Spacer -> null
}

internal val CollapsedRailWidth: Dp = 96.dp
private val MenuButtonSize: Dp = 48.dp
internal val ExpandedRailWidth: Dp = 280.dp
internal val PermanentDrawerWidth: Dp = 300.dp

/**
 * The app bar's overflow button and menu, listing the graph's overflow entries. The button slides
 * in and out with the tab roots it belongs to, and turns a quarter while the menu is open, as the
 * Toolkit's Support menu does.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun OverflowMenu(entries: List<DrawerEntry>, callbacks: NavigationCallbacks, visible: Boolean) {
    if (entries.isEmpty()) return
    var open by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (open) 90f else 0f, label = "OverflowRotation")
    val feedback = rememberNavigationFeedback(haptic = true)
    Box {
        AnimatedIconButtonDirection(
            modifier = Modifier.graphicsLayer { rotationZ = rotation },
            visible = visible,
            fromRight = true,
            icon = ToolkitIcon.Vector(Icons.Outlined.MoreVert),
            contentDescription = stringResource(CoreUiR.string.content_description_more_options),
            onClick = { open = true },
        )
        DropdownMenu(
            expanded = open && visible,
            onDismissRequest = { open = false },
            shape = MaterialTheme.shapes.largeIncreased,
        ) {
            entries.forEach { entry ->
                val (label, icon) = entry.labelAndIcon() ?: run {
                    HorizontalDivider()
                    return@forEach
                }
                DropdownMenuItem(
                    text = { Text(stringResource(label)) },
                    leadingIcon = {
                        AnimatedToolkitIcon(icon = icon, clickCount = 0, contentDescription = null)
                    },
                    onClick = {
                        feedback()
                        open = false
                        callbacks.onEntryClick(entry)
                    },
                )
            }
        }
    }
}
