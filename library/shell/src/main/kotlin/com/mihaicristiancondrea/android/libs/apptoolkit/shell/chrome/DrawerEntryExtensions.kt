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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry

/** The drawer's entries before its [DrawerEntry.Spacer], and the pinned footer after it. */
internal fun List<DrawerEntry>.splitAtSpacer(): Pair<List<DrawerEntry>, List<DrawerEntry>> {
    val spacer = indexOf(DrawerEntry.Spacer)
    return if (spacer < 0) this to emptyList() else take(spacer) to drop(spacer + 1).filterNot { it == DrawerEntry.Spacer }
}

internal fun DrawerEntry.labelAndIcon(): Pair<Int, ToolkitIcon>? = when (this) {
    is DrawerEntry.Link -> label to icon
    is DrawerEntry.Action -> label to icon
    DrawerEntry.Spacer -> null
}

/** A link's badge now: its [DrawerEntry.Link.badgeText] read in composition, or its fixed badge. */
@Composable
internal fun DrawerEntry.currentBadge(): String? =
    (this as? DrawerEntry.Link)?.let { link -> link.badgeText?.invoke() ?: link.badge }

/** Whether this entry opens [key], the page shown beside the navigation. */
internal fun DrawerEntry.opens(key: NavKey?): Boolean =
    key != null && this is DrawerEntry.Link && this.key == key
