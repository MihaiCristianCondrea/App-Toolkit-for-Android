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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid

import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SnippetFolder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdBadgeShape
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.ButtonFeedback
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * A block of category cells laid out as one rounded group.
 *
 * The group is the unit, not the cell: only the corners at the outside of the block are rounded at
 * [outerRadius], every corner where two cells meet is cut at [innerRadius], and the cells are
 * separated by [itemSpacing] rather than floating as separate cards. It is the layout a storage
 * breakdown or a media summary is read in: a badge, what the row is, and how big it is. It works
 * just as well for a block of actions, where a cell has no subtitle.
 *
 * What a caller chooses:
 * - [measurements] sets the height of a cell and everything that scales with it, the way a size
 *   class does for a button.
 * - [outerRadius] and [innerRadius] set how hard the group and its seams are cut.
 * - [iconShape] cuts the icon badge, and a [GroupedGridItem] may override it per cell. Any [Shape]
 *   works, so `MaterialShapes.Cookie9Sided.toShape()` and the rest of the Material 3 shape set are
 *   available without the toolkit shipping artwork of its own.
 * - [columns] sets how many cells share a row. A last row that is short shares its width between
 *   the cells it does have, so a lone leftover item spans the row.
 *
 * Passing an [adUnitId] adds one native ad row spanning the full width, in the middle of the block,
 * from [GroupedGridDefaults.MinItemsForAd] items up. The ad is part of the group: it is cut with the
 * same radii, and takes the same badge, padding and headline size as the cells, so it reads as one
 * more row rather than as something dropped between them. A grid of one cell never shows one, and
 * neither does a grid whose ad fails to load, where the cells round off exactly as they would have
 * without it. Placement policy stays with the host, which decides whether to pass a unit id at all.
 *
 * @param items Cells to render, in order. An empty list renders nothing.
 * @param bounceOnClick `true` scales a cell down while it is held. Off by default: a grid is read as
 * one block, and a cell that shrinks on its own breaks the block apart.
 * @param adUnitId AdMob native ad unit for the ad row. `null` or blank renders no ad row.
 */
@Composable
fun GroupedGrid(
    items: ImmutableList<GroupedGridItem>,
    modifier: Modifier = Modifier,
    columns: Int = GroupedGridDefaults.Columns,
    measurements: GroupedGridMeasurements = GroupedGridDefaults.Measurements,
    outerRadius: Dp = GroupedGridDefaults.OuterRadius,
    innerRadius: Dp = GroupedGridDefaults.InnerRadius,
    itemSpacing: Dp = GroupedGridDefaults.ItemSpacing,
    iconShape: Shape = GroupedGridDefaults.IconShape,
    colors: GroupedGridColors = GroupedGridDefaults.colors(),
    bounceOnClick: Boolean = false,
    adUnitId: String? = null,
) {
    if (items.isEmpty()) return

    val carriesAd: Boolean = !adUnitId.isNullOrBlank() &&
            items.size >= GroupedGridDefaults.MinItemsForAd
    var adLoaded: Boolean by remember(adUnitId) { mutableStateOf(value = false) }
    // No ad can load in a preview or the layout inspector, where the slot draws a placeholder
    // instead. Treating that as a loaded ad keeps the previewed group cut the way a real one is.
    val adOnScreen: Boolean = adLoaded || LocalInspectionMode.current

    val adRow: GroupedGridAdRow = when {
        !carriesAd -> GroupedGridAdRow.None
        adOnScreen -> GroupedGridAdRow.Visible
        else -> GroupedGridAdRow.Collapsed
    }
    val rows: ImmutableList<GroupedGridRow> = remember(items.size, columns, adRow) {
        groupedGridRows(itemCount = items.size, columns = columns, adRow = adRow)
    }
    val adPresentation: NativeAdPresentation.GridRow =
        remember(measurements) { measurements.nativeAdPresentation() }
    // The ad badge is filled with the same silhouette the cells cut theirs from, flattened to a
    // path because the ad's icon lives in a real `NativeAdView` rather than in Compose.
    val adStyle: NativeAdStyle = measurements.nativeAdStyle(
        titleTextStyle = measurements.titleTextStyle(),
        colors = colors,
        badgeShape = rememberNativeAdBadgeShape(
            shape = iconShape,
            size = measurements.iconContainerSize,
        ),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        var precededByVisibleRow = false

        rows.forEach { row ->
            // Keyed so the ad slot keeps its identity, and its in-flight request, while the rows
            // around it are re-cut. Composed positionally it would be torn down and restarted the
            // moment an ad arrived and the plan grew a row, which never settles.
            key(row.key) {
                if (!row.collapsed && precededByVisibleRow) {
                    Spacer(modifier = Modifier.height(height = itemSpacing))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = itemSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    row.cells.forEach { cell ->
                        val shape: Shape = cell.corners.cornerShape(
                            outerRadius = outerRadius,
                            innerRadius = innerRadius,
                        )

                        if (cell.isAd) {
                            GroupedGridAdCell(
                                adUnitId = adUnitId.orEmpty(),
                                shape = shape,
                                visible = !row.collapsed,
                                presentation = adPresentation,
                                adStyle = adStyle,
                                colors = colors,
                                onAdLoaded = { adLoaded = it },
                            )
                        } else {
                            GroupedGridCard(
                                item = items[cell.itemIndex],
                                shape = shape,
                                measurements = measurements,
                                iconShape = iconShape,
                                colors = colors,
                                bounceOnClick = bounceOnClick,
                            )
                        }
                    }
                }
            }

            precededByVisibleRow = precededByVisibleRow || !row.collapsed
        }
    }
}

