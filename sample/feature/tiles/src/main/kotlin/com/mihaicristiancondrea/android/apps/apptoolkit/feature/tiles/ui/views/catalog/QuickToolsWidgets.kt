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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.BreathingRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.backgroundShape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.states.LiveToolsUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.CompassRose
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.LevelFace
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools.compassDirection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * One Quick Tools section's widgets, as the marketing kit draws them (marketing/ui/src/screens/quick-tools.js):
 * the sensors and the torch tools in pairs of live widgets, Water Eject on its own, Breathe and Material
 * Colors as wide cards. Every widget opens its tool; on a torch that can dim, a vertical swipe over the
 * flashlight's shape sets the brightness instead.
 */
@Composable
internal fun QuickToolsWidgets(tools: List<ToolkitTile>, live: LiveToolsUiState, onOpen: (ToolkitTile) -> Unit, onLevel: (Int) -> Unit) {
    val single = tools.singleOrNull()
    when (single?.id) {
        ToolkitTileIds.BREATHING -> return BreatheCard(single) { onOpen(single) }
        ToolkitTileIds.MATERIAL_COLORS -> return MaterialColorsCard(single) { onOpen(single) }
        ToolkitTileIds.WATER_EJECT -> return WaterEjectWidget(single, Modifier.fillMaxWidth()) { onOpen(single) }
    }
    ToolPair(tools) { tile, shape ->
        val modifier = Modifier.weight(1f).fillMaxHeight()
        when (tile.id) {
            ToolkitTileIds.COMPASS -> {
                val heading = live.heading.roundToInt().mod(360)
                LiveWidget(
                    label = stringResource(tile.titleResId),
                    accent = tile.icon.iconColors(),
                    faceShape = tile.icon.backgroundShape(),
                    value = stringResource(R.string.tile_service_azimuth_format, heading),
                    note = compassDirection(live.heading),
                    onClick = { onOpen(tile) },
                    modifier = modifier,
                    shape = shape,
                ) { CompassRose(live.heading, Modifier.size(136.dp), face = false, degreeLabels = false) }
            }
            ToolkitTileIds.BUBBLE_LEVEL -> {
                val flat = abs(live.pitch) < 1f && abs(live.roll) < 1f
                LiveWidget(
                    label = stringResource(tile.titleResId),
                    accent = tile.icon.iconColors(),
                    faceShape = tile.icon.backgroundShape(),
                    value = stringResource(if (flat) R.string.tile_preview_level_result else R.string.widget_level_tilted),
                    note = String.format(LocalConfiguration.current.locales[0], "%.1f° · %.1f°", abs(live.roll), abs(live.pitch)),
                    onClick = { onOpen(tile) },
                    modifier = modifier,
                    shape = shape,
                ) { LevelFace(live.pitch, live.roll, Modifier.fillMaxSize()) }
            }
            ToolkitTileIds.FLASH_DIMMER -> FlashlightWidget(tile, live, onLevel, modifier, shape) { onOpen(tile) }
            else -> LiveWidget(
                label = stringResource(R.string.tile_sos_title),
                accent = tile.icon.iconColors(),
                faceShape = tile.icon.backgroundShape(),
                value = stringResource(R.string.morse_ready),
                note = stringResource(R.string.widget_sos_note),
                onClick = { onOpen(tile) },
                modifier = modifier,
                shape = shape,
                signal = true,
            ) {
                Text(
                    text = "SOS",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 30.sp, lineHeight = 30.sp, letterSpacing = 1.sp),
                    fontWeight = FontWeight.ExtraBold,
                    color = tile.icon.iconColors().content,
                )
            }
        }
    }
}

