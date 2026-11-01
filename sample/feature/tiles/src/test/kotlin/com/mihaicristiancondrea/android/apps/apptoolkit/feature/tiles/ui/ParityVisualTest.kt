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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileCategoryData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toUiModels
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CoinFlipToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.CounterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.DiceRollToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ProtractorToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RandomNumberToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.ToolkitTilesUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.UnitConverterToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.WheelToolUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.ProtractorTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RandomNumberTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RulerTool
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.UnitConverterTool
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.LocalAdsEnabled
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.typography.AppTypography
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

/**
 * Renders the screens and tool sheets in the kit's design scheme and Google Sans, light and dark, for side-by-side review with the marketing kit. Images land in
 * build/outputs/parity.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w430dp-h957dp")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ParityVisualTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private val kitLight = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolLightColors
    private val kitDark = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolDarkColors

    private fun scheme(dark: Boolean): ColorScheme = if (dark) kitDark else kitLight

    private fun render(name: String, dark: Boolean, dialog: Boolean = false, content: @Composable () -> Unit) {
        compose.setContent {
            MaterialTheme(colorScheme = scheme(dark), typography = AppTypography) {
                CompositionLocalProvider(LocalAdsEnabled provides false, LocalTelemetry provides FakeTelemetryRepository()) {
                    Surface { content() }
                }
            }
        }
        compose.waitForIdle()
        val output = File("build/outputs/parity/$name.png")
        output.parentFile?.mkdirs()
        val node = if (dialog) compose.onNode(isDialog()) else compose.onRoot()
        output.outputStream().use { node.captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private fun quickTools() = listOf(
        ToolkitTileCategoryData("sensors", tiles = persistentListOf(ToolkitTileData("compass", status = ToolkitTileStatus.Available), ToolkitTileData("bubble_level", status = ToolkitTileStatus.Available))),
        ToolkitTileCategoryData("system", tiles = persistentListOf(ToolkitTileData("flash_dimmer", status = ToolkitTileStatus.Available), ToolkitTileData("sos", status = ToolkitTileStatus.Available))),
        ToolkitTileCategoryData("sound", tiles = persistentListOf(ToolkitTileData("water_eject", status = ToolkitTileStatus.Available))),
        ToolkitTileCategoryData("wellbeing", tiles = persistentListOf(ToolkitTileData("breathing", status = ToolkitTileStatus.Available))),
        ToolkitTileCategoryData("colors", tiles = persistentListOf(ToolkitTileData("material_colors", status = ToolkitTileStatus.Available, quickTool = ToolkitQuickTool.MaterialColors))),
    ).toUiModels()

    @Test @Config(sdk = [36], qualifiers = "w430dp-h1500dp")
    fun quickToolsDark() = render("quick-tools-dark", dark = true) {
        ToolkitTilesScreenContent(ToolkitTilesUiState(categories = Loadable.Ready(quickTools())), PaddingValues(), {})
    }
    @Test @Config(sdk = [36], qualifiers = "w430dp-h1500dp")
    fun measureDark() = render("measure-dark", dark = true) { NumbersScreenContent(CounterToolUiState(count = 42), PaddingValues()) }
    @Test
    fun playDark() = render("play-dark", dark = true) {
        PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues())
    }
    @Test fun protractor() = render("protractor", dark = false, dialog = true) { ProtractorTool(ProtractorToolUiState(angle = 45f), {}, {}) }
    @Test fun converter() = render("converter", dark = false) { Box(Modifier.padding(16.dp)) { UnitConverterTool(UnitConverterToolUiState(), {}) } }
    @Test fun randomNumber() = render("random-number", dark = false) { Box(Modifier.padding(16.dp)) { RandomNumberTool(RandomNumberToolUiState(), {}) } }
    @Test fun rulerSimple() = render("ruler-simple", dark = false, dialog = true) { RulerTool(RulerToolUiState(), {}) }
    @Test fun rulerAdvanced() = render("ruler-advanced", dark = false, dialog = true) {
        RulerTool(RulerToolUiState(settings = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings(advanced = true)), {})
    }
}
