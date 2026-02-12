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

package com.d4rk.android.libs.apptoolkit.app.about.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo

/**
 * UI representation for the about screen.
 *
 * Values are loaded by [AboutViewModel] using the provided data sources and are
 * exposed as immutable properties to the UI layer.
 */
@Immutable
data class AboutUiState(
    val appVersionInfo: AppVersionInfo = AppVersionInfo(versionName = "", versionCode = 0L),
    val deviceInfo: String = "",
)