/** The torch: the burst fills like a progress bar with the brightness, and a vertical swipe over it sets it. */
@Composable
private fun FlashlightWidget(
    tile: ToolkitTile,
    live: LiveToolsUiState,
    onLevel: (Int) -> Unit,
    modifier: Modifier,
    shape: androidx.compose.ui.graphics.Shape,
    onOpen: () -> Unit,
) {
    val torch = live.torch
    val maximum = torch.capabilities.maximumLevel
    val dims = torch.capabilities.supportsDimming && maximum > 1
    val level = if (torch.isEnabled) torch.currentLevel.coerceIn(0, maximum.coerceAtLeast(1)) else 0
    val fraction = if (!torch.isEnabled) 0f else if (dims) level.toFloat() / maximum else 1f
    val accent = tile.icon.iconColors()
    val latestLevel by rememberUpdatedState(onLevel)
    val haptic = LocalHapticFeedback.current
    val swipe = if (dims) Modifier.pointerInput(maximum) {
        detectVerticalDragGestures(onDragStart = { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) }) { change, _ ->
            change.consume()
            latestLevel(((1f - change.position.y / size.height) * maximum).roundToInt().coerceIn(0, maximum))
        }
    } else Modifier
    LiveWidget(
        label = stringResource(R.string.widget_flashlight),
        accent = accent,
        faceShape = tile.icon.backgroundShape(),
        value = when {
            !torch.isEnabled -> stringResource(R.string.flash_dimmer_off)
            dims -> stringResource(R.string.widget_flash_level, level, maximum)
            else -> stringResource(R.string.flash_dimmer_on)
        },
        note = stringResource(if (dims) R.string.widget_flash_swipe else R.string.widget_flash_tap),
        onClick = onOpen,
        modifier = modifier,
        shape = shape,
        signal = true,
        faceModifier = swipe,
    ) {
        Box(Modifier.fillMaxWidth().fillMaxHeight(fraction).align(Alignment.BottomCenter).background(accent.container))
        Icon(
            imageVector = if (torch.isEnabled) ToolSymbols.FlashlightOn else ToolSymbols.FlashlightOff,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun WaterEjectWidget(tile: ToolkitTile, modifier: Modifier, onOpen: () -> Unit) {
    LiveWidget(
        label = stringResource(tile.titleResId),
        accent = tile.icon.iconColors(),
        faceShape = tile.icon.backgroundShape(),
        value = "165 Hz",
        note = stringResource(R.string.widget_water_note),
        onClick = onOpen,
        modifier = modifier,
    ) {
        Icon(ToolSymbols.WaterDrop, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurface)
    }
}

/** Breathe has no live reading, so it is one wide card: its shape, its name and its rhythm. */
@Composable
private fun BreatheCard(tile: ToolkitTile, onOpen: () -> Unit) {
    val accent = ToolkitTileIcon.Breathing.iconColors()
    val steps = listOf(
        R.string.breathe_inhale to BreathingRepository.DURATION_INHALE,
        R.string.breathe_hold to BreathingRepository.DURATION_HOLD_FULL,
        R.string.breathe_exhale to BreathingRepository.DURATION_EXHALE,
        R.string.breathe_hold to BreathingRepository.DURATION_HOLD_EMPTY,
    )
    Surface(onClick = onOpen, modifier = Modifier.fillMaxWidth().animateVisibility(), shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.size(72.dp).clip(tile.icon.backgroundShape()).background(accent.container.copy(alpha = 0.22f)), contentAlignment = Alignment.Center) {
                Icon(tile.icon.imageVector(), null, Modifier.size(32.dp), tint = accent.content)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(tile.titleResId), style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, lineHeight = 24.sp), fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    steps.forEach { (name, millis) ->
                        Text(
                            text = stringResource(R.string.widget_breathe_step, stringResource(name), (millis / 1000L).toInt()),
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp, lineHeight = 18.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = accent.content,
                        )
                    }
                }
            }
        }
    }
}

/** Material Colors shows the scheme it previews: the three accents and their containers, in one bento strip. */
@Composable
private fun MaterialColorsCard(tile: ToolkitTile, onOpen: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val strip: List<Color> = listOf(
        scheme.primary, scheme.primaryContainer, scheme.secondary,
        scheme.secondaryContainer, scheme.tertiary, scheme.tertiaryContainer,
    )
    Surface(onClick = onOpen, modifier = Modifier.fillMaxWidth().animateVisibility(), shape = RoundedCornerShape(28.dp), color = scheme.surfaceContainerHigh) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            WidgetLabel(stringResource(tile.titleResId))
            Row(Modifier.fillMaxWidth().height(56.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                strip.forEachIndexed { index, color ->
                    Box(Modifier.weight(1f).fillMaxHeight().clip(toolCorners(index, strip.size)).background(color))
                }
            }
            Text(
                text = stringResource(R.string.widget_colors_note),
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = scheme.onSurfaceVariant,
            )
        }
    }
}
