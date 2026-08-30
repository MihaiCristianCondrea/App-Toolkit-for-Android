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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.models.AppDetailsQuickActionUi
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer

@Composable
internal fun AppDetailsQuickActions(
    appInfo: AppInfo,
    isFavorite: Boolean,
    isAppInstalled: Boolean?,
    actionLauncher: AppActionLauncher,
    onFavoriteClick: () -> Unit,
) {
    val quickActions =
        remember(appInfo, isFavorite, isAppInstalled, actionLauncher, onFavoriteClick) {
            listOfNotNull(
                AppDetailsQuickActionUi(
                    R.string.app_details_notifications,
                    Icons.Outlined.Notifications,
                ) {
                    actionLauncher.openNotifications(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                AppDetailsQuickActionUi(
                    R.string.app_details_permissions,
                    Icons.Outlined.Security,
                ) {
                    actionLauncher.openPermissions(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                AppDetailsQuickActionUi(
                    R.string.app_details_storage,
                    Icons.Outlined.Storage,
                ) {
                    actionLauncher.openStorage(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                AppDetailsQuickActionUi(
                    R.string.app_details_battery,
                    Icons.Outlined.BatteryFull,
                ) {
                    actionLauncher.openBattery(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                AppDetailsQuickActionUi(
                    labelRes = R.string.favorite_apps,
                    icon = if (isFavorite) Icons.Outlined.Star else Icons.Default.Star,
                    onClick = onFavoriteClick,
                ),
                AppDetailsQuickActionUi(
                    R.string.app_details_play_store,
                    Icons.Outlined.PlayArrow,
                ) {
                    actionLauncher.openPlayStore(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                AppDetailsQuickActionUi(
                    R.string.app_details_copy_package,
                    Icons.Outlined.ContentCopy,
                ) {
                    actionLauncher.copyPackageName(appInfo.packageName)
                },
            )
        }

    Text(
        text = stringResource(id = R.string.app_details_quick_actions_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
    )
    ExtraSmallVerticalSpacer()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        quickActions.chunked(QUICK_ACTION_COLUMNS).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
            ) {
                rowActions.forEach { quickAction ->
                    QuickActionTile(
                        quickAction = quickAction,
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(QUICK_ACTION_COLUMNS - rowActions.size) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun QuickActionTile(
    quickAction: AppDetailsQuickActionUi,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = quickAction.onClick,
        modifier = modifier.height(SizeConstants.NinetySixSize),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(SizeConstants.MediumSize),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = SizeConstants.SmallSize,
                    alignment = Alignment.CenterVertically,
                ),
            ) {
                Icon(imageVector = quickAction.icon, contentDescription = null)
                Text(
                    text = stringResource(id = quickAction.labelRes),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private const val QUICK_ACTION_COLUMNS: Int = 3
