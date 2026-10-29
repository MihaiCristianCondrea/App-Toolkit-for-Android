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
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R as AppsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.AppsListScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R as ComponentsR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.ArticleDemoScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.ComponentsScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.AboutSettingsScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R as TilesR
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.PlayScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.NumbersScreen
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.ToolkitTilesScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.toolkitFooter
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.navigation.toolkitGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ads.AdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.R as DesignSystemR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.BottomAppBarNativeAdBanner
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.navigation.aboutPages
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TabSearch
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.TopBarStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

/** The launcher shortcut's action: it opens the settings page. */
const val ACTION_OPEN_SETTINGS: String = "com.d4rk.android.apps.apptoolkit.action.OPEN_SETTINGS"

/**
 * The sample's whole navigation: three tabs, the drawer, the overflow menu, the components page and
 * its article bar demo, the optional bottom banner and the intents it answers. The demo registers a
 * small bar, the one an article draws anyway, so it does not open large for its first frame.
 * `toolkitGraph` adds the Toolkit's own pages (settings, help, support, the first-launch start
 * screens and the rest).
 *
 * Routes and the graph belong to `:sample:app` under app/navigation. Features expose callbacks
 * rather than importing app route keys.
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
    defaults = AppDefaults.navigation,
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
        ToolkitTilesScreen()
    }
    tab(
        key = NumbersRoute,
        label = TilesR.string.measure_title,
        search = TabSearch(hint = TilesR.string.tiles_search_hint),
        icon = ToolkitIcon.Vector(Icons.Outlined.Straighten),
        selectedIcon = ToolkitIcon.Vector(Icons.Filled.Straighten),
    ) {
        NumbersScreen()
    }
    tab(
        key = PlayRoute,
        label = TilesR.string.play_title,
        icon = ToolkitIcon.Vector(Icons.Outlined.Casino),
        selectedIcon = ToolkitIcon.Vector(Icons.Filled.Casino),
        search = TabSearch(hint = TilesR.string.tiles_search_hint),
    ) {
        PlayScreen()
    }
    page<AppsListRoute>(title = { stringResource(TilesR.string.discover_apps) }) { AppsListScreen() }
    page<ExtraToolsRoute>(title = { stringResource(TilesR.string.extra_tools) }) { ToolkitTilesScreen(extraTools = true) }
    page<ComponentsRoute>(title = { stringResource(ComponentsR.string.components_title) }) {
        val navigator = com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellNavigator.current
        ComponentsScreen(onOpenArticleDemo = { navigator.navigate(ArticleDemoRoute(it)) })
    }
    page<ArticleDemoRoute>(
        topBar = TopBarStyle.Small,
        title = { stringResource(ComponentsR.string.components_section_article_bar) },
    ) { key ->
        ArticleDemoScreen(branded = key.branded)
    }
    aboutPages { AboutSettingsScreen() }
    banner {
        val config: AdsConfig = koinInject(qualifier = named(AdsQualifiers.BOTTOM_NAV_BAR_NATIVE_AD))
        BottomAppBarNativeAdBanner(
            adUnitId = config.bannerAdUnitId,
            // Retained for host integration; keep it off near navigation (accidental-click risk).
            enabled = false,
        )
    }

    drawer {
        link(key = AppsListRoute, label = TilesR.string.discover_apps, icon = ToolkitIcon.Vector(Icons.Outlined.Apps))
        link(key = ExtraToolsRoute, label = TilesR.string.extra_tools, icon = ToolkitIcon.Vector(Icons.Outlined.Widgets))
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
 * the tab's [AppsListRoute.ROUTE_ID], [NumbersRoute.ROUTE_ID] or [ToolkitTilesRoute.ROUTE_ID].
 */
fun startKeyFor(route: String): NavKey = when (route) {
    PlayRoute.ROUTE_ID -> PlayRoute
    NumbersRoute.ROUTE_ID -> NumbersRoute
    AppsListRoute.ROUTE_ID -> ToolkitTilesRoute
    else -> ToolkitTilesRoute
}
