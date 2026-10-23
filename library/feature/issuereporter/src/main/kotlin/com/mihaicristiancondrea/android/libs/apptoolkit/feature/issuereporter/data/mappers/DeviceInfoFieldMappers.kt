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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.DeviceInfo

/**
 * Shares field labels and ordering between the Markdown and plain-text renderings.
 */
internal fun DeviceInfo.toDeviceInfoFields(): List<Pair<String, String>> = listOf(
    "App version" to appVersionName.toString(),
    "App version code" to appVersionCode.toString(),
    "Android build version" to buildVersion,
    "Android release version" to releaseVersion,
    "Android SDK version" to sdkVersion.toString(),
    "Android build ID" to buildId,
    "Device brand" to brand,
    "Device manufacturer" to manufacturer,
    "Device name" to device,
    "Device model" to model,
    "Device product name" to product,
    "Device hardware name" to hardware,
    "ABIs" to abis.toString(),
    "ABIs (32bit)" to abis32Bit.toString(),
    "ABIs (64bit)" to abis64Bit.toString(),
)
