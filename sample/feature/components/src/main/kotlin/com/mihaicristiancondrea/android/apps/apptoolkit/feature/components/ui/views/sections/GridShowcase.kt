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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SnippetFolder
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGrid
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGridItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGridMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Demonstrates `GroupedGrid`: the grouped corners, the per-cell `MaterialShapes` badges, the size
 * classes, and the single-line variant.
 *
 * No ad unit is passed, so the showcase never asks for the grid's sponsored row. That row is shown
 * where it belongs, in the app-details quick actions.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GridShowcase() {
    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_grid),
        icon = Icons.Outlined.GridView,
    )

    val installedAppsLabel: String = stringResource(id = R.string.components_grid_installed_apps)
    val imagesLabel: String = stringResource(id = R.string.components_grid_images)
    val musicLabel: String = stringResource(id = R.string.components_grid_music)
    val documentsLabel: String = stringResource(id = R.string.components_grid_documents)
    val downloadsLabel: String = stringResource(id = R.string.components_grid_downloads)

    // `toShape` is composable, so the silhouettes are resolved here and handed to the cells rather
    // than built inside the remembered list.
    val installedAppsShape: Shape = MaterialShapes.Cookie9Sided.toShape()
    val imagesShape: Shape = MaterialShapes.Flower.toShape()
    val musicShape: Shape = MaterialShapes.Gem.toShape()
    val documentsShape: Shape = MaterialShapes.Cookie12Sided.toShape()
    val downloadsShape: Shape = MaterialShapes.SoftBurst.toShape()

    val categories: ImmutableList<GroupedGridItem> = remember(
        installedAppsLabel,
        imagesLabel,
        musicLabel,
        documentsLabel,
        downloadsLabel,
        installedAppsShape,
        imagesShape,
        musicShape,
        documentsShape,
        downloadsShape,
    ) {
        persistentListOf(
            GroupedGridItem(
                title = installedAppsLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Apps),
                subtitle = "12.4 GB",
                iconShape = installedAppsShape,
                onClick = {},
            ),
            GroupedGridItem(
                title = imagesLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Image),
                subtitle = "3.1 GB",
                iconShape = imagesShape,
                onClick = {},
            ),
            GroupedGridItem(
                title = musicLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.MusicNote),
                subtitle = "820 MB",
                iconShape = musicShape,
                onClick = {},
            ),
            GroupedGridItem(
                title = documentsLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.SnippetFolder),
                subtitle = "216 MB",
                iconShape = documentsShape,
                onClick = {},
            ),
            GroupedGridItem(
                title = downloadsLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Download),
                subtitle = "94 MB",
                iconShape = downloadsShape,
                onClick = {},
            ),
        )
    }

    // The same cells without their supporting line, at the smallest size class.
    val compactActions: ImmutableList<GroupedGridItem> = remember(categories) {
        categories.take(n = 3).map { item -> item.copy(subtitle = null) }.toImmutableList()
    }
    val singleCell: ImmutableList<GroupedGridItem> = remember(categories) {
        persistentListOf(categories.first())
    }

    ShowcaseSection {
        GridGroupLabel(text = stringResource(id = R.string.components_grid_group_categories))
        GroupedGrid(items = categories)

        SmallVerticalSpacer()
        GridGroupLabel(text = stringResource(id = R.string.components_grid_group_compact))
        GroupedGrid(
            items = compactActions,
            measurements = GroupedGridMeasurements.ExtraSmall,
        )

        SmallVerticalSpacer()
        GridGroupLabel(text = stringResource(id = R.string.components_grid_group_single))
        GroupedGrid(items = singleCell)
    }
}

@Composable
private fun GridGroupLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = SizeConstants.SmallSize),
    )
}
