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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.constants

internal object GithubIssueMarkdownConstants {
    const val PARAGRAPH_BREAK: String = "\n\n"
    const val HORIZONTAL_RULE: String = "---"
    const val DESCRIPTION_HEADING: String = "## Description"
    const val DEVICE_INFO_HEADING: String = "## Device info"
    const val EXTRA_INFO_HEADING: String = "## Extra info"
    const val DETAILS_OPEN: String = "<details>\n<summary>Device and app details</summary>"
    const val DETAILS_CLOSE: String = "</details>"
    const val TABLE_HEADER: String = "| Item | Value |\n"
    const val TABLE_DIVIDER: String = "| --- | --- |\n"
}
