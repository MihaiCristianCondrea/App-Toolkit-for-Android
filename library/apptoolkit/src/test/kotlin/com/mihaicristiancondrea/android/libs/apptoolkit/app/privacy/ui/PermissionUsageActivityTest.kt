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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.privacy.ui

import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.di.privacyModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.ui.providers.PrivacySettingsProvider
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.privacy.R as PrivacyR

/**
 * Starts [PermissionUsageActivity] the way Android's permission manager does and checks that it
 * shows the privacy page and that its back arrow leaves, returning to the system's settings.
 */
// The same Android version as the shell's screenshot tests.
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class PermissionUsageActivityTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUpKoin() {
        startKoin {
            modules(
                module {
                    single { CommonDataStore(context) }
                    single<TelemetryRepository> { mockk(relaxed = true) }
                    single<PrivacySettingsProvider> { object : PrivacySettingsProvider {} }
                },
                privacyModule,
            )
        }
    }

    @After
    fun tearDownKoin() {
        stopKoin()
    }

    private fun launch(action: String): ActivityScenario<PermissionUsageActivity> =
        ActivityScenario.launch(Intent(action).setClass(context, PermissionUsageActivity::class.java))

    private fun awaitPrivacyPage() {
        // ShellHost awaits DataStore before drawing its first page.
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(context.getString(PrivacyR.string.security_and_privacy))
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun `the permission manager's icon opens the privacy page, and its back arrow leaves`() {
        launch("android.intent.action.VIEW_PERMISSION_USAGE").use { scenario ->
            awaitPrivacyPage()
            composeRule.onNodeWithText(context.getString(PrivacyR.string.security_and_privacy)).assertExists()

            composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.go_back)).performClick()
            composeRule.waitForIdle()

            var finishing = false
            scenario.onActivity { finishing = it.isFinishing }
            assertTrue(finishing)
        }
    }

    @Test
    fun `system back leaves the privacy page for the system's settings`() {
        launch("android.intent.action.VIEW_PERMISSION_USAGE").use { scenario ->
            awaitPrivacyPage()

            var finishing = false
            scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
                finishing = activity.isFinishing
            }
            assertTrue(finishing)
        }
    }

    @Test
    fun `the privacy dashboard's icon opens the privacy page too`() {
        launch("android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD").use {
            awaitPrivacyPage()
            composeRule.onNodeWithText(context.getString(PrivacyR.string.security_and_privacy)).assertExists()
        }
    }
}
