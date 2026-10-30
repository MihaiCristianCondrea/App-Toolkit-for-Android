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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.analytics.toolOpenedEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toAppColorTable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.toHexString
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.AndroidColorData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.AndroidColorTable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ColorTable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog.TileIconBadge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.utils.constants.ToolkitTileIds
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.copyTextToClipboard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import java.util.Locale

/** Full-height expanded tool that previews the current app and Android Material You palettes. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialColorsToolDialog(
    onClose: () -> Unit,
) {
    val telemetry = LocalTelemetry.current
    LaunchedEffect(Unit) { telemetry.logEvent(toolOpenedEvent(ToolkitTileIds.MATERIAL_COLORS)) }
    val colorScheme = MaterialTheme.colorScheme
    val appColorTableTitle = stringResource(id = R.string.tool_material_colors_app_scheme)
    val appColorTable = remember(colorScheme, appColorTableTitle) {
        colorScheme.toAppColorTable(title = appColorTableTitle)
    }
    val androidColorTables = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        androidMaterialYouTables()
    } else {
        emptyList()
    }

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = SizeConstants.LargeSize)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TileIconBadge(icon = ToolkitTileIcon.Palette, large = true)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.tool_material_colors_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                GeneralButton(
                    style = GeneralButtonStyle.Text,
                    onClick = onClose,
                    icon = ToolkitIcon.Vector(ToolSymbols.Close),
                    contentDescription = stringResource(id = R.string.tool_dialog_close_content_description),
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.tool_material_colors_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                item {
                    AppColorBento(table = appColorTable)
                }
                if (androidColorTables.isNotEmpty()) {
                    items(
                        items = androidColorTables,
                        key = AndroidColorTable::title,
                    ) { table ->
                        AndroidToneStrip(table = table)
                    }
                } else {
                    item {
                        Text(
                            text = stringResource(id = R.string.tool_material_colors_android_unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/** Real scheme roles arranged as one connected bento, rather than nested cards. */
