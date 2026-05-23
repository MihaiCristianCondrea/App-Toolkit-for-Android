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

package com.d4rk.android.libs.apptoolkit.core.ui.model

/**
 * Data class representing the version information of an application.
 *
 * @property versionName The user-visible version string (e.g., "1.0.0"). Can be null if not defined.
 * @property versionCode The internal version number used to determine whether one version is more recent than another.
 */
data class AppVersionInfo(
    val versionName: String?,
    val versionCode: Long,
)
