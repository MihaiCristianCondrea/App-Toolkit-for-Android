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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppLink
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AppDetailsNativeAd
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumHorizontalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.model.AppVersionInfo as InstalledAppVersionInfo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppDetailsBottomSheet(
    appInfo: AppInfo,
    appDetails: AppDetails?,
    isDetailsLoading: Boolean,
    hasDetailsError: Boolean,
    isFavorite: Boolean,
    isAppInstalled: Boolean?,
    installedVersionInfo: InstalledAppVersionInfo?,
    actionLauncher: AppActionLauncher,
    onFavoriteClick: () -> Unit,
    onRetryDetails: () -> Unit,
    adsConfig: AdsConfig,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LargeVerticalSpacer()
        AppDetailsHeader(
            appInfo = appInfo,
        )
        LargeVerticalSpacer()
        AppDetailsActions(
            appInfo = appInfo,
            isAppInstalled = isAppInstalled,
            actionLauncher = actionLauncher,
        )
        LargeVerticalSpacer()
        AppMetadataChips(
            appInfo = appInfo,
            appDetails = appDetails,
            isAppInstalled = isAppInstalled,
            installedVersionInfo = installedVersionInfo,
        )
        LargeVerticalSpacer()
        AppDetailsNativeAd(
            modifier = Modifier.fillMaxWidth(),
            adsConfig = adsConfig
        )
        LargeVerticalSpacer()
        QuickActionsPanel(
            appInfo = appInfo,
            isFavorite = isFavorite,
            isAppInstalled = isAppInstalled,
            actionLauncher = actionLauncher,
            onFavoriteClick = onFavoriteClick,
        )

        AppDetailsNativeAd(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SizeConstants.LargeSize),
            adsConfig = adsConfig
        )

        when {
            isDetailsLoading -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.ExtraLargeSize),
                contentAlignment = Alignment.Center,
            ) {
                CircularWavyProgressIndicator()
            }

            hasDetailsError -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SizeConstants.LargeSize),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
            ) {
                Text(text = stringResource(id = R.string.error_failed_to_load_apps))
                GeneralButton(
                    onClick = onRetryDetails,
                    label = stringResource(id = com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R.string.try_again),
                )
            }
        }
        if (appDetails?.description?.isNotEmpty() == true) {
            AppSection(
                title = stringResource(id = R.string.app_details_about_title),
                icon = Icons.Outlined.Info,
            ) {
                Text(
                    text = appDetails.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        if (!appDetails?.screenshots.isNullOrEmpty()) {
            LargeVerticalSpacer()
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.app_details_screenshots_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
                )
                LargeVerticalSpacer()
                LazyRow(
                    contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
                    horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize)
                ) {
                    items(appDetails.screenshots) { screenshot ->
                        Card(shape = RoundedCornerShape(SizeConstants.LargeSize)) {
                            AsyncImage(
                                model = screenshot.url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(SizeConstants.TwoHundredFortySize)
                                    .aspectRatio(screenshot.aspectRatio.toDisplayRatio())
                                    .clip(RoundedCornerShape(SizeConstants.LargeSize))
                            )
                        }
                    }
                }
            }
        }
        appDetails?.let { details ->
            AppLinksSection(links = details.links, actionLauncher = actionLauncher)
        }
        LargeVerticalSpacer()
    }
}

