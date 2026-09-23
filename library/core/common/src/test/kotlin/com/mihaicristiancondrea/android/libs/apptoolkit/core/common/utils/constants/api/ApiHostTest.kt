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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApiHostTest {

    @Test
    fun `ApiHost builds public metadata routes and encodes packages`() {
        val baseUrl = "https://example.com/"

        assertAll(
            {
                assertEquals(
                    "https://example.com/api/v1/apps",
                    ApiHost.appsUrl(baseUrl),
                )
            },
            {
                assertEquals(
                    "https://example.com/api/v1/apps/com.example%2Fapp%20name",
                    ApiHost.appDetailsUrl(
                        packageName = "com.example/app name",
                        baseUrl = baseUrl,
                    ),
                )
            },
            {
                assertEquals(
                    "https://example.com/api/v1/apps/com.example.app/changelog.md",
                    ApiHost.appChangelogUrl(
                        packageName = "com.example.app",
                        baseUrl = baseUrl,
                    ),
                )
            },
        )
        assertFailsWith<IllegalArgumentException> {
            ApiHost.appDetailsUrl(packageName = " ", baseUrl = baseUrl)
        }
    }
}