/** One cell of the group: the icon badge, the title, and the optional supporting line. */
@Composable
private fun RowScope.GroupedGridCard(
    item: GroupedGridItem,
    shape: Shape,
    measurements: GroupedGridMeasurements,
    iconShape: Shape,
    colors: GroupedGridColors,
    bounceOnClick: Boolean,
) {
    val view: View = LocalView.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val feedback: ButtonFeedback = remember { ButtonFeedback() }

    Card(
        modifier = Modifier
            .weight(weight = 1f)
            .bounceClick(animationEnabled = bounceOnClick && item.enabled),
        onClick = {
            feedback.performClick(view = view, hapticFeedback = hapticFeedback)
            item.onClick()
        },
        enabled = item.enabled,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = measurements.cellHeight)
                .padding(paddingValues = measurements.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(size = measurements.iconContainerSize)
                    .clip(shape = item.iconShape ?: iconShape)
                    .background(
                        color = if (item.iconContainerColor.isSpecified) {
                            item.iconContainerColor
                        } else {
                            colors.iconContainerColor
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                ToolkitIconContent(
                    icon = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(size = measurements.iconSize),
                    tint = if (item.iconContentColor.isSpecified) {
                        item.iconContentColor
                    } else {
                        colors.iconContentColor
                    },
                )
            }

            Spacer(modifier = Modifier.width(width = measurements.iconSpacing))

            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    style = measurements.titleTextStyle(),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
                item.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = measurements.subtitleTextStyle(),
                        color = colors.subtitleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/**
 * The group's ad row.
 *
 * The slot is composed whether or not an ad is on screen, because that is what lets it load one;
 * until it has, it renders nothing and the transparent surface around it collapses to no height.
 */
@Composable
private fun RowScope.GroupedGridAdCell(
    adUnitId: String,
    shape: Shape,
    visible: Boolean,
    presentation: NativeAdPresentation.GridRow,
    adStyle: NativeAdStyle,
    colors: GroupedGridColors,
    onAdLoaded: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.weight(weight = 1f),
        shape = shape,
        color = if (visible) colors.containerColor else Color.Transparent,
        contentColor = colors.contentColor,
    ) {
        NativeAdSlot(
            adUnitId = adUnitId,
            presentation = presentation,
            modifier = Modifier.fillMaxWidth(),
            showContainer = false,
            style = adStyle,
            onAdLoaded = onAdLoaded,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
private fun GroupedGridPreview() {
    val items: ImmutableList<GroupedGridItem> = persistentListOf(
        GroupedGridItem(
            title = "Installed apps",
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Apps),
            subtitle = "12.4 GB",
            iconShape = MaterialShapes.Cookie9Sided.toShape(),
            onClick = {},
        ),
        GroupedGridItem(
            title = "Images",
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Image),
            subtitle = "3.1 GB",
            iconShape = MaterialShapes.Flower.toShape(),
            onClick = {},
        ),
        GroupedGridItem(
            title = "Music",
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.MusicNote),
            subtitle = "820 MB",
            iconShape = MaterialShapes.Gem.toShape(),
            onClick = {},
        ),
        GroupedGridItem(
            title = "Documents",
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.SnippetFolder),
            subtitle = "216 MB",
            iconShape = MaterialShapes.Cookie12Sided.toShape(),
            onClick = {},
        ),
        GroupedGridItem(
            title = "Downloads",
            icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Download),
            subtitle = "94 MB",
            onClick = {},
        ),
    )

    MaterialTheme {
        GroupedGrid(
            items = items,
            modifier = Modifier.padding(all = SizeConstants.LargeSize),
        )
    }
}
