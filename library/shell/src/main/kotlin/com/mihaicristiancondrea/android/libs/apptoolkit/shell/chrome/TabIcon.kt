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

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellTab

/**
 * A tab's icon: [ShellTab.icon], or [ShellTab.selectedIcon] while selected, played on every click
 * that selects the tab when it animates, and pressed in with the Toolkit's bounce. Clicking the
 * tab already selected leaves it as it is.
 */
@Composable
internal fun TabIcon(tab: ShellTab, selected: Boolean, clicks: Int) {
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
    val badge = tab.currentBadge()
    if (badge != null) {
        BadgedBox(badge = { TabBadge(badge) }) { icon() }
    } else {
        icon()
    }
}

/** A count or label, or a dot for an empty one. */
@Composable
internal fun TabBadge(text: String) {
    if (text.isEmpty()) Badge() else Badge { Text(text) }
}
