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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.contracts.ToolkitTilesEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileCategory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp")
class ToolkitTilesScreenTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun scrollingAndExpandingCategoriesInsideLookaheadKeepsTheCatalogUsable() {
        val categories = persistentListOf(
            ToolkitTileCategory(
                id = "sensors",
                titleResId = R.string.tiles_category_sensors,
                icon = ToolkitTileIcon.Compass,
                tiles = (0..7).map { tile("sensor_$it") }.toImmutableList(),
            ),
            ToolkitTileCategory(
                id = "utilities",
                titleResId = R.string.tiles_category_utilities,
                icon = ToolkitTileIcon.Dice,
                tiles = persistentListOf(tile("utility_1"), tile("utility_2")),
            ),
        )
        compose.setContent {
            var expanded by remember { mutableStateOf(persistentSetOf("sensors")) }
            MaterialTheme {
                CompositionLocalProvider(
                    LocalAdsEnabled provides false,
                    LocalTelemetry provides FakeTelemetryRepository(),
                ) {
                    LookaheadScope {
                        ToolkitTilesScreenContent(
                            state = ToolkitTilesUiState(categories = Loadable.Ready(categories), expandedCategoryIds = expanded),
                            contentPadding = PaddingValues(0.dp),
                            onEvent = { event ->
                                if (event is ToolkitTilesEvent.CategoryToggled) {
                                    expanded = if (event.categoryId in expanded) expanded.remove(event.categoryId)
                                    else expanded.add(event.categoryId)
                                }
                            },
                        )
                    }
                }
            }
        }
        val verticalScroll = compose.onNode(
            hasScrollAction() and SemanticsMatcher.keyIsDefined(SemanticsProperties.VerticalScrollAxisRange),
        )
        repeat(3) {
            repeat(4) { verticalScroll.performTouchInput { swipeUp() } }
            compose.onAllNodesWithText("Utilities & Fun").onLast().assertIsDisplayed()
            compose.onNodeWithContentDescription("Expand category").performClick()
            compose.waitForIdle()
            compose.onAllNodesWithContentDescription("Collapse category").onLast().performClick()
            compose.waitForIdle()
            repeat(4) { verticalScroll.performTouchInput { swipeDown() } }
            compose.onAllNodesWithText("Bubble Level").onFirst().assertIsDisplayed()
        }
    }

    private fun tile(id: String) = ToolkitTile(
        id = id,
        titleResId = R.string.tile_bubble_level_title,
        summaryResId = R.string.tile_bubble_level_summary,
        icon = ToolkitTileIcon.Level,
        status = ToolkitTileStatus.Available,
    )
}
