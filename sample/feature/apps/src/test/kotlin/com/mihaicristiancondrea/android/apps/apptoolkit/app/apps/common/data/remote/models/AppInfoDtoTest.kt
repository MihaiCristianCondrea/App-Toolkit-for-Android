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

/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.mappers.toDomain
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDeviceType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AppInfoDtoTest {

    @Test
    fun `details mapper preserves valid screenshot metadata and links`() {
        val dto = AppDetailsDto(
            name = "App Toolkit",
            packageName = "com.mihaicristiancondrea.apptoolkit",
            iconUrl = "  https://example.com/icon.png  ",
            description = "Test description",
            screenshots = listOf(
                AppScreenshotDto(
                    url = " https://example.com/phone.png ",
                    aspectRatio = "9:16",
                    deviceType = "phone",
                ),
                AppScreenshotDto(
                    url = " ",
                    aspectRatio = "9:16",
                    deviceType = "phone",
                ),
                AppScreenshotDto(
                    url = "https://example.com/tablet.png",
                    aspectRatio = "16:9",
                    deviceType = "tablet",
                ),
            ),
            links = listOf(
                AppLinkDto(label = " Play Store ", url = " https://example.com/store "),
                AppLinkDto(label = " ", url = "https://example.com/ignored"),
            ),
        )

        val domain = dto.toDomain()

        assertEquals("https://example.com/icon.png", domain.iconUrl)
        assertEquals(
            listOf("https://example.com/phone.png", "https://example.com/tablet.png"),
            domain.screenshots.map { it.url },
        )
        assertEquals(
            listOf(AppDeviceType.Phone, AppDeviceType.Tablet),
            domain.screenshots.map { it.deviceType },
        )
        assertEquals(listOf("Play Store"), domain.links.map { it.label })
    }

    @Test
    fun `summary mapper uses default icon for an invalid URL`() {
        val dto = AppSummaryDto(
            name = "App Toolkit",
            packageName = "com.mihaicristiancondrea.apptoolkit",
            iconUrl = "not-an-http-url",
            shortDescription = "Tools",
        )

        val domain = dto.toDomain()

        assertEquals("https://c.clc2l.com/t/g/o/google-playstore-Iauj7q.png", domain.iconUrl)
        assertEquals("Tools", domain.shortDescription)
        assertEquals(domain.category, null)
    }
}
