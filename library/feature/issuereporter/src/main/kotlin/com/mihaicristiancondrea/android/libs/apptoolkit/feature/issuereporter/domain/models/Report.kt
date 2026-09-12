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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.mappers.toMarkdown
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.ExtraInfo

/**
 * A report as it will be filed: the title becomes the issue title, and [getDescription] renders the
 * issue body.
 *
 * The body is Markdown because that is what GitHub renders. The author's description is inserted
 * verbatim (since it is written in a Markdown editor, so their formatting is intentional) and everything
 * this class adds around it is structured so a maintainer can read the report without expanding
 * anything, and expand the device table only when it matters.
 */
class Report(
    val title: String,
    private val description: String,
    private val deviceInfo: DeviceInfo,
    private val extraInfo: ExtraInfo,
    private val email: String?
) {
    /** Renders the GitHub issue body. */
    fun getDescription(): String = buildString {
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

        if (!extraInfo.isEmpty()) {
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

    companion object {
        private const val PARAGRAPH_BREAK = "\n\n"
        private const val HORIZONTAL_RULE = "---"
        private const val DESCRIPTION_HEADING = "## Description"
        private const val DEVICE_INFO_HEADING = "## Device info"
        private const val EXTRA_INFO_HEADING = "## Extra info"
        private const val DETAILS_OPEN = "<details>\n<summary>Device and app details</summary>"
        private const val DETAILS_CLOSE = "</details>"
    }
}
