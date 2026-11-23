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

import androidx.compose.foundation.layout.only
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellTab

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
        {
            Text(
                stringResource(tab.shortLabel ?: tab.label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    if (short) {
        ShortNavigationBar {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedIndex
                var clicks by remember(tab.key) { mutableIntStateOf(0) }
                ShortNavigationBarItem(
                    selected = selected,
                    onClick = {
                        // The tab already shown plays nothing: nothing changes.
                        if (!selected) clicks++
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
                        // The tab already shown plays nothing: nothing changes.
                        if (!selected) clicks++
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
