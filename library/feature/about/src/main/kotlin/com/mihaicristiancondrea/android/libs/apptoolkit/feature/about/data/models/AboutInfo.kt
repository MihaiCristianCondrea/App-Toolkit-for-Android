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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models

/**
 * Application and device metadata backing the About screen.
 *
 * @property appVersion The host application version name.
 * @property appVersionCode The host application version code.
 * @property appToolkitVersion The App Toolkit publishing version, blank when unavailable.
 * @property googlePlayServicesVersion The installed Google Play services version, `null` when the
 * package is not present on the device.
 * @property deviceInfo The formatted device report supplied by the host.
 */
data class AboutInfo(
    val appVersion: String,
    val appVersionCode: Int,
    val appToolkitVersion: String,
    val googlePlayServicesVersion: String?,
    val deviceInfo: String,
)
