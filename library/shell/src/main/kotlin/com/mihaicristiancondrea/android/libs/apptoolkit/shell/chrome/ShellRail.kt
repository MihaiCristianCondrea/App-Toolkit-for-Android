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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.shell.R

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
    val width by animateDpAsState(
        if (expanded) ExpandedRailWidth else CollapsedRailWidth,
        label = "RailWidth"
    )
    // How far the rail is between collapsed (0) and expanded (1), following its animated width.
    // Alignment and padding move with it; switching them at once made the menu button jump to
    // the middle of the still-wide rail and then slide back as the width caught up.
    val openness =
        ((width - CollapsedRailWidth) / (ExpandedRailWidth - CollapsedRailWidth)).coerceIn(0f, 1f)
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
                    .padding(start = (CollapsedRailWidth - MaterialTheme.size.extraExtraLarge) / 2),
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
                        modifier = Modifier.padding(
                            start = MaterialTheme.spacing.small,
                            end = MaterialTheme.spacing.large,
                        ),
                    )
                }
            }
            val itemModifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium * openness)
            PinnedFooterColumn(
                horizontalAlignment = alignment,
                spacing = MaterialTheme.spacing.extraSmall * (1f - openness),
                top = {
                    graph.tabs.forEachIndexed { index, tab ->
                        val selected = index == selectedIndex
                        var clicks by remember(tab.key) { mutableIntStateOf(0) }
                        WideNavigationRailItem(
                            selected = selected,
                            onClick = {
                                if (!selected) clicks++
                                feedback()
                                callbacks.onTabClick(index)
                            },
                            icon = { TabIcon(tab, selected, clicks) },
                            label = {
                                RailLabel(
                                    if (expanded) tab.label else tab.shortLabel ?: tab.label
                                )
                            },
                            railExpanded = expanded,
                            modifier = itemModifier,
                        )
                    }
                    if (top.isNotEmpty()) RailDivider(expanded)
                    top.forEach { entry ->
                        RailEntry(
                            entry,
                            expanded,
                            itemModifier,
                            callbacks,
                            entry.opens(selectedEntry)
                        )
                    }
                },
                footer = {
                    footer.forEach { entry ->
                        RailEntry(
                            entry,
                            expanded,
                            itemModifier,
                            callbacks,
                            entry.opens(selectedEntry)
                        )
                    }
                    SmallVerticalSpacer()
                },
            )
        }
    }
}

@Composable
private fun RailLabel(label: Int) {
    Text(stringResource(label), maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun RailDivider(expanded: Boolean) {
    HorizontalDivider(
        Modifier.padding(
            horizontal = if (expanded) MaterialTheme.spacing.extraLarge else MaterialTheme.spacing.extraLargeCompact,
            vertical = MaterialTheme.spacing.small,
        )
    )
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
            if (!selected) clicks++
            feedback()
            callbacks.onEntryClick(entry)
        },
        icon = {
            val railIcon: @Composable () -> Unit = {
                AnimatedToolkitIcon(
                    icon = icon,
                    clickCount = clicks,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = MaterialTheme.size.medium)
                        .bounceClick(),
                )
            }
            val badge = entry.currentBadge()
            if (badge != null) BadgedBox(badge = { TabBadge(badge) }) { railIcon() } else railIcon()
        },
        label = { RailLabel(label) },
        railExpanded = expanded,
        modifier = modifier,
    )
}

internal val CollapsedRailWidth: Dp = 96.dp
internal val ExpandedRailWidth: Dp = 280.dp
