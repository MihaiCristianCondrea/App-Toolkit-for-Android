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

    /** The kit's design scheme ("dynamic" in marketing/ui/src/theme/palettes.css), so differences are the app's, not the palette's. */
    private val kitLight = androidx.compose.material3.lightColorScheme(
        primary = Color(0xFF286A49),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFADF2C7),
        onPrimaryContainer = Color(0xFF055232),
        inversePrimary = Color(0xFF92D5AC),
        secondary = Color(0xFF4E6355),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD0E8D6),
        onSecondaryContainer = Color(0xFF374B3E),
        tertiary = Color(0xFF3C6471),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBFE9F9),
        onTertiaryContainer = Color(0xFF224C59),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        surface = Color(0xFFF6FBF4),
        onSurface = Color(0xFF171D19),
        surfaceVariant = Color(0xFFDCE5DC),
        onSurfaceVariant = Color(0xFF404942),
        inverseSurface = Color(0xFF2C322D),
        inverseOnSurface = Color(0xFFEDF2EB),
        outline = Color(0xFF717972),
        outlineVariant = Color(0xFFC0C9C0),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFFF6FBF4),
        surfaceContainer = Color(0xFFEAEFE9),
        surfaceContainerHigh = Color(0xFFE4EAE3),
        surfaceContainerHighest = Color(0xFFDFE4DD),
        surfaceContainerLow = Color(0xFFF0F5EE),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceDim = Color(0xFFD6DBD5),
    )
    private val kitDark = androidx.compose.material3.darkColorScheme(
        primary = Color(0xFF92D5AC),
        onPrimary = Color(0xFF003921),
        primaryContainer = Color(0xFF055232),
        onPrimaryContainer = Color(0xFFADF2C7),
        inversePrimary = Color(0xFF286A49),
        secondary = Color(0xFFB5CCBB),
        onSecondary = Color(0xFF213528),
        secondaryContainer = Color(0xFF374B3E),
        onSecondaryContainer = Color(0xFFD0E8D6),
        tertiary = Color(0xFFA3CDDC),
        onTertiary = Color(0xFF043541),
        tertiaryContainer = Color(0xFF224C59),
        onTertiaryContainer = Color(0xFFBFE9F9),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        surface = Color(0xFF0F1511),
        onSurface = Color(0xFFDFE4DD),
        surfaceVariant = Color(0xFF404942),
        onSurfaceVariant = Color(0xFFC0C9C0),
        inverseSurface = Color(0xFFDFE4DD),
        inverseOnSurface = Color(0xFF2C322D),
        outline = Color(0xFF8A938B),
        outlineVariant = Color(0xFF404942),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFF353B36),
        surfaceContainer = Color(0xFF1B211D),
        surfaceContainerHigh = Color(0xFF262B27),
        surfaceContainerHighest = Color(0xFF313632),
        surfaceContainerLow = Color(0xFF171D19),
        surfaceContainerLowest = Color(0xFF0A0F0C),
        surfaceDim = Color(0xFF0F1511),
    )

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
