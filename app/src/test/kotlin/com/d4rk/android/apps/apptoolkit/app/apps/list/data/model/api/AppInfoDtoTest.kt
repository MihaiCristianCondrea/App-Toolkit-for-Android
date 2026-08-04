/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api

import com.d4rk.android.apps.apptoolkit.app.apps.common.data.mapper.toDomain
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppDetailsDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppLinkDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppScreenshotDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.data.remote.model.AppSummaryDto
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppDeviceType
import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppInfoDtoTest {

    @Test
    fun `details mapper preserves valid screenshot metadata and links`() {
        val dto = AppDetailsDto(
            name = "App Toolkit",
            packageName = "com.d4rk.apptoolkit",
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
            packageName = "com.d4rk.apptoolkit",
            iconUrl = "not-an-http-url",
            shortDescription = "Tools",
        )

        val domain = dto.toDomain()

        assertEquals(PlayStoreUrls.DEFAULT_ICON_URL, domain.iconUrl)
        assertEquals("Tools", domain.shortDescription)
        assertTrue(domain.category == null)
    }
}
