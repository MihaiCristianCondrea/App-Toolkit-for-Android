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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.navigation

import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.licenses.ui.LicensesScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.ShellGraphBuilder
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.LicensesRoute

/**
 * Registers the licenses page for [LicensesRoute], unless the app registered its own.
 *
 * It opens from About, itself a detail beside the settings list, so it is a page of its own
 * rather than a detail: a detail opened from a detail would replace it instead of stacking on it.
 */
fun ShellGraphBuilder.licensesPage() {
    pageIfAbsent<LicensesRoute>(
        paneRole = PaneRole.None,
        title = { stringResource(R.string.oss_license_title) },
    ) {
        LicensesScreen()
    }
}
