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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.mappers.toDeviceInfoFields
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.Report

internal fun Report.toGithubIssueBody(): String = buildString {
    append(DESCRIPTION_HEADING)
    append(PARAGRAPH_BREAK)
    append(description.trim())
    append(PARAGRAPH_BREAK)

    append(DEVICE_INFO_HEADING)
    append(PARAGRAPH_BREAK)
    append(DETAILS_OPEN)
    append(PARAGRAPH_BREAK)
    append(deviceInfo.toMarkdown())
    append(PARAGRAPH_BREAK)
    append(DETAILS_CLOSE)

    if (extraInfo.values.isNotEmpty()) {
        append(PARAGRAPH_BREAK)
        append(EXTRA_INFO_HEADING)
        append(PARAGRAPH_BREAK)
        append(extraInfo.toMarkdown())
    }

    if (!email.isNullOrBlank()) {
        append(PARAGRAPH_BREAK)
        append(HORIZONTAL_RULE)
        append(PARAGRAPH_BREAK)
        append("*Reported by [$email](mailto:$email)*")
    }
}

/**
 * Device table for the GitHub issue body. Markdown remains readable in plain-text payloads and
 * notification emails. The report owns surrounding headings and details blocks.
 */
private fun DeviceInfo.toMarkdown(): String = buildString {
    append("| Item | Value |\n")
    append("| --- | --- |\n")
    toDeviceInfoFields().forEach { (label, value) ->
        append("| ${label.escapeTableCell()} | ${value.escapeTableCell()} |\n")
    }
}

private fun ExtraInfo.toMarkdown(): String {
    if (values.isEmpty()) return ""
    return buildString {
        append("| Item | Value |\n")
        append("| --- | --- |\n")
        values.forEach { (key, value) ->
            append("| ${key.escapeTableCell()} | ${value.escapeTableCell()} |\n")
        }
    }
}

/** Keeps a value containing a pipe from splitting the row it is rendered in. */
private fun String.escapeTableCell(): String = replace(oldValue = "|", newValue = "\\|")

private const val PARAGRAPH_BREAK = "\n\n"
private const val HORIZONTAL_RULE = "---"
private const val DESCRIPTION_HEADING = "## Description"
private const val DEVICE_INFO_HEADING = "## Device info"
private const val EXTRA_INFO_HEADING = "## Extra info"
private const val DETAILS_OPEN = "<details>\n<summary>Device and app details</summary>"
private const val DETAILS_CLOSE = "</details>"
