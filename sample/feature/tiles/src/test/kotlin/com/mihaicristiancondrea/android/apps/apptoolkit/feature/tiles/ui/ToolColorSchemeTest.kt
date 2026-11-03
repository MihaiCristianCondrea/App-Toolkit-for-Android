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

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.hasToolAccent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.StatusColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolDarkColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.ToolLightColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.toolAccentScheme
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.toolColorScheme
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToolColorSchemeTest {
    @Test fun preservesHostBackgroundAndNeutralRoles() {
        for (host in listOf(
            lightColorScheme(background = Color(0xFFEAF1F5), surface = Color(0xFFF8FCF7), surfaceContainerHigh = Color(0xFFE1E8EE)),
            darkColorScheme(background = Color(0xFF151822), surface = Color(0xFF121923), surfaceContainerHigh = Color(0xFF242D38)),
        )) {
            val result = toolColorScheme(host)
            assertEquals(host.background, result.background)
            assertEquals(host.onBackground, result.onBackground)
            assertEquals(host.surface, result.surface)
            assertEquals(host.onSurface, result.onSurface)
            assertEquals(host.surfaceVariant, result.surfaceVariant)
            assertEquals(host.onSurfaceVariant, result.onSurfaceVariant)
            assertEquals(host.surfaceContainerLowest, result.surfaceContainerLowest)
            assertEquals(host.surfaceContainerLow, result.surfaceContainerLow)
            assertEquals(host.surfaceContainer, result.surfaceContainer)
            assertEquals(host.surfaceContainerHigh, result.surfaceContainerHigh)
            assertEquals(host.surfaceContainerHighest, result.surfaceContainerHighest)
            assertEquals(host.surfaceDim, result.surfaceDim)
            assertEquals(host.surfaceBright, result.surfaceBright)
            assertEquals(host.surfaceTint, result.surfaceTint)
        }
    }

    @Test fun keepsProductAccentsIndependentOfTheHostPalette() {
        val light = toolColorScheme(lightColorScheme(primary = Color.Blue, secondaryContainer = Color.Cyan))
        val dark = toolColorScheme(darkColorScheme(primary = Color.Red, tertiaryContainer = Color.Green))
        assertEquals(ToolLightColors.primary, light.primary)
        assertEquals(ToolLightColors.secondaryContainer, light.secondaryContainer)
        assertEquals(ToolDarkColors.primary, dark.primary)
        assertEquals(ToolDarkColors.tertiaryContainer, dark.tertiaryContainer)
    }

    @Test fun appliesToolAccentToBottomSheetColorSchemeOnlyForAccentedTools() {
        assertTrue(ToolkitTileIcon.Level.hasToolAccent())
        assertTrue(ToolkitTileIcon.Water.hasToolAccent())
        assertTrue(ToolkitTileIcon.Breathing.hasToolAccent())
        assertTrue(ToolkitTileIcon.Sos.hasToolAccent())
        assertTrue(ToolkitTileIcon.Morse.hasToolAccent())
        assertTrue(ToolkitTileIcon.BillSplitter.hasToolAccent())
        assertFalse(ToolkitTileIcon.Compass.hasToolAccent())
        assertFalse(ToolkitTileIcon.FlashDimmer.hasToolAccent())

        val host = toolColorScheme(lightColorScheme())
        val accent = StatusColors(container = Color(0xFFEE0000), content = Color(0xFFB10000))
        val scheme = toolAccentScheme(host, accent)
        assertEquals(accent.content, scheme.primary)
        assertEquals(accent.container, scheme.inversePrimary)
        assertEquals(lerp(host.surfaceContainerHighest, accent.container, .22f), scheme.primaryContainer)
        assertEquals(lerp(accent.content, host.onSurface, .35f), scheme.onPrimaryContainer)
    }
}
