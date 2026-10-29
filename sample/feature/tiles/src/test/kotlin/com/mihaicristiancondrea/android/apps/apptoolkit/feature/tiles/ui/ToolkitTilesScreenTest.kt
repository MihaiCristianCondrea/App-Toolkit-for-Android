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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp")
class ToolkitTilesScreenTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    @Test fun searchShowsMatchingWidgetsWithoutOldFilters() {
        val categories = listOf(
            ToolkitTileCategoryData("sensors", tiles = persistentListOf(ToolkitTileData("compass", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available),ToolkitTileData("bubble_level", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("system", tiles = persistentListOf(ToolkitTileData("flash_dimmer", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available),ToolkitTileData("sos", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
        ).toUiModels()
        compose.setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalAdsEnabled provides false, LocalTelemetry provides FakeTelemetryRepository()) {
                    LookaheadScope {
                        ToolkitTilesScreenContent(ToolkitTilesUiState(categories = Loadable.Ready(categories)), PaddingValues(0.dp), {}, searchQuery = "compass")
                    }
                }
            }
        }
        compose.onNodeWithText("Compass").assertIsDisplayed()
        compose.onAllNodesWithText("Flash Dimmer").assertCountEquals(0)
        compose.onAllNodesWithText("All tools").assertCountEquals(0)
    }
}
