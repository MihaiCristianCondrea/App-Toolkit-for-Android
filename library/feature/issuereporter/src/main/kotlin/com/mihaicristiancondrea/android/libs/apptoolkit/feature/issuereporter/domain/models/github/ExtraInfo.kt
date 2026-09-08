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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github

/** Host-supplied key/value pairs appended to a report. */
class ExtraInfo {
    private val extraInfo: MutableMap<String, String> = LinkedHashMap()

    fun isEmpty(): Boolean = extraInfo.isEmpty()

    /** The Markdown table for these pairs, or an empty string when there are none. */
    fun toMarkdown(): String {
        if (extraInfo.isEmpty()) return ""
        return buildString {
            append("| Item | Value |\n")
            append("| --- | --- |\n")
            extraInfo.forEach { (key, value) ->
                append("| ${key.escapeTableCell()} | ${value.escapeTableCell()} |\n")
            }
        }
    }

    private fun String.escapeTableCell(): String = replace(oldValue = "|", newValue = "\\|")
}

