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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storage
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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGrid
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGridItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid.GroupedGridMeasurements
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * The app-details quick actions, drawn as one grouped block.
 *
 * The actions used to be free-floating tiles in a three-column grid. They are peers of one another,
 * so they now read as one block through the toolkit's `GroupedGrid`, which also owns the sponsored
 * row that used to sit loose underneath them.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AppDetailsQuickActions(
    appInfo: AppInfo,
    isFavorite: Boolean,
    isAppInstalled: Boolean?,
    actionLauncher: AppActionLauncher,
    adsConfig: AdsConfig,
    onFavoriteClick: () -> Unit,
) {
    val notificationsLabel: String = stringResource(id = R.string.app_details_notifications)
    val permissionsLabel: String = stringResource(id = R.string.app_details_permissions)
    val storageLabel: String = stringResource(id = R.string.app_details_storage)
    val batteryLabel: String = stringResource(id = R.string.app_details_battery)
    val favoriteLabel: String = stringResource(id = R.string.favorite_apps)
    val playStoreLabel: String = stringResource(id = R.string.app_details_play_store)
    val copyPackageLabel: String = stringResource(id = R.string.app_details_copy_package)

    val quickActions: ImmutableList<GroupedGridItem> = remember(
        appInfo,
        isFavorite,
        isAppInstalled,
        actionLauncher,
        onFavoriteClick,
        notificationsLabel,
        permissionsLabel,
        storageLabel,
        batteryLabel,
        favoriteLabel,
        playStoreLabel,
        copyPackageLabel,
    ) {
        val installed: Boolean = isAppInstalled == true

        listOfNotNull(
            GroupedGridItem(
                title = notificationsLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Notifications),
            ) {
                actionLauncher.openNotifications(appInfo.packageName)
            }.takeIf { installed },
            GroupedGridItem(
                title = permissionsLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Security),
            ) {
                actionLauncher.openPermissions(appInfo.packageName)
            }.takeIf { installed },
            GroupedGridItem(
                title = storageLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Storage),
            ) {
                actionLauncher.openStorage(appInfo.packageName)
            }.takeIf { installed },
            GroupedGridItem(
                title = batteryLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.BatteryFull),
            ) {
                actionLauncher.openBattery(appInfo.packageName)
            }.takeIf { installed },
            GroupedGridItem(
                title = favoriteLabel,
                icon = ToolkitIcon.Vector(
                    imageVector = if (isFavorite) Icons.Outlined.Star else Icons.Default.Star,
                ),
                onClick = onFavoriteClick,
            ),
            GroupedGridItem(
                title = playStoreLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.PlayArrow),
            ) {
                actionLauncher.openPlayStore(appInfo.packageName)
            }.takeIf { installed },
            GroupedGridItem(
                title = copyPackageLabel,
                icon = ToolkitIcon.Vector(imageVector = Icons.Outlined.ContentCopy),
            ) {
                actionLauncher.copyPackageName(appInfo.packageName)
            },
        ).toImmutableList()
    }

    if (quickActions.isEmpty()) return

    val badgeShape: Shape = MaterialShapes.Cookie12Sided.toShape()

    Text(
        text = stringResource(id = R.string.app_details_quick_actions_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
    )
    ExtraSmallVerticalSpacer()
    GroupedGrid(
        items = quickActions,
        modifier = Modifier.padding(horizontal = SizeConstants.LargeSize),
        measurements = GroupedGridMeasurements.Small,
        iconShape = badgeShape,
        adUnitId = adsConfig.bannerAdUnitId,
    )
}
