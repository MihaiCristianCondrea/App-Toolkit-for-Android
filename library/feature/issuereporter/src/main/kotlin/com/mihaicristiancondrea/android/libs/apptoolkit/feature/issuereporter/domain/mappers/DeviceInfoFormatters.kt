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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.DeviceInfo

/**
 * Renders [DeviceInfo] for the two places a report shows it.
 *
 * These were `toMarkdown()` and an overridden `toString()` on the model itself. Making the model a
 * data class would have replaced that `toString()` with the generated one, so the device panel would
 * have started rendering `DeviceInfo(appVersionName=…)`, the formatting has to move out with it,
 * not just disappear.
 */

/**
 * The Markdown table embedded in the GitHub issue body.
 *
 * A Markdown table rather than the HTML one this used to emit: GitHub renders both, but only the
 * Markdown form stays readable in the plain issue body, in notification emails, and in the API
 * payload. Headings and the surrounding `<details>` belong to the report, not here.
 */
fun DeviceInfo.toMarkdown(): String = buildString {
    append("| Item | Value |\n")
    append("| --- | --- |\n")
    rows().forEach { (label, value) ->
        append("| ${label.escapeTableCell()} | ${value.escapeTableCell()} |\n")
    }
}

/** The plain listing shown in the collapsible device-info panel on the report screen. */
fun DeviceInfo.toPlainText(): String =
    rows().joinToString(separator = "\n") { (label, value) -> "$label: $value" }

/**
 * Single source for the field order and labels, so the two renderings cannot drift apart, they
 * previously repeated the same fifteen fields in two hand-maintained lists.
 */
private fun DeviceInfo.rows(): List<Pair<String, String>> = listOf(
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

/** Keeps a value containing a pipe from splitting the row it is rendered in. */
private fun String.escapeTableCell(): String = replace(oldValue = "|", newValue = "\\|")
