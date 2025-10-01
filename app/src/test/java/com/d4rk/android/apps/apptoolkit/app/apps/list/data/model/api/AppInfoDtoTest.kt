package com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.api

import com.d4rk.android.apps.apptoolkit.app.apps.list.utils.constants.PlayStoreUrls
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class AppInfoDtoTest {

    @Test
    fun `toDomain trims urls and drops blank screenshots`() {
        val dto = AppInfoDto(
            name = "App Toolkit",
            packageName = "com.d4rk.apptoolkit",
            iconUrl = "  https://example.com/icon.png  ",
            description = "Test description",
            screenshots = listOf(
                " https://example.com/screenshot1.png ",
                "\t\n  ",
                "https://example.com/screenshot2.png"
            )
        )

        val domain = dto.toDomain()

        assertEquals("https://example.com/icon.png", domain.iconUrl)
        assertEquals(
            listOf(
                "https://example.com/screenshot1.png",
                "https://example.com/screenshot2.png"
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
            screenshots = listOf("  ")
        )

        val domain = dto.toDomain()

        assertEquals(PlayStoreUrls.DEFAULT_ICON_URL, domain.iconUrl)
        assertTrue(domain.screenshots.isEmpty())
    }
}
