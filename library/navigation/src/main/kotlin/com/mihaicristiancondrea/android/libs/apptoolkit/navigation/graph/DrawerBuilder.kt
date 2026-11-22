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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportScreen

import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR

class DrawerBuilder internal constructor() {
    internal val entries = mutableListOf<DrawerEntry>()
    internal val footer = mutableListOf<DrawerEntry>()

    /**
     * Entries that always close the drawer, after every other entry and pinned to its bottom edge,
     * such as the Toolkit's Settings, Help, Updates and Share. An entry the app adds later, even in
     * a later `drawer { }` call, still comes before them.
     */
    fun footer(builder: DrawerBuilder.() -> Unit) {
        footer += DrawerBuilder().apply(builder).let { it.entries + it.footer }
    }

    /**
     * An entry that opens [key], normally a page. It shows [badge], or [badgeText] read in
     * composition, as a tab does: a count, a label, or a dot for an empty string.
     */
    fun link(
        key: NavKey,
        @StringRes label: Int,
        icon: ToolkitIcon,
        @StringRes shortLabel: Int? = null,
        badge: String? = null,
        badgeText: (@Composable () -> String?)? = null,
    ) {
        entries += DrawerEntry.Link(key, label, icon, shortLabel, badge, badgeText)
    }

    /** An entry that runs [onClick] instead of navigating. */
    fun action(
        @StringRes label: Int,
        icon: ToolkitIcon,
        @StringRes shortLabel: Int? = null,
        onClick: (Context) -> Unit,
    ) {
        entries += DrawerEntry.Action(label, icon, onClick, shortLabel)
    }

    /** The Support page: rating, sharing and donations. The support feature registers it. */
    fun supportUs() {
        link(
            key = SupportScreen,
            label = R.string.shell_support_us,
            icon = ToolkitIcon.Vector(Icons.Outlined.VolunteerActivism),
            shortLabel = R.string.shell_support_short,
        )
    }

    /** Pins every entry after it to the bottom of the drawer. */
    fun spacer() {
        entries += DrawerEntry.Spacer
    }

    /**
     * The settings list, with the Toolkit's standard label and its animated settings icon. The
     * settings feature registers the page.
     */
    fun settings() {
        link(SettingsScreen, R.string.settings, ToolkitIcon.AnimatedVector(DesignSystemR.drawable.anim_settings))
    }
}
