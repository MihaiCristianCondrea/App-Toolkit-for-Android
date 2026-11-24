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

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph

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
                        badge = tab.currentBadge(),
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

@Composable
private fun DrawerEntryRow(entry: DrawerEntry, callbacks: NavigationCallbacks, selected: Boolean) {
    val (label, icon) = entry.labelAndIcon() ?: return
    DrawerRow(
        label = label,
        icon = icon,
        selected = selected,
        badge = entry.currentBadge(),
        onClick = { callbacks.onEntryClick(entry) })
}

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
            if (!selected) clicks++
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
        badge = badge?.let { text -> { TabBadge(text) } },
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

internal val PermanentDrawerWidth: Dp = 300.dp

/** A drawer keeps clear of the top and start; its rows pad the bottom themselves, to scroll behind it. */
internal val DrawerContentInsets: WindowInsets
    @Composable get() = WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Top)