@Composable
private fun AppColorBento(table: ColorTable) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(table.title, style = MaterialTheme.typography.titleMedium)
        BoxWithConstraints {
            // Wider fonts use fewer columns instead of clipping role names or ARGB values.
            val fontScale = LocalDensity.current.fontScale
            val columns = when {
                fontScale > 1.6f -> 1
                maxWidth < 360.dp || fontScale > 1.2f -> 2
                else -> 3
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                table.colors.firstOrNull()?.let { color ->
                    ColorSwatch(
                        name = color.name,
                        color = color.color,
                        hero = true,
                        shape = RoundedCornerShape(28.dp, 28.dp, 8.dp, 8.dp),
                        modifier = Modifier.fillMaxWidth().animateVisibility(index = 0),
                    )
                }
                val rows = table.colors.drop(1).chunked(columns)
                rows.forEachIndexed { rowIndex, row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        row.forEachIndexed { columnIndex, color ->
                            val lastRow = rowIndex == rows.lastIndex
                            ColorSwatch(
                                name = color.name,
                                color = color.color,
                                shape = RoundedCornerShape(
                                    topStart = 8.dp, topEnd = 8.dp,
                                    bottomEnd = if (lastRow && columnIndex == row.lastIndex) 28.dp else 8.dp,
                                    bottomStart = if (lastRow && columnIndex == 0) 28.dp else 8.dp,
                                ),
                                modifier = Modifier.weight(1f)
                                    .animateVisibility(index = 1 + rowIndex * columns + columnIndex),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AndroidToneStrip(table: AndroidColorTable) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(table.title, style = MaterialTheme.typography.titleMedium)
        val tileWidth = 116.dp * LocalDensity.current.fontScale.coerceAtLeast(1f)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(table.colors, key = AndroidColorData::name) { color ->
                ColorSwatch(
                    name = color.name,
                    color = colorResource(color.colorResId),
                    shape = RoundedCornerShape(
                        topStart = if (color == table.colors.first()) 28.dp else 8.dp,
                        bottomStart = if (color == table.colors.first()) 28.dp else 8.dp,
                        topEnd = if (color == table.colors.last()) 28.dp else 8.dp,
                        bottomEnd = if (color == table.colors.last()) 28.dp else 8.dp,
                    ),
                    modifier = Modifier.width(tileWidth).animateVisibility(),
                )
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    hero: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
) {
    val context = LocalContext.current
    val hexString = color.toHexString()
    val copiedMessage = stringResource(R.string.tool_material_colors_copied, hexString)
    val visibleColor = color.compositeOver(MaterialTheme.colorScheme.surface)
    // Pick the higher-contrast ink, including for mid-tones and translucent roles.
    val contentColor = if (visibleColor.luminance() > 0.179f) Color.Black else Color.White
    Surface(
        onClick = {
            context.copyTextToClipboard(label = name, text = hexString) {
                Toast.makeText(context, copiedMessage, Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
    ) {
        Column(
            modifier = Modifier.heightIn(min = if (hero) 140.dp else 104.dp).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(name.uppercase(Locale.ENGLISH), style = MaterialTheme.typography.labelMedium)
            Text(
                hexString,
                style = if (hero) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun androidMaterialYouTables(): List<AndroidColorTable> {
    val accent1Title = stringResource(id = R.string.tool_material_colors_android_accent_1)
    val accent2Title = stringResource(id = R.string.tool_material_colors_android_accent_2)
    val accent3Title = stringResource(id = R.string.tool_material_colors_android_accent_3)
    val neutral1Title = stringResource(id = R.string.tool_material_colors_android_neutral_1)
    val neutral2Title = stringResource(id = R.string.tool_material_colors_android_neutral_2)

    return remember(accent1Title, accent2Title, accent3Title, neutral1Title, neutral2Title) {
        listOf(
            AndroidColorTable(
                title = accent1Title,
                colors = androidColorRamp(
                    android.R.color.system_accent1_0,
                    android.R.color.system_accent1_10,
                    android.R.color.system_accent1_50,
                    android.R.color.system_accent1_100,
                    android.R.color.system_accent1_200,
                    android.R.color.system_accent1_300,
                    android.R.color.system_accent1_400,
                    android.R.color.system_accent1_500,
                    android.R.color.system_accent1_600,
                    android.R.color.system_accent1_700,
                    android.R.color.system_accent1_800,
                    android.R.color.system_accent1_900,
                    android.R.color.system_accent1_1000,
                ),
            ),
            AndroidColorTable(
                title = accent2Title,
                colors = androidColorRamp(
                    android.R.color.system_accent2_0,
                    android.R.color.system_accent2_10,
                    android.R.color.system_accent2_50,
                    android.R.color.system_accent2_100,
                    android.R.color.system_accent2_200,
                    android.R.color.system_accent2_300,
                    android.R.color.system_accent2_400,
                    android.R.color.system_accent2_500,
                    android.R.color.system_accent2_600,
                    android.R.color.system_accent2_700,
                    android.R.color.system_accent2_800,
                    android.R.color.system_accent2_900,
                    android.R.color.system_accent2_1000,
                ),
            ),
            AndroidColorTable(
                title = accent3Title,
                colors = androidColorRamp(
                    android.R.color.system_accent3_0,
                    android.R.color.system_accent3_10,
                    android.R.color.system_accent3_50,
                    android.R.color.system_accent3_100,
                    android.R.color.system_accent3_200,
                    android.R.color.system_accent3_300,
                    android.R.color.system_accent3_400,
                    android.R.color.system_accent3_500,
                    android.R.color.system_accent3_600,
                    android.R.color.system_accent3_700,
                    android.R.color.system_accent3_800,
                    android.R.color.system_accent3_900,
                    android.R.color.system_accent3_1000,
                ),
            ),
            AndroidColorTable(
                title = neutral1Title,
                colors = androidColorRamp(
                    android.R.color.system_neutral1_0,
                    android.R.color.system_neutral1_10,
                    android.R.color.system_neutral1_50,
                    android.R.color.system_neutral1_100,
                    android.R.color.system_neutral1_200,
                    android.R.color.system_neutral1_300,
                    android.R.color.system_neutral1_400,
                    android.R.color.system_neutral1_500,
                    android.R.color.system_neutral1_600,
                    android.R.color.system_neutral1_700,
                    android.R.color.system_neutral1_800,
                    android.R.color.system_neutral1_900,
                    android.R.color.system_neutral1_1000,
                ),
            ),
            AndroidColorTable(
                title = neutral2Title,
                colors = androidColorRamp(
                    android.R.color.system_neutral2_0,
                    android.R.color.system_neutral2_10,
                    android.R.color.system_neutral2_50,
                    android.R.color.system_neutral2_100,
                    android.R.color.system_neutral2_200,
                    android.R.color.system_neutral2_300,
                    android.R.color.system_neutral2_400,
                    android.R.color.system_neutral2_500,
                    android.R.color.system_neutral2_600,
                    android.R.color.system_neutral2_700,
                    android.R.color.system_neutral2_800,
                    android.R.color.system_neutral2_900,
                    android.R.color.system_neutral2_1000,
                ),
            ),
        )
    }
}

private fun androidColorRamp(
    @ColorRes color0: Int,
    @ColorRes color10: Int,
    @ColorRes color50: Int,
    @ColorRes color100: Int,
    @ColorRes color200: Int,
    @ColorRes color300: Int,
    @ColorRes color400: Int,
    @ColorRes color500: Int,
    @ColorRes color600: Int,
    @ColorRes color700: Int,
    @ColorRes color800: Int,
    @ColorRes color900: Int,
    @ColorRes color1000: Int,
): List<AndroidColorData> = listOf(
    AndroidColorData("0", color0),
    AndroidColorData("10", color10),
    AndroidColorData("50", color50),
    AndroidColorData("100", color100),
    AndroidColorData("200", color200),
    AndroidColorData("300", color300),
    AndroidColorData("400", color400),
    AndroidColorData("500", color500),
    AndroidColorData("600", color600),
    AndroidColorData("700", color700),
    AndroidColorData("800", color800),
    AndroidColorData("900", color900),
    AndroidColorData("1000", color1000),
)

