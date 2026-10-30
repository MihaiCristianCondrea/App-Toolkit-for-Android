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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.AppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.MoreAppsEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.MoreAppsUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** What a tap in "More apps" does: open, show details, go to the store, or view all. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp")
class MoreAppsSectionTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val installed = AppSummary(name = "Smart Cleaner", packageName = "com.example.cleaner", iconUrl = "", shortDescription = "Frees up storage")
    private val notInstalled = AppSummary(name = "Android Insider", packageName = "com.example.insider", iconUrl = "", shortDescription = "Android news")

    private val events = mutableListOf<MoreAppsEvent>()
    private val opened = mutableListOf<String>()
    private val stores = mutableListOf<String>()
    private var viewAll = 0

    private fun show(state: MoreAppsUiState, fontScale: Float = 1f) {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
            MaterialTheme {
                MoreAppsSectionContent(
                    state = state,
                    onEvent = { event: MoreAppsEvent -> events += event },
                    onOpenApp = { app: AppSummary -> opened += app.packageName },
                    onOpenStore = { app: AppSummary -> stores += app.packageName },
                    onViewAll = { viewAll++ },
                    onCardsVisibilityChanged = {},
                    onDetailsClosed = {},
                    onFavoriteToggle = {},
                    detailsActionLauncher = mockk<AppActionLauncher>(relaxed = true),
                )
            }
            }
        }
    }

    private val ready = MoreAppsUiState(
        apps = Loadable.Ready(persistentListOf(installed, notInstalled)),
        installedPackages = persistentSetOf(installed.packageName),
    )

    @Test
    fun `an installed app opens and one that is not shows its details`() {
        show(ready)

        compose.onNodeWithText("Smart Cleaner").performClick()
        compose.onNodeWithText("Android Insider").performClick()

        assertEquals(listOf(installed.packageName), opened)
        assertEquals(listOf<MoreAppsEvent>(MoreAppsEvent.AppSelected(notInstalled.packageName)), events)
    }

    @Test
    fun `a card's button opens the app or its store page`() {
        show(ready)

        compose.onNodeWithContentDescription("Open Smart Cleaner").performClick()
        compose.onNodeWithContentDescription("Open Android Insider on Google Play").performScrollTo().performClick()

        assertEquals(listOf(installed.packageName), opened)
        assertEquals(listOf(notInstalled.packageName), stores)
    }

    @Test
    fun `view all opens the developer page`() {
        show(ready)

        compose.onNodeWithContentDescription("View all apps on Google Play").performClick()

        assertEquals(1, viewAll)
        assertEquals("https://play.google.com/store/apps/dev?id=5390214922640123642", DeveloperPageUrl)
    }

    @Test
    fun `a failed catalogue without cached apps contributes no widget`() {
        show(MoreAppsUiState(apps = Loadable.Failed(com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText.DynamicString("Failed to load apps"))))

        compose.onNodeWithText("More apps").assertDoesNotExist()
        compose.onNodeWithText("Failed to load apps").assertDoesNotExist()
        assertEquals(emptyList<MoreAppsEvent>(), events)
    }

    @Test
    fun `an empty catalogue contributes no widget`() {
        show(MoreAppsUiState(apps = Loadable.Empty()))

        compose.onNodeWithText("More apps").assertDoesNotExist()
        compose.onNodeWithText("No apps are available right now.").assertDoesNotExist()
    }

    @Test
    @Config(qualifiers = "w320dp-h640dp")
    fun tonalActionRemainsReachableWithLargeFontsOnASmallPhone() {
        show(ready, fontScale = 2f)
        compose.onNodeWithContentDescription("Open Smart Cleaner").performScrollTo().assertIsDisplayed().performClick()
        assertEquals(listOf(installed.packageName), opened)
    }
}
