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

package com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.mapper.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppInfoDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppScreenshotDto
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppInfoDtoTest {

    @Test
    fun `toDomain trims urls and drops blank screenshots`() {
        val dto = AppInfoDto(
            name = "App Toolkit",
            packageName = "com.d4rk.apptoolkit",
            iconUrl = "  https://example.com/icon.png  ",
            description = "Test description",
            screenshots = listOf(
                AppScreenshotDto(
                    url = " https://example.com/screenshot1.png ",
                    aspectRatio = "9:16"
                ),
                AppScreenshotDto(
                    url = "\t\n  ",
                    aspectRatio = "9:16"
                ),
                AppScreenshotDto(
                    url = "https://example.com/screenshot2.png",
                    aspectRatio = "16:9"
                ),
                AppScreenshotDto(
                    url = " https://example.com/screenshot3.png ",
                    aspectRatio = " 9:16 "
                )
            )
        )

        val domain = dto.toDomain()

        assertEquals("https://example.com/icon.png", domain.iconUrl)
        assertEquals(
            listOf(
                "https://example.com/screenshot1.png",
                "https://example.com/screenshot3.png"
            ),
            domain.screenshots
        )
    }

    @Test
    fun `toDomain falls back to default icon when sanitized url is blank`() {
        val dto = AppInfoDto(
            name = "App Toolkit",
            packageName = "com.d4rk.apptoolkit",
            iconUrl = "   ",
            description = null,
            screenshots = listOf(AppScreenshotDto(url = "  ", aspectRatio = "9:16"))
        )

        val domain = dto.toDomain()

        assertEquals(PlayStoreUrls.DEFAULT_ICON_URL, domain.iconUrl)
        assertTrue(domain.screenshots.isEmpty())
    }
}
