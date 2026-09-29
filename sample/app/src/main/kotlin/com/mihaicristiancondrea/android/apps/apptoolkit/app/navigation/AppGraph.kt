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
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.DeveloperMode
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.AppsListScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.fab.RandomAppAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.fab.RandomAppFloatingActionButton
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.ComponentsScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ToolkitTilesScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.toolkitGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.shareApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R as AppsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R as ComponentsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R as TilesR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R as AboutR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R as DeveloperR

/** The launcher shortcut's action: it opens the settings page. */
const val ACTION_OPEN_SETTINGS: String = "com.d4rk.android.apps.apptoolkit.action.OPEN_SETTINGS"

/**
 * The sample's whole navigation: two tabs, the drawer, the overflow menu, the components page and
 * the intents it answers. `toolkitGraph` adds the Toolkit's own pages (settings, help, support and
 * the rest).
 *
 * This is the one place that knows the full feature set, which is why it stays in `:sample:app`:
 * each feature provides its screens, and the graph decides where they appear.
 *
 * @param randomApp What the Apps tab's button does, registered by the list.
 * @param showComponents Whether the drawer offers the components showcase: in debug builds, or once
 * it has been unlocked.
 * @param onShowChangelog Shows the changelog dialog, which the drawer's Updates entry opens.
 */
fun appGraph(
    randomApp: RandomAppAction,
    showComponents: Boolean,
    onShowChangelog: () -> Unit,
): ShellGraph = toolkitGraph(
    appTitle = R.string.app_name,
    appIcon = ToolkitIcon.Resource(R.drawable.app_logo),
) {
    tab(
        key = ToolkitTilesRoute,
        label = TilesR.string.tiles_title,
        icon = ToolkitIcon.AnimatedVector(
            resId = DesignSystemR.drawable.anim_grid_select,
            replayMode = ToolkitIconReplayMode.Reverse,
        ),
    ) {
        ToolkitTilesScreen(paddingValues = contentPadding())
    }
    tab(
        key = AppsListRoute,
        label = AppsR.string.apps_tools_title,
        icon = ToolkitIcon.Vector(Icons.Outlined.Apps),
        // Every `Apps` variant bundled with Compose is squares; the selected state uses the dot
        // grid Material Symbols draws for this glyph.
        selectedIcon = ToolkitIcon.Resource(CoreUiR.drawable.ic_apps_dots),
        fab = { RandomAppFloatingActionButton(action = randomApp) },
    ) {
        AppsListScreen(
            paddingValues = contentPadding(),
            windowWidthSizeClass = rememberWindowWidthSizeClass(),
            onRegisterRandomAppHandler = { handler -> randomApp.handler = handler },
        )
    }
    page<ComponentsRoute>(title = { stringResource(ComponentsR.string.components_title) }) {
        ComponentsScreen(paddingValues = contentPadding())
    }

    drawer {
        if (showComponents) {
            link(
                key = ComponentsRoute,
                label = ComponentsR.string.components_title,
                icon = ToolkitIcon.Vector(Icons.Outlined.Widgets),
            )
        }
        settings()
        link(
            key = HelpRoute,
            label = AboutR.string.help_and_feedback,
            icon = ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.HelpOutline),
        )
        action(
            label = AboutR.string.updates,
            icon = ToolkitIcon.Vector(Icons.AutoMirrored.Outlined.EventNote),
        ) {
            onShowChangelog()
        }
        action(
            label = AboutR.string.share,
            icon = ToolkitIcon.AnimatedVector(DesignSystemR.drawable.anim_share),
        ) { context ->
            context.shareApp(shareMessageFormat = AboutR.string.summary_share_message)
        }
        if (BuildConfig.DEBUG) {
            spacer()
            link(
                key = DeveloperOptionsRoute,
                label = DeveloperR.string.shell_developer_options,
                icon = ToolkitIcon.Vector(Icons.Outlined.DeveloperMode),
            )
        }
    }
    overflow { supportUs() }
    deepLinks { action(ACTION_OPEN_SETTINGS) { SettingsRoute } }
}

/**
 * The tab a launch opens on, from the startup page chosen in the display settings, which stores
 * the tab's [AppsListRoute.ROUTE_ID] or [ToolkitTilesRoute.ROUTE_ID].
 */
fun startKeyFor(route: String): NavKey = when (route) {
    AppsListRoute.ROUTE_ID -> AppsListRoute
    else -> ToolkitTilesRoute
}
