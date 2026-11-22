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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.navigation.aboutScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.navigation.advancedSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.navigation.developerOptionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.navigation.diagnosticsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.navigation.displaySettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.navigation.helpScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.navigation.licensesScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.navigation.onboardingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.navigation.startupScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.navigation.permissionsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.navigation.privacySettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.navigation.settingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.navigation.supportScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.navigation.themeSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.navigation.adsSettingsScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.NavigationDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder

/**
 * Builds a [ShellGraph] with the Toolkit's own pages already in it: the settings list and its
 * pages, about, licenses, help, support, permissions, ads settings, developer options and the
 * first-launch start screens.
 *
 * The app's [builder] runs first, so a page it registers for one of the Toolkit's keys replaces
 * the Toolkit's page, and a feature's registration function it calls itself, such as
 * `aboutScreen { ... }`, takes its arguments. The app still decides where each page is offered: in
 * the drawer, the overflow menu or from a screen.
 *
 * [defaults] set the app bar of every destination that names none, the Toolkit's pages included,
 * and the layout policy `ShellHost` uses; see `appToolkitDefaults { }`.
 *
 * ```
 * val graph = toolkitGraph(appTitle = R.string.app_name) {
 *     tab(HomeScreen, R.string.home, ToolkitIcon.Vector(Icons.Outlined.Home)) { HomeScreen() }
 *     drawer { settings(); link(HelpScreen, R.string.help, ToolkitIcon.Vector(Icons.Outlined.Help)) }
 *     overflow { supportUs() }
 * }
 * ```
 */
fun toolkitGraph(
    @StringRes appTitle: Int,
    appIcon: ToolkitIcon? = null,
    defaults: NavigationDefaults = NavigationDefaults(),
    builder: ShellGraphBuilder.() -> Unit,
): ShellGraph = ShellGraphBuilder(appTitle, appIcon, defaults).apply {
    builder()
    toolkitScreens()
}.build()

/**
 * Registers the Toolkit's pages the app has not registered itself, each through its own feature's
 * registration function. [toolkitGraph] calls it; an app that builds its graph with
 * `ShellGraphBuilder` directly calls it after its own destinations.
 *
 * This is the only place that names every feature; the features know nothing of each other and
 * open one another's pages by key.
 */
fun ShellGraphBuilder.toolkitScreens() {
    settingsScreen()
    displaySettingsScreen()
    themeSettingsScreen()
    privacySettingsScreen()
    diagnosticsSettingsScreen()
    permissionsScreen()
    adsSettingsScreen()
    advancedSettingsScreen()
    aboutScreen()
    licensesScreen()
    helpScreen()
    supportScreen()
    developerOptionsScreen()
    startupScreen()
    onboardingScreen()
}
