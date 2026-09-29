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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalContentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.views.extras.LibraryExtrasScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.DeveloperOptionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.FaqScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.LicensesScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.PermissionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.SettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.general.GeneralSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.views.dropdowns.SettingsMenuActions
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.SupportScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.AdsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdsSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DeveloperOptionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.GeneralSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.HelpRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PermissionsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.SupportRoute
import org.koin.compose.koinInject
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R as DeveloperR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R as FaqR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.R as LicensesR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R as PermissionsR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R as SettingsR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.R as SupportR
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.R as AdsR

/**
 * Builds a [ShellGraph] with the Toolkit's own pages already in it: settings, help, support,
 * licenses, permissions, ads settings and developer options.
 *
 * The app's [builder] runs first, so a page it registers for one of the Toolkit's keys replaces
 * the Toolkit's page. The app still decides where each page is offered: in the drawer, the overflow
 * menu or from a screen.
 *
 * ```
 * val graph = toolkitGraph(appTitle = R.string.app_name) {
 *     tab(HomeRoute, R.string.home, ToolkitIcon.Vector(Icons.Outlined.Home)) { HomeScreen() }
 *     drawer { settings(); link(HelpRoute, R.string.help, ToolkitIcon.Vector(Icons.Outlined.Help)) }
 *     overflow { supportUs() }
 * }
 * ```
 */
fun toolkitGraph(
    @StringRes appTitle: Int,
    appIcon: ToolkitIcon? = null,
    builder: ShellGraphBuilder.() -> Unit,
): ShellGraph = ShellGraphBuilder(appTitle, appIcon).apply {
    builder()
    toolkitPages()
}.build()

/**
 * Registers the Toolkit's pages the app has not registered itself. [toolkitGraph] calls it; an app
 * that builds its graph with `ShellGraphBuilder` directly calls it after its own destinations.
 */
fun ShellGraphBuilder.toolkitPages() {
    pageIfAbsent<SettingsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(SettingsR.string.settings) },
        actions = { SettingsMenuActions() },
    ) {
        EmbeddedPage { SettingsScreen(isEmbedded = true) }
    }
    pageIfAbsent<GeneralSettingsRoute>(paneRole = PaneRole.None, title = { it.title }) { route ->
        EmbeddedPage {
            GeneralSettingsScreen(
                title = route.title,
                contentKey = route.contentKey,
                onBackClicked = {},
                isEmbedded = true,
            )
        }
    }
    pageIfAbsent<HelpRoute>(paneRole = PaneRole.None, title = { stringResource(FaqR.string.help) }) {
        val config: AppVersionInfo = koinInject()
        EmbeddedPage { FaqScreen(config = config, isEmbedded = true) }
    }
    pageIfAbsent<SupportRoute>(paneRole = PaneRole.None, title = { stringResource(SupportR.string.support_us) }) {
        EmbeddedPage { SupportScreen(isEmbedded = true) }
    }
    pageIfAbsent<AdsSettingsRoute>(paneRole = PaneRole.None, title = { stringResource(AdsR.string.ads) }) {
        EmbeddedPage { AdsSettingsScreen(isEmbedded = true) }
    }
    pageIfAbsent<PermissionsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(PermissionsR.string.permissions) },
    ) {
        EmbeddedPage { PermissionsScreen(isEmbedded = true) }
    }
    pageIfAbsent<LicensesRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(LicensesR.string.oss_license_title) },
    ) {
        EmbeddedPage { LicensesScreen(isEmbedded = true) }
    }
    pageIfAbsent<LibraryExtrasRoute>(paneRole = PaneRole.None, title = { stringResource(CommonR.string.app_name) }) {
        LibraryExtrasScreen(paddingValues = contentPadding())
    }
    pageIfAbsent<DeveloperOptionsRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(DeveloperR.string.shell_developer_options) },
    ) {
        DeveloperOptionsScreen()
    }
}

/**
 * Pads a screen that still has an embedded mode by the shell's bars, which its own padding does
 * not account for. Each feature drops it when its screen reads [LocalContentPadding] itself.
 */
@Composable
private fun EmbeddedPage(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(LocalContentPadding.current)) {
        content()
    }
}
