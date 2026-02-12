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

package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github

class ExtraInfo {
    private val extraInfo: MutableMap<String, String> = LinkedHashMap()

    fun isEmpty(): Boolean = extraInfo.isEmpty()

    fun toMarkdown(): String {
        if (extraInfo.isEmpty()) return ""
        val output = StringBuilder()
        output.append(
            "Extra info:\n" +
                    "---\n" +
                    "<table>\n"
        )
        for (key in extraInfo.keys) {
            output.append("<tr><td>")
                .append(key)
                .append("</td><td>")
                .append(extraInfo[key])
                .append("</td></tr>\n")
        }
        output.append("</table>\n")
        return output.toString()
    }
}