@Composable
private fun AppDetailsHeader(
    appInfo: AppInfo,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)
    ) {
        Card(shape = RoundedCornerShape(SizeConstants.LargeSize)) {
            AsyncImage(
                model = appInfo.iconUrl,
                contentDescription = appInfo.name,
                modifier = Modifier.size(SizeConstants.LauncherIconSize * 2)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = appInfo.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            appInfo.shortDescription.takeIf(String::isNotBlank)?.let { shortDescription ->
                Text(
                    text = shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun AppDetailsActions(
    appInfo: AppInfo,
    isAppInstalled: Boolean?,
    actionLauncher: AppActionLauncher,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (isAppInstalled) {
            true -> {
                GeneralOutlinedButton(
                    onClick = { actionLauncher.shareApp(appInfo.packageName, appInfo.name) },
                    vectorIcon = Icons.Outlined.Share,
                    label = stringResource(id = R.string.app_details_share),
                    modifier = Modifier.weight(1f)
                )
                GeneralButton(
                    onClick = { actionLauncher.openApp(appInfo.packageName) },
                    vectorIcon = Icons.AutoMirrored.Outlined.OpenInNew,
                    label = stringResource(id = R.string.app_details_open_app),
                    modifier = Modifier.weight(1f)
                )
            }

            false -> {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.get_it_on_google_play),
                    contentDescription = stringResource(R.string.app_details_view_on_play_store),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(SizeConstants.LauncherIconSize)
                        .bounceClick()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { actionLauncher.openPlayStore(appInfo.packageName) }
                )
            }

            null -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularWavyProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun AppMetadataChips(
    appInfo: AppInfo,
    appDetails: AppDetails?,
    isAppInstalled: Boolean?,
    installedVersionInfo: InstalledAppVersionInfo?,
) {
    val chipItems = listOfNotNull(
        AppInfoChipUi(
            icon = Icons.Outlined.CheckBox,
            label = when (isAppInstalled) {
                true -> stringResource(id = R.string.app_details_installed)
                false -> stringResource(id = R.string.app_details_not_installed)
                null -> stringResource(id = R.string.app_details_checking_install_state)
            }
        ),
        appInfo.category?.label?.takeIf { it.isNotBlank() }?.let { category ->
            AppInfoChipUi(icon = Icons.Outlined.Category, label = category)
        },
        installedVersionInfo?.versionName?.takeIf { it.isNotBlank() }?.let { versionName ->
            AppInfoChipUi(
                icon = Icons.Outlined.Verified,
                label = stringResource(id = R.string.app_details_version, versionName)
            )
        },
        appDetails?.latestVersion?.versionName?.takeIf { it.isNotBlank() }?.let { versionName ->
            AppInfoChipUi(icon = Icons.Outlined.Verified, label = versionName)
        },
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
    ) {
        items(chipItems) { chipItem ->
            AppInfoChip(icon = chipItem.icon, label = chipItem.label)
        }
    }
}

@Composable
private fun AppInfoChip(
    icon: ImageVector,
    label: String,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        shape = RoundedCornerShape(SizeConstants.ExtraLargeSize),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = SizeConstants.MediumSize,
                vertical = SizeConstants.SmallSize,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun QuickActionsPanel(
    appInfo: AppInfo,
    isFavorite: Boolean,
    isAppInstalled: Boolean?,
    actionLauncher: AppActionLauncher,
    onFavoriteClick: () -> Unit,
) {
    val quickActions =
        remember(appInfo, isFavorite, isAppInstalled, actionLauncher, onFavoriteClick) {
            listOfNotNull(
                QuickActionUi(R.string.app_details_notifications, Icons.Outlined.Notifications) {
                    actionLauncher.openNotifications(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                QuickActionUi(R.string.app_details_permissions, Icons.Outlined.Security) {
                    actionLauncher.openPermissions(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                QuickActionUi(R.string.app_details_storage, Icons.Outlined.Storage) {
                    actionLauncher.openStorage(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                QuickActionUi(R.string.app_details_battery, Icons.Outlined.BatteryFull) {
                    actionLauncher.openBattery(appInfo.packageName)
                }.takeIf { isAppInstalled == true },
                QuickActionUi(
                    labelRes = R.string.favorite_apps,
                    icon = if (isFavorite) Icons.Outlined.Star else Icons.Default.Star,
                    onClick = onFavoriteClick,
                ),
                QuickActionUi(R.string.app_details_play_store, Icons.Outlined.PlayArrow) {
                    actionLauncher.openPlayStore(appInfo.packageName)
                },
                QuickActionUi(R.string.app_details_copy_package, Icons.Outlined.ContentCopy) {
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
            .padding(horizontal = SizeConstants.LargeSize)
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
    quickAction: QuickActionUi,
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

@Composable
private fun AppSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    LargeVerticalSpacer()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SizeConstants.LargeSize),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null)
            MediumHorizontalSpacer()
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        ExtraSmallVerticalSpacer()
        content()
    }
}

@Composable
private fun AppLinksSection(
    links: List<AppLink>,
    actionLauncher: AppActionLauncher,
) {
    if (links.isEmpty()) return

    AppSection(
        title = stringResource(id = R.string.app_details_links_title),
        icon = Icons.AutoMirrored.Outlined.OpenInNew,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize)) {
            links.forEach { link ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick()
                        .clickable { actionLauncher.openUrl(link.url) }
                        .padding(vertical = SizeConstants.SmallSize),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null)
                    MediumHorizontalSpacer()
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = link.label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

private const val QUICK_ACTION_COLUMNS: Int = 3

@Immutable
private data class AppInfoChipUi(
    val icon: ImageVector,
    val label: String,
)

@Immutable
private data class QuickActionUi(
    val labelRes: Int,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

private fun String.toDisplayRatio(): Float {
    val parts = split(':', limit = 2)
    val width = parts.getOrNull(0)?.toFloatOrNull()
    val height = parts.getOrNull(1)?.toFloatOrNull()
    return if (width != null && height != null && width > 0f && height > 0f) {
        width / height
    } else {
        9f / 16f
    }
}
