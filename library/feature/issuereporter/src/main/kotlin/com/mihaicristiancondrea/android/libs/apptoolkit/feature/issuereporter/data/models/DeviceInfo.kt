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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models

/** Device and app details captured by the local data source and attached to a report. */
data class DeviceInfo(
    val appVersionName: String?,
    val appVersionCode: Long,
    val buildVersion: String,
    val releaseVersion: String,
    val sdkVersion: Int,
    val buildId: String,
    val brand: String,
    val manufacturer: String,
    val device: String,
    val model: String,
    val product: String,
    val hardware: String,
    /** Lists rather than arrays: this is a value type, and arrays compare by identity. */
    val abis: List<String>,
    val abis32Bit: List<String>,
    val abis64Bit: List<String>,
)
