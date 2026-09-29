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
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.navigation.aboutPages
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.navigation.advancedSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.navigation.developerOptionsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.navigation.diagnosticsSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.navigation.displaySettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.navigation.helpPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.navigation.licensesPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.navigation.onboardingPages
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.navigation.permissionsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.navigation.privacySettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.navigation.settingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.ui.navigation.supportPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.navigation.themeSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.navigation.adsSettingsPage
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraph
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder

/**
 * Builds a [ShellGraph] with the Toolkit's own pages already in it: the settings list and its
 * pages, about, licenses, help, support, permissions, ads settings, developer options and the
 * first-launch start screens.
 *
 * The app's [builder] runs first, so a page it registers for one of the Toolkit's keys replaces
 * the Toolkit's page, and a feature's registration function it calls itself, such as
 * `aboutPages { ... }`, takes its arguments. The app still decides where each page is offered: in
 * the drawer, the overflow menu or from a screen.
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
 * Registers the Toolkit's pages the app has not registered itself, each through its own feature's
 * registration function. [toolkitGraph] calls it; an app that builds its graph with
 * `ShellGraphBuilder` directly calls it after its own destinations.
 *
 * This is the only place that names every feature; the features know nothing of each other and
 * open one another's pages by key.
 */
fun ShellGraphBuilder.toolkitPages() {
    settingsPage()
    displaySettingsPage()
    themeSettingsPage()
    privacySettingsPage()
    diagnosticsSettingsPage()
    permissionsPage()
    adsSettingsPage()
    advancedSettingsPage()
    aboutPages()
    licensesPage()
    helpPage()
    supportPage()
    developerOptionsPage()
    onboardingPages()
}
