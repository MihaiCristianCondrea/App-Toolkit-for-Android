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

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TabSearch
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.views.AboutSettingsContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.BottomAppBarNativeAdBanner
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.navigation.aboutPages
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.AppsListScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.navigation.AppsListRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.ComponentsScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.navigation.ComponentsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ToolkitTilesScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.navigation.ToolkitTilesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.toolkitFooter
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.toolkitGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.rememberWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R as AppsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R as ComponentsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R as TilesR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR

/** The launcher shortcut's action: it opens the settings page. */
const val ACTION_OPEN_SETTINGS: String = "com.d4rk.android.apps.apptoolkit.action.OPEN_SETTINGS"

/**
 * The sample's whole navigation: two tabs, the drawer, the overflow menu, the components page, the
 * bottom banner and the intents it answers. `toolkitGraph` adds the Toolkit's own pages (settings,
 * help, support, the first-launch start screens and the rest).
 *
 * This is the one place that knows the full feature set, which is why it stays in `:sample:app`:
 * each feature provides its screens, and the graph decides where they appear.
 *
 * @param showComponents Whether the drawer offers the components showcase: in debug builds, or once
 * it has been unlocked.
 * @param onShowChangelog Shows the changelog dialog, which the drawer's Updates entry opens.
 */
fun appGraph(
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
        // The app bar holds a search field for the tools; the screen filters by its query.
        search = TabSearch(hint = TilesR.string.tiles_search_hint),
    ) {
        ToolkitTilesScreen(paddingValues = contentPadding())
    }
    tab(
        key = AppsListRoute,
        label = AppsR.string.apps_tools_title,
        icon = ToolkitIcon.Vector(Icons.Outlined.Apps),
        // Every `Apps` variant bundled with Compose is squares; the selected state uses the dot
        // grid Material Symbols draws for this glyph.
        selectedIcon = ToolkitIcon.Resource(R.drawable.ic_apps_dots),
        search = TabSearch(hint = AppsR.string.apps_search_hint),
    ) {
        AppsListScreen(
            paddingValues = contentPadding(),
            windowWidthSizeClass = rememberWindowWidthSizeClass(),
        )
    }
    page<ComponentsRoute>(title = { stringResource(ComponentsR.string.components_title) }) {
        ComponentsScreen()
    }
    aboutPages { AboutSettingsContent() }
    // A native ad docked on the bottom navigation bar, and only there; nothing while ads are off.
    banner {
        val config: AdsConfig = koinInject(qualifier = named(AdsQualifiers.BOTTOM_NAV_BAR_NATIVE_AD))
        BottomAppBarNativeAdBanner(adUnitId = config.bannerAdUnitId)
    }

    drawer {
        if (showComponents) {
            link(
                key = ComponentsRoute,
                label = ComponentsR.string.components_title,
                icon = ToolkitIcon.Vector(Icons.Outlined.Widgets),
            )
        }
        toolkitFooter(onShowUpdates = onShowChangelog)
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
