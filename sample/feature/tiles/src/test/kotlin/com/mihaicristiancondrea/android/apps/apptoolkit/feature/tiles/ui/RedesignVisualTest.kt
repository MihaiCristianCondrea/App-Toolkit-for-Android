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

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.ProtractorTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** Render the stateless native screens for side-by-side review with the marketing kit. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w430dp-h957dp")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class RedesignVisualTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private fun capture(name: String) {
        val output = File("build/outputs/redesign/"+name+".png")
        output.parentFile?.mkdirs()
        output.outputStream().use { compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG,100,it) }
    }
    @Test fun measure() {
        compose.setContent { MaterialTheme { Surface { NumbersScreenContent(CounterToolUiState(count = Int.MAX_VALUE), PaddingValues()) } } }
        compose.waitForIdle()
        capture("measure-phone")
    }
    @Test fun play() {
        compose.setContent { MaterialTheme { Surface { PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues()) } } }
        compose.waitForIdle()
        capture("play-phone")
    }
    @Test
    @Config(sdk = [36], qualifiers = "w840dp-h780dp")
    fun measureFoldDark() {
        compose.setContent { MaterialTheme(colorScheme = darkColorScheme()) { Surface { NumbersScreenContent(CounterToolUiState(count = 42), PaddingValues()) } } }
        compose.waitForIdle()
        capture("measure-fold-dark")
    }
    @Test
    @Config(sdk = [36], qualifiers = "w957dp-h430dp")
    fun protractorLandscape() {
        compose.setContent { MaterialTheme { ProtractorTool(ProtractorToolUiState(angle = 120f), {}, {}) } }
        compose.waitForIdle()
        val output = File("build/outputs/redesign/protractor-landscape.png")
        output.parentFile?.mkdirs()
        output.outputStream().use { compose.onNode(isDialog()).captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG,100,it) }
    }
    @Test fun quickTools() {
        val categories = listOf(
            ToolkitTileCategoryData("sensors", tiles = persistentListOf(ToolkitTileData("compass", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available), ToolkitTileData("bubble_level", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("system", tiles = persistentListOf(ToolkitTileData("flash_dimmer", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available), ToolkitTileData("sos", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("sound", tiles = persistentListOf(ToolkitTileData("water_eject", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("wellbeing", tiles = persistentListOf(ToolkitTileData("breathing", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
        ).toUiModels()
        compose.setContent { MaterialTheme { Surface {
            CompositionLocalProvider(LocalAdsEnabled provides false, LocalTelemetry provides FakeTelemetryRepository()) {
                ToolkitTilesScreenContent(ToolkitTilesUiState(categories = Loadable.Ready(categories)), PaddingValues(), {})
            }
        } } }
        compose.waitForIdle()
        capture("quick-tools-phone")
    }
}
