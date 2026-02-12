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

package com.d4rk.android.libs.apptoolkit.app.about.ui.mapper

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * Extension function to map [AboutInfo] domain model to [AboutUiState] UI state.
 *
 * @return A new instance of [AboutUiState] containing the mapped application and device information.
 */
internal fun AboutInfo.toUiState(): AboutUiState =
    AboutUiState(
        appVersionInfo = AppVersionInfo(
            versionName = appVersion,
            versionCode = appVersionCode.toLong()
        ),
        deviceInfo = deviceInfo,
    )
