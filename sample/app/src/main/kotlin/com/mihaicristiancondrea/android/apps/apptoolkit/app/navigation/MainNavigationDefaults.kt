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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.BottomBarItem
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR

object MainNavigationDefaults {
    val fabSupportedRoutes: ImmutableSet<StableNavKey> = persistentSetOf(
        AppsListRoute
    )

    val bottomBarItems: ImmutableList<BottomBarItem<StableNavKey>> = persistentListOf(
        BottomBarItem(
            route = ToolkitTilesRoute,
            animatedIcon = ToolkitIcon.AnimatedVector(
                resId = DesignSystemR.drawable.anim_grid_select,
                replayMode = ToolkitIconReplayMode.Reverse
            ),
            title = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R.string.tiles_title
        ),
        BottomBarItem(
            route = AppsListRoute,
            icon = ToolkitIcon.Vector(Icons.Outlined.Apps),
            // Every `Apps` variant bundled with Compose is squares; the selected state uses the
            // dot grid Material Symbols draws for this glyph.
            selectedIcon = ToolkitIcon.Resource(CoreUiR.drawable.ic_apps_dots),
            title = com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R.string.apps_tools_title
        )
    )
}
