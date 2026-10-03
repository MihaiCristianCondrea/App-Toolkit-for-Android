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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.utils.constants.SettingsConstants

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AboutRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdvancedSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openAppNotificationSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNull
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R as SettingsR

class AppSettingsProviderTest {

    private val defaultStrings = mapOf(
        SettingsR.string.settings to "Settings",
        SettingsR.string.notifications to "Notifications",
        SettingsR.string.summary_preference_settings_notifications to "Manage app notifications",
        SettingsR.string.display to "Display",
        SettingsR.string.summary_preference_settings_display to "Personalize your app's look and feel",
        SettingsR.string.security_and_privacy to "Security & privacy",
        SettingsR.string.summary_preference_settings_privacy_and_security to "Manage your privacy settings",
        SettingsR.string.advanced to "Advanced",
        SettingsR.string.summary_preference_settings_advanced to "Explore more advanced settings",
        CoreUiR.string.about to "About",
        SettingsR.string.summary_preference_settings_about to "Learn more about the app"
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `provideSettingsConfig returns expected configuration`() {
        val context = createContext(defaultStrings)
        val provider = AppSettingsProvider(context)
        mockkStatic("com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.ContextIntentExtensionsKt")
        every { context.openAppNotificationSettings() } returns true

        val config = provider.provideSettingsConfig()

        assertEquals(defaultStrings[SettingsR.string.settings], config.title)
        assertEquals(2, config.categories.size)

        val generalPreferences = config.categories[0].preferences
        assertEquals(2, generalPreferences.size)

        val notifications = generalPreferences[0]
        assertEquals(SettingsConstants.KEY_SETTINGS_NOTIFICATION, notifications.key)
        assertEquals(defaultStrings[SettingsR.string.notifications], notifications.title)
        assertEquals(
            defaultStrings[SettingsR.string.summary_preference_settings_notifications],
            notifications.summary
        )

        val display = generalPreferences[1]
        assertEquals(SettingsConstants.KEY_SETTINGS_DISPLAY, display.key)
        assertEquals(defaultStrings[SettingsR.string.display], display.title)
        assertEquals(defaultStrings[SettingsR.string.summary_preference_settings_display], display.summary)

        val advancedCategory = config.categories[1].preferences
        assertEquals(3, advancedCategory.size)

        val security = advancedCategory[0]
        assertEquals(SettingsConstants.KEY_SETTINGS_SECURITY_AND_PRIVACY, security.key)
        assertEquals(defaultStrings[SettingsR.string.security_and_privacy], security.title)
        assertEquals(
            defaultStrings[SettingsR.string.summary_preference_settings_privacy_and_security],
            security.summary
        )

        val advanced = advancedCategory[1]
        assertEquals(SettingsConstants.KEY_SETTINGS_ADVANCED, advanced.key)
        assertEquals(defaultStrings[SettingsR.string.advanced], advanced.title)
        assertEquals(
            defaultStrings[SettingsR.string.summary_preference_settings_advanced],
            advanced.summary
        )

        val about = advancedCategory[2]
        assertEquals(SettingsConstants.KEY_SETTINGS_ABOUT, about.key)
        assertEquals(defaultStrings[CoreUiR.string.about], about.title)
        assertEquals(defaultStrings[SettingsR.string.summary_preference_settings_about], about.summary)

        // Each row opens its Toolkit page; notifications first tries the system's page.
        assertEquals(PrivacySettingsRoute, notifications.destination)
        assertEquals(DisplaySettingsRoute, display.destination)
        assertEquals(PrivacySettingsRoute, security.destination)
        assertEquals(AdvancedSettingsRoute, advanced.destination)
        assertEquals(AboutRoute, about.destination)
        assertTrue(notifications.action?.invoke() == true)
        verify(exactly = 1) { context.openAppNotificationSettings() }
        listOf(display, security, advanced, about).forEach { assertNull(it.action) }

        assertNull(config.categories[0].title)
        assertEquals(defaultStrings[SettingsR.string.settings], config.title)
    }

    @Test
    fun `notifications preference falls back to privacy screen when system settings cannot open`() {
        val context = createContext(defaultStrings)
        val provider = AppSettingsProvider(context)
        mockkStatic("com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.ContextIntentExtensionsKt")
        every { context.openAppNotificationSettings() } returns false

        val config = provider.provideSettingsConfig()
        val notifications = config.categories.first().preferences.first()

        // Unhandled, so the settings list opens the row's destination instead.
        assertFalse(assertDoesNotThrow { notifications.action?.invoke() } == true)
        assertEquals(PrivacySettingsRoute, notifications.destination)
        verify(exactly = 1) { context.openAppNotificationSettings() }
    }

    @Test
    fun `provideSettingsConfig handles missing resources without crashing`() {
        val context = createContext(emptyMap()) { "<missing>" }
        val provider = AppSettingsProvider(context)

        val config = assertDoesNotThrow { provider.provideSettingsConfig() }

        assertEquals("<missing>", config.title)
        config.categories.flatMap { it.preferences }.forEach { preference ->
            assertEquals("<missing>", preference.title)
            assertEquals("<missing>", preference.summary)
        }
    }

    private fun createContext(
        overrides: Map<Int, String>,
        fallback: (Int) -> String = { id -> "missing-$id" }
    ): Context {
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        every { context.getString(any()) } answers { overrides[arg<Int>(0)] ?: fallback(arg(0)) }
        return context
    }
}
