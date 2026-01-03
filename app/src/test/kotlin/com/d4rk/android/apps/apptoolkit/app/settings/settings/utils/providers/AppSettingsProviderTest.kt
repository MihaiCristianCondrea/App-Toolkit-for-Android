package com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.providers

import android.content.Context
import com.d4rk.android.apps.apptoolkit.app.settings.settings.utils.constants.SettingsConstants
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.general.ui.GeneralSettingsActivity
import com.d4rk.android.libs.apptoolkit.app.settings.utils.constants.SettingsContent
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openAppNotificationSettings
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppSettingsProviderTest {

    private val provider = AppSettingsProvider()

    private val defaultStrings = mapOf(
        R.string.settings to "Settings",
        R.string.notifications to "Notifications",
        R.string.summary_preference_settings_notifications to "Manage app notifications",
        R.string.display to "Display",
        R.string.summary_preference_settings_display to "Personalize your app's look and feel",
        R.string.security_and_privacy to "Security & privacy",
        R.string.summary_preference_settings_privacy_and_security to "Manage your privacy settings",
        R.string.advanced to "Advanced",
        R.string.summary_preference_settings_advanced to "Explore more advanced settings",
        R.string.about to "About",
        R.string.summary_preference_settings_about to "Learn more about the app"
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `provideSettingsConfig returns expected configuration`() {
        val context = createContext(defaultStrings)
        mockkStatic("com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.IntentActionsExtensionsKt")
        mockkObject(GeneralSettingsActivity.Companion)
        every { context.openAppNotificationSettings() } returns true
        every { GeneralSettingsActivity.start(any(), any(), any()) } just Runs

        val config = provider.provideSettingsConfig(context)

        assertEquals(defaultStrings[R.string.settings], config.title)
        assertEquals(2, config.categories.size)

        val generalPreferences = config.categories[0].preferences
        assertEquals(2, generalPreferences.size)

        val notifications = generalPreferences[0]
        assertEquals(SettingsConstants.KEY_SETTINGS_NOTIFICATION, notifications.key)
        assertEquals(defaultStrings[R.string.notifications], notifications.title)
        assertEquals(
            defaultStrings[R.string.summary_preference_settings_notifications],
            notifications.summary
        )

        val display = generalPreferences[1]
        assertEquals(SettingsContent.DISPLAY, display.key)
        assertEquals(defaultStrings[R.string.display], display.title)
        assertEquals(defaultStrings[R.string.summary_preference_settings_display], display.summary)

        val advancedCategory = config.categories[1].preferences
        assertEquals(3, advancedCategory.size)

        val security = advancedCategory[0]
        assertEquals(SettingsContent.SECURITY_AND_PRIVACY, security.key)
        assertEquals(defaultStrings[R.string.security_and_privacy], security.title)
        assertEquals(
            defaultStrings[R.string.summary_preference_settings_privacy_and_security],
            security.summary
        )

        val advanced = advancedCategory[1]
        assertEquals(SettingsContent.ADVANCED, advanced.key)
        assertEquals(defaultStrings[R.string.advanced], advanced.title)
        assertEquals(
            defaultStrings[R.string.summary_preference_settings_advanced],
            advanced.summary
        )

        val about = advancedCategory[2]
        assertEquals(SettingsContent.ABOUT, about.key)
        assertEquals(defaultStrings[R.string.about], about.title)
        assertEquals(defaultStrings[R.string.summary_preference_settings_about], about.summary)

        notifications.action.invoke()
        verify(exactly = 1) { context.openAppNotificationSettings() }

        display.action.invoke()
        verify(exactly = 1) {
            GeneralSettingsActivity.start(
                context,
                defaultStrings[R.string.display]!!,
                SettingsContent.DISPLAY
            )
        }

        security.action.invoke()
        verify(exactly = 1) {
            GeneralSettingsActivity.start(
                context,
                defaultStrings[R.string.security_and_privacy]!!,
                SettingsContent.SECURITY_AND_PRIVACY
            )
        }

        advanced.action.invoke()
        verify(exactly = 1) {
            GeneralSettingsActivity.start(
                context,
                defaultStrings[R.string.advanced]!!,
                SettingsContent.ADVANCED
            )
        }

        about.action.invoke()
        verify(exactly = 1) {
            GeneralSettingsActivity.start(
                context,
                defaultStrings[R.string.about]!!,
                SettingsContent.ABOUT
            )
        }

        assertNull(config.categories[0].title)
        assertEquals(defaultStrings[R.string.settings], config.title)
    }

    @Test
    fun `notifications preference falls back to privacy screen when system settings cannot open`() {
        val context = createContext(defaultStrings)
        mockkStatic("com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.IntentActionsExtensionsKt")
        mockkObject(GeneralSettingsActivity.Companion)
        every { context.openAppNotificationSettings() } returns false
        every { GeneralSettingsActivity.start(any(), any(), any()) } just Runs

        val config = provider.provideSettingsConfig(context)
        val notifications = config.categories.first().preferences.first()

        assertDoesNotThrow { notifications.action.invoke() }

        verify(exactly = 1) { context.openAppNotificationSettings() }
        verify(exactly = 1) {
            GeneralSettingsActivity.start(
                context,
                defaultStrings[R.string.security_and_privacy]!!,
                SettingsContent.SECURITY_AND_PRIVACY
            )
        }
    }

    @Test
    fun `provideSettingsConfig handles missing resources without crashing`() {
        val context = createContext(emptyMap()) { "<missing>" }

        val config = assertDoesNotThrow { provider.provideSettingsConfig(context) }

        assertEquals("<missing>", config.title)
        config.categories.flatMap { it.preferences }.forEach { preference ->
            assertEquals("<missing>", preference.title)
            assertEquals("<missing>", preference.summary)
        }
    }

    @Test
    fun `provideSettingsConfig handles null context safely`() {
        val result = assertDoesNotThrow {
            null
        }

        assertNull(result)
    }

    private fun createContext(
        overrides: Map<Int, String>,
        fallback: (Int) -> String = { id -> "missing-$id" }
    ): Context {
        val context = mockk<Context>(relaxed = true)
        every { context.getString(any()) } answers { overrides[arg<Int>(0)] ?: fallback(arg(0)) }
        return context
    }
}
