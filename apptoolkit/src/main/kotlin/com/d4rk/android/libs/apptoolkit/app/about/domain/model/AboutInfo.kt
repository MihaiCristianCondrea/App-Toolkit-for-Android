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

package com.d4rk.android.libs.apptoolkit.app.about.domain.model

/**
 * Data class representing basic application and device information.
 *
 * @property appVersion The human-readable version name of the application.
 * @property appVersionCode The internal version code of the application.
 * @property deviceInfo A string containing relevant hardware and software specifications of the device.
 */
data class AboutInfo(
    val appVersion: String,
    val appVersionCode: Int,
    val deviceInfo: String,
)
