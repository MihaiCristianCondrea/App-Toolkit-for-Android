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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.StatusColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.Cookie16Shape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.Cookie8Shape
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.KitShape

internal fun ToolkitTileIcon.imageVector(): ImageVector = when (this) {
    ToolkitTileIcon.Wheel -> ToolSymbols.RotateRight
    ToolkitTileIcon.Water -> ToolSymbols.WaterDrop
    ToolkitTileIcon.Bell -> ToolSymbols.Notifications
    ToolkitTileIcon.Toys -> ToolSymbols.Rounded.Toys
    ToolkitTileIcon.Protractor -> ToolSymbols.Architecture
    ToolkitTileIcon.BillSplitter -> ToolSymbols.Calculate
    ToolkitTileIcon.RandomNumber -> ToolSymbols.Casino
    ToolkitTileIcon.Level -> ToolSymbols.Straighten
    ToolkitTileIcon.Compass -> ToolSymbols.Explore
    ToolkitTileIcon.Coin -> ToolSymbols.MonetizationOn
    ToolkitTileIcon.Dice -> ToolSymbols.Casino
    ToolkitTileIcon.Counter -> ToolSymbols.Dehaze
    ToolkitTileIcon.Breathing -> ToolSymbols.Favorite
    ToolkitTileIcon.Sos -> ToolSymbols.Emergency
    ToolkitTileIcon.Morse -> ToolSymbols.Emergency
    ToolkitTileIcon.FlashDimmer -> ToolSymbols.FlashlightOn
    ToolkitTileIcon.Palette -> ToolSymbols.Palette
    ToolkitTileIcon.Timer -> ToolSymbols.Timer
    ToolkitTileIcon.Converter -> ToolSymbols.Straighten
    ToolkitTileIcon.Ruler -> ToolSymbols.SquareFoot
}

/** The kit's shape behind a tool's live face (see [KitShape]). */
@Composable
internal fun ToolkitTileIcon.backgroundShape(): Shape = when (this) {
    ToolkitTileIcon.Compass -> Cookie8Shape
    ToolkitTileIcon.Level -> Cookie16Shape
    ToolkitTileIcon.Sos, ToolkitTileIcon.Morse -> KitShape.Cookie7
    ToolkitTileIcon.FlashDimmer -> KitShape.Burst
    ToolkitTileIcon.Water, ToolkitTileIcon.Bell, ToolkitTileIcon.Protractor,
    ToolkitTileIcon.Converter, ToolkitTileIcon.Timer, ToolkitTileIcon.Toys -> KitShape.Cookie9
    ToolkitTileIcon.Breathing, ToolkitTileIcon.BillSplitter, ToolkitTileIcon.Palette -> KitShape.SoftBurst
    ToolkitTileIcon.Counter -> KitShape.Flower
    ToolkitTileIcon.Coin, ToolkitTileIcon.Dice, ToolkitTileIcon.Ruler, ToolkitTileIcon.Wheel -> KitShape.Cookie12
    ToolkitTileIcon.RandomNumber -> KitShape.Gem
}

/**
 * The shape of a tool's badge in its sheet header, the kit's `TOOLS[id].shape`. It is the live face's
 * shape for every tool but two: the compass and the level draw their faces in the brand cookies and
 * their badges in the kit's `sunny` and `pill`.
 */
@Composable
internal fun ToolkitTileIcon.badgeShape(): Shape = when (this) {
    ToolkitTileIcon.Compass -> KitShape.Sunny
    ToolkitTileIcon.Level -> KitShape.Pill
    else -> backgroundShape()
}

/**
 * The tool accents of marketing/ui/src/kit/kit.css (--tk-teal, --tk-blue, --tk-orange, --tk-red, --tk-magenta,
 * --tk-gold), light and dark. Sensors are teal, Water Eject blue, Breathe orange, SOS and Morse red, the bill
 * splitter pink, and every other tool gold.
 */
@Composable
internal fun ToolkitTileIcon.iconColors(): StatusColors {
    val dark = MaterialTheme.colorScheme.surface.luminance() < .5f
    return when (this) {
        ToolkitTileIcon.Level, ToolkitTileIcon.Compass -> StatusColors(
            container = if (dark) Color(0xFF225B53) else Color(0xFF00A091),
            content = if (dark) Color(0xFF8FD3C7) else Color(0xFF006A60))
        ToolkitTileIcon.Water -> StatusColors(
            container = if (dark) Color(0xFF234B82) else Color(0xFF2D7DF6),
            content = if (dark) Color(0xFFA4C7F7) else Color(0xFF0B4FA8))
        ToolkitTileIcon.Breathing -> StatusColors(
            container = if (dark) Color(0xFF79471D) else Color(0xFFFF8B26),
            content = if (dark) Color(0xFFE7BC98) else Color(0xFF8B4100))
        ToolkitTileIcon.Sos, ToolkitTileIcon.Morse -> StatusColors(
            container = if (dark) Color(0xFFB10000) else Color(0xFFEE0000),
            content = if (dark) Color(0xFFFF897D) else Color(0xFFB10000))
        ToolkitTileIcon.BillSplitter -> StatusColors(
            container = if (dark) Color(0xFF753354) else Color(0xFFE2008E),
            content = if (dark) Color(0xFFE4B1CA) else Color(0xFF91005A))
        else -> StatusColors(
            container = if (dark) Color(0xFF746528) else Color(0xFFE2C900),
            content = if (dark) Color(0xFFDCCB7A) else Color(0xFF6B5E00))
    }
}

/**
 * Whether the tool's sheet is drawn in its own accent instead of the gold tool theme. The compass is not
 * listed: its sheet draws its accent itself.
 */
internal fun ToolkitTileIcon.hasToolAccent(): Boolean = when (this) {
    ToolkitTileIcon.Level, ToolkitTileIcon.Water, ToolkitTileIcon.Breathing,
    ToolkitTileIcon.Sos, ToolkitTileIcon.Morse, ToolkitTileIcon.BillSplitter -> true
    else -> false
}
