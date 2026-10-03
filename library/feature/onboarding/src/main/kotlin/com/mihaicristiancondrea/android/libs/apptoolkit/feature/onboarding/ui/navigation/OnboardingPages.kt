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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.navigation

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.OnboardingScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.OnboardingRoute

/**
 * Registers [OnboardingRoute], the onboarding start screen, unless the app registered its own. It
 * follows `StartupRoute` from `:library:feature:startup` and enters the shell when the person
 * finishes it. There is no shell under it, so back leaves the app.
 *
 * It is also offered as a start screen in the developer options, to try it again.
 */
fun ShellGraphBuilder.onboardingPages() {
    pageIfAbsent<OnboardingRoute>(paneRole = PaneRole.None, title = null) { OnboardingScreen() }
    startScreens(OnboardingRoute)
}
