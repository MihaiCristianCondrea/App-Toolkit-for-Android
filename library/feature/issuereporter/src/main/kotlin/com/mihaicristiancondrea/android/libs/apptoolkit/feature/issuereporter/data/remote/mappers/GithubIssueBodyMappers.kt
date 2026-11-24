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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.constants.GithubIssueMarkdownConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.mappers.toDeviceInfoFields
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.Report

internal fun Report.toGithubIssueBody(): String = buildString {
    append(GithubIssueMarkdownConstants.DESCRIPTION_HEADING)
    append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
    append(description.trim())
    append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)

    append(GithubIssueMarkdownConstants.DEVICE_INFO_HEADING)
    append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
    append(GithubIssueMarkdownConstants.DETAILS_OPEN)
    append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
    append(formatDeviceInfoTable(deviceInfo = deviceInfo))
    append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
    append(GithubIssueMarkdownConstants.DETAILS_CLOSE)

    if (extraInfo.values.isNotEmpty()) {
        append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
        append(GithubIssueMarkdownConstants.EXTRA_INFO_HEADING)
        append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
        append(formatExtraInfoTable(extraInfo = extraInfo))
    }

    if (!email.isNullOrBlank()) {
        append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
        append(GithubIssueMarkdownConstants.HORIZONTAL_RULE)
        append(GithubIssueMarkdownConstants.PARAGRAPH_BREAK)
        append("*Reported by [$email](mailto:$email)*")
    }
}

/**
 * Device table for the GitHub issue body. Markdown remains readable in plain-text payloads and
 * notification emails. The report owns surrounding headings and details blocks.
 */
private fun formatDeviceInfoTable(deviceInfo: DeviceInfo): String =
    formatMarkdownTable(rows = deviceInfo.toDeviceInfoFields())

private fun formatExtraInfoTable(extraInfo: ExtraInfo): String {
    if (extraInfo.values.isEmpty()) return ""
    return formatMarkdownTable(rows = extraInfo.values.entries.map { (key, value) -> key to value })
}

private fun formatMarkdownTable(rows: List<Pair<String, String>>): String = buildString {
    append(GithubIssueMarkdownConstants.TABLE_HEADER)
    append(GithubIssueMarkdownConstants.TABLE_DIVIDER)
    rows.forEach { (label, value) ->
        append("| ${escapeTableCell(value = label)} | ${escapeTableCell(value = value)} |\n")
    }
}

/** Keeps pipes and line breaks inside a value from breaking the Markdown table row. */
private fun escapeTableCell(value: String): String = value
    .replace(oldValue = "|", newValue = "\\|")
    .replace(oldValue = "\r\n", newValue = "<br>")
    .replace(oldValue = "\n", newValue = "<br>")
    .replace(oldValue = "\r", newValue = "<br>")
