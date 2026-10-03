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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.views.extras.LibraryExtrasScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AboutRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LibraryExtrasRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * Registers the About page, a detail of the settings list, and the library extras page, unless
 * the app registered its own.
 *
 * An app that wants more from About, such as reacting to taps on the version, calls this itself
 * before the Toolkit's pages are added, with its own [about] content:
 *
 * ```
 * aboutPages { AboutScreen(onVersionTap = { taps -> unlockAfter(taps) }) }
 * ```
 */
fun ShellGraphBuilder.aboutPages(about: @Composable () -> Unit = { AboutScreen() }) {
    pageIfAbsent<AboutRoute>(
        paneRole = PaneRole.Detail,
        title = { stringResource(CoreUiR.string.about) },
    ) {
        about()
    }
    pageIfAbsent<LibraryExtrasRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(CommonR.string.app_name) },
    ) {
        LibraryExtrasScreen()
    }
}
