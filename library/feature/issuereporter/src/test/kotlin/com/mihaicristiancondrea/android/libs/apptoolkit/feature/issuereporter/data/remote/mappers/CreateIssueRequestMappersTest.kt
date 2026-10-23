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

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.DeviceInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.ExtraInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.models.Report
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class CreateIssueRequestMappersTest {
    @Test
    fun reportDataBecomesTheGithubRequestWithoutChangingItsFormatting() {
        val request = report(email = "author@example.com").toCreateIssueRequest()
        val deviceTable = """
            | Item | Value |
            | --- | --- |
            | App version | 1.0 |
            | App version code | 1 |
            | Android build version | build |
            | Android release version | 16 |
            | Android SDK version | 36 |
            | Android build ID | id |
            | Device brand | brand |
            | Device manufacturer | manufacturer |
            | Device name | device |
            | Device model | model\|variant |
            | Device product name | product |
            | Device hardware name | hardware |
            | ABIs | [arm64-v8a] |
            | ABIs (32bit) | [] |
            | ABIs (64bit) | [arm64-v8a] |
        """.trimIndent() + "\n"
        val expected = "## Description\n\nDescription\n\n*Details*\n\n" +
            "## Device info\n\n<details>\n<summary>Device and app details</summary>\n\n" +
            deviceTable + "\n\n</details>\n\n---\n\n" +
            "*Reported by [author@example.com](mailto:author@example.com)*"

        assertEquals("Title", request.title)
        assertEquals(listOf("bug", "from-mobile"), request.labels)
        assertEquals(expected, request.body)
    }

    @Test
    fun emptyOptionalFieldsDoNotProduceSections() {
        val body = report(email = null).toCreateIssueRequest().body

        assertFalse(body.contains("## Extra info"))
        assertFalse(body.contains("Reported by"))
    }

    @Test
    fun extraFieldsEscapeTableSeparators() {
        val body = report(email = null)
            .copy(extraInfo = ExtraInfo(mapOf("Host|field" to "Value|detail")))
            .toCreateIssueRequest().body

        assertTrue(body.contains("| Host\\|field | Value\\|detail |\n"))
    }

    private fun report(email: String?): Report = Report(
        title = "Title",
        description = "  Description\n\n*Details*  ",
        deviceInfo = DeviceInfo(
            appVersionName = "1.0",
            appVersionCode = 1,
            buildVersion = "build",
            releaseVersion = "16",
            sdkVersion = 36,
            buildId = "id",
            brand = "brand",
            manufacturer = "manufacturer",
            device = "device",
            model = "model|variant",
            product = "product",
            hardware = "hardware",
            abis = listOf("arm64-v8a"),
            abis32Bit = emptyList(),
            abis64Bit = listOf("arm64-v8a"),
        ),
        extraInfo = ExtraInfo(),
        email = email,
    )
}
