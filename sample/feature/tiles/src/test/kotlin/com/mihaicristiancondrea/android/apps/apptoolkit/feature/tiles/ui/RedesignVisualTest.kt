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
import androidx.compose.material3.toShape
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
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
    @Test
    @Config(sdk = [36], qualifiers = "w430dp-h957dp")
    fun playDark() {
        compose.setContent { MaterialTheme(colorScheme = darkColorScheme()) { Surface { PlayScreenContent(CoinFlipToolUiState(), DiceRollToolUiState(result = 5), RandomNumberToolUiState(), WheelToolUiState(), PaddingValues()) } } }
        compose.waitForIdle()
        capture("play-phone-dark")
    }
    @Test
    @Config(sdk = [36], qualifiers = "w430dp-h1500dp")
    fun quickToolsDark() = quickTools(dark = true)
    @Test
    @Config(sdk = [36], qualifiers = "w430dp-h1500dp")
    fun quickToolsLight() = quickTools(dark = false)
    private fun quickTools(dark: Boolean) {
        val categories = listOf(
            ToolkitTileCategoryData("sensors", tiles = persistentListOf(ToolkitTileData("compass", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available), ToolkitTileData("bubble_level", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("system", tiles = persistentListOf(ToolkitTileData("flash_dimmer", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available), ToolkitTileData("sos", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("sound", tiles = persistentListOf(ToolkitTileData("water_eject", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("wellbeing", tiles = persistentListOf(ToolkitTileData("breathing", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available))),
            ToolkitTileCategoryData("colors", tiles = persistentListOf(ToolkitTileData("material_colors", status = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitTileStatus.Available, quickTool = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.ToolkitQuickTool.MaterialColors))),
        ).toUiModels()
        compose.setContent { MaterialTheme(colorScheme = if (dark) darkColorScheme() else androidx.compose.material3.lightColorScheme()) { Surface {
            CompositionLocalProvider(LocalAdsEnabled provides false, LocalTelemetry provides FakeTelemetryRepository()) {
                ToolkitTilesScreenContent(ToolkitTilesUiState(categories = Loadable.Ready(categories)), PaddingValues(), {})
            }
        } } }
        compose.waitForIdle()
        capture(if (dark) "quick-tools-phone-dark" else "quick-tools-phone")
    }
    @Test
    @Config(sdk = [36], qualifiers = "w430dp-h500dp")
    @OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
    fun quickToolsAd() {
        compose.setContent { MaterialTheme(typography = com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.typography.AppTypography) { Surface {
            val palette = com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.nativeAdPalette()
            val style = com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle(
                badgeShape = com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdBadgeShape(androidx.compose.material3.MaterialShapes.Cookie9Sided.toShape(), 88.dp),
                badgeColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconInsetDp = 16,
            )
            androidx.compose.foundation.layout.Column(androidx.compose.ui.Modifier.padding(16.dp)) {
                androidx.compose.ui.viewinterop.AndroidView(factory = { context ->
                    com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads.QuickToolsNativeAdViewFactory()
                        .createViewHolder(context, com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation.Compact).apply {
                            label.text = "Sponsored content"; headline.text = "Advertiser app name"
                            body?.text = "One short line from the advertiser about what the app does."
                            advertiser.text = "Advertiser"; callToAction?.text = "Install"
                            icon?.setImageDrawable(android.graphics.drawable.ColorDrawable(0xFF3366CC.toInt()))
                        }.root
                }, update = { view -> (view.tag as com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewHolder).applyPalette(palette, style) })
            }
        } } }
        compose.waitForIdle()
        capture("quick-tools-ad")
    }
    @Test
    @Config(sdk = [36], qualifiers = "w430dp-h400dp")
    fun coinTurns() {
        compose.setContent { MaterialTheme { Surface {
            androidx.compose.foundation.layout.FlowRow(androidx.compose.ui.Modifier.padding(16.dp), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                for (angle in listOf(0f, 35f, 65f, 88f, 120f, 180f)) com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.Coin(angle, 120.dp)
            }
        } } }
        compose.waitForIdle()
        capture("coin-turns")
    }
    private fun ruler(name: String, state: com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState) {
        compose.setContent { MaterialTheme(typography = com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.typography.AppTypography) {
            com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.RulerTool(state, {})
        } }
        compose.waitForIdle()
        val output = File("build/outputs/redesign/"+name+".png")
        output.parentFile?.mkdirs()
        output.outputStream().use { compose.onNode(isDialog()).captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG,100,it) }
    }
    @Test fun rulerSimple() = ruler("ruler-simple", com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState(marker = .3f))
    @Test fun rulerAdvanced() = ruler("ruler-advanced", com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState(
        settings = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings(advanced = true, inchDivisions = 8), start = .1f, end = .4f))
    @Test
    @Config(sdk = [36], qualifiers = "w957dp-h430dp")
    fun rulerLandscape() = ruler("ruler-landscape", com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.RulerToolUiState(
        settings = com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings(countFromEnd = true), marker = .4f))
}
