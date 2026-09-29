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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.shareApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR

/**
 * The entries every Toolkit app ends its drawer with, in this order: Settings, Help and feedback,
 * Updates and Share. They go in the drawer's footer, so they stay at the bottom edge after
 * anything the app lists, whatever order it lists it in.
 *
 * ```
 * drawer {
 *     link(LibraryRoute, R.string.library, ToolkitIcon.Vector(Icons.Outlined.VideoLibrary))
 *     toolkitFooter(onShowUpdates = { showChangelog = true })
 * }
 * ```
 *
 * @param onShowUpdates Shows what is new, typically the changelog dialog of
 * `:library:feature:changelog`. Null leaves Updates out.
 */
fun DrawerBuilder.toolkitFooter(onShowUpdates: (() -> Unit)?) {
    footer {
        settings()
        link(
            key = HelpRoute,
            label = AboutR.string.help_and_feedback,
            icon = ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
        )
        if (onShowUpdates != null) {
            action(label = AboutR.string.updates, icon = ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.EventNote)) {
                onShowUpdates()
            }
        }
        action(label = AboutR.string.share, icon = ToolkitIcon.AnimatedVector(DesignSystemR.drawable.anim_share)) { context ->
            context.shareApp(shareMessageFormat = AboutR.string.summary_share_message)
        }
    }
}
