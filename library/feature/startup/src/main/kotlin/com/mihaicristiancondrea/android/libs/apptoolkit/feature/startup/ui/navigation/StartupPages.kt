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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.StartupScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.OnboardingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.StartupScreen

/**
 * Registers [StartupScreen], the first-launch start screen (consent and runtime permissions),
 * unless the app registered its own. Continuing hands over to [OnboardingScreen], which
 * `:library:feature:onboarding` registers; this module opens it by key and does not depend on it.
 *
 * There is no shell under it, so back leaves the app. The app opens it by starting on
 * [StartupScreen] until onboarding is done, from `ShellHost(resolveStart = ...)`:
 *
 * ```
 * resolveStart = { if (dataStore.startup.first()) StartupScreen else null }
 * ```
 *
 * It is also offered as a start screen in the developer options, to try it again.
 */
fun ShellGraphBuilder.startupScreen() {
    pageIfAbsent<StartupScreen>(paneRole = PaneRole.None, title = null) { StartupScreen() }
    startScreens(StartupScreen)
}
