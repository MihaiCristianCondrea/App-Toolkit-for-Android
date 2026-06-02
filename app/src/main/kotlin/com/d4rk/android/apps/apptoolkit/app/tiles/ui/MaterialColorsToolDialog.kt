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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui

import android.os.Build
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import java.util.Locale

/** Full-height expanded tool that previews the current app and Android Material You palettes. */
@Composable
fun MaterialColorsToolDialog(
    onClose: () -> Unit,
) {
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = onClose,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = SizeConstants.LargeSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.tool_material_colors_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(id = R.string.tool_dialog_close_content_description),
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
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
                    ColorTableCard(table = appColorTable)
                }
                if (androidColorTables.isNotEmpty()) {
                    items(
                        items = androidColorTables,
                        key = AndroidColorTable::title,
                    ) { table ->
                        AndroidColorTableCard(table = table)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorTableCard(table: ColorTable) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SizeConstants.LargeSize),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier.padding(SizeConstants.MediumSize),
            verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        ) {
            Text(
                text = table.title,
                style = MaterialTheme.typography.titleMedium,
            )
            HorizontalDivider()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
                maxItemsInEachRow = 3,
            ) {
                table.colors.forEach { color ->
                    ColorSwatch(
                        name = color.name,
                        color = color.color,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun AndroidColorTableCard(table: AndroidColorTable) {
    val colors = table.colors.map { androidColor ->
        ColorSwatchData(
            name = androidColor.name,
            color = colorResource(id = androidColor.colorResId),
        )
    }
    ColorTableCard(table = ColorTable(title = table.title, colors = colors))
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (color.luminance() > 0.5f) Color.Black else Color.White
    Box(
        modifier = modifier
            .height(ColorSwatchHeight)
            .background(color = color, shape = RoundedCornerShape(SizeConstants.SmallSize))
            .padding(SizeConstants.SmallSize),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize)) {
            Text(
                text = name.uppercase(Locale.ENGLISH),
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
            )
            Text(
                text = color.toHexString(),
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
            )
        }
    }
}

private fun androidx.compose.material3.ColorScheme.toAppColorTable(title: String): ColorTable = ColorTable(
    title = title,
    colors = listOf(
        ColorSwatchData("primary", primary),
        ColorSwatchData("on primary", onPrimary),
        ColorSwatchData("primary container", primaryContainer),
        ColorSwatchData("secondary", secondary),
        ColorSwatchData("tertiary", tertiary),
        ColorSwatchData("error", error),
        ColorSwatchData("background", background),
        ColorSwatchData("surface", surface),
        ColorSwatchData("surface variant", surfaceVariant),
        ColorSwatchData("outline", outline),
        ColorSwatchData("inverse surface", inverseSurface),
        ColorSwatchData("scrim", scrim),
    ),
)

private fun Color.toHexString(): String = String.format(Locale.US, "#%08X", toArgb())

@Immutable
private data class ColorSwatchData(
    val name: String,
    val color: Color,
)

@Immutable
private data class ColorTable(
    val title: String,
    val colors: List<ColorSwatchData>,
)

@Immutable
private data class AndroidColorData(
    val name: String,
    @ColorRes val colorResId: Int,
)

@Immutable
private data class AndroidColorTable(
    val title: String,
    val colors: List<AndroidColorData>,
)

@Composable
private fun androidMaterialYouTables(): List<AndroidColorTable> = listOf(
    AndroidColorTable(
        title = stringResource(id = R.string.tool_material_colors_android_accent_1),
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
        title = stringResource(id = R.string.tool_material_colors_android_accent_2),
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
        title = stringResource(id = R.string.tool_material_colors_android_accent_3),
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
        title = stringResource(id = R.string.tool_material_colors_android_neutral_1),
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
        title = stringResource(id = R.string.tool_material_colors_android_neutral_2),
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

private val ColorSwatchHeight = 72.dp
