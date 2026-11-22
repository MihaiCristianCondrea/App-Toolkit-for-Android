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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItemsIndexed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.constants.PermissionKeys

/** Describes permissions. Text resolves against the current UI locale on every configuration change. */
@Composable
fun PermissionsScreen() {
    // A fixed catalogue: nothing loads, so the screen reports its visits and no status.
    ScreenHost(screen = PERMISSIONS_SCREEN) {
        PermissionsContent(
            paddingValues = contentPadding(),
            settingsConfig = permissionsConfig(),
        )
    }
}

private val PERMISSIONS_SCREEN: TrackedScreen = TrackedScreen(
    name = "Permissions",
    className = "PermissionsScreen",
)

@Composable
internal fun permissionsConfig(): SettingsConfig {
    return SettingsConfig(
        title = stringResource(id = R.string.permissions),
        categories = listOf(
            SettingsCategory(
                title = stringResource(id = R.string.normal),
                preferences = listOf(
                    SettingsPreference(
                        key = PermissionKeys.ACCESS_NETWORK_STATE,
                        title = stringResource(id = R.string.access_network_state),
                        summary = stringResource(id = R.string.summary_preference_permissions_access_network_state),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.AD_ID,
                        title = stringResource(id = R.string.ad_id),
                        summary = stringResource(id = R.string.summary_preference_permissions_ad_id),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.BILLING,
                        title = stringResource(id = R.string.billing),
                        summary = stringResource(id = R.string.summary_preference_permissions_billing),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.CHECK_LICENSE,
                        title = stringResource(id = R.string.check_license),
                        summary = stringResource(id = R.string.summary_preference_permissions_check_license),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.FOREGROUND_SERVICE,
                        title = stringResource(id = R.string.foreground_service),
                        summary = stringResource(id = R.string.summary_preference_permissions_foreground_service),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.INTERNET,
                        title = stringResource(id = R.string.internet),
                        summary = stringResource(id = R.string.summary_preference_permissions_internet),
                    ),
                    SettingsPreference(
                        key = PermissionKeys.WAKE_LOCK,
                        title = stringResource(id = R.string.wake_lock),
                        summary = stringResource(id = R.string.summary_preference_permissions_wake_lock),
                    ),
                ),
            ),
            SettingsCategory(
                title = stringResource(id = R.string.runtime),
                preferences = listOf(
                    SettingsPreference(
                        key = PermissionKeys.POST_NOTIFICATIONS,
                        title = stringResource(id = R.string.post_notifications),
                        summary = stringResource(id = R.string.summary_preference_permissions_post_notifications),
                    ),
                ),
            ),
        ),
    )
}

/**
 * Renders the grouped permission catalogue from [settingsConfig].
 *
 * Rows without an `action` are displayed as non-clickable descriptive cards.
 *
 * @param paddingValues Content padding supplied by the host scaffold.
 * @param settingsConfig Permission categories and rows to render.
 * @param modifier Optional modifier applied to the root list.
 */
@Composable
fun PermissionsContent(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        settingsConfig.categories.forEachIndexed { categoryIndex, category ->
            category.title?.let { title ->
                animatedItem(
                    key = "permission_category_$categoryIndex",
                    contentType = "permission_category",
                ) {
                    PreferenceCategoryItem(title = title)
                }
            }

            animatedItemsIndexed(
                items = category.preferences,
                key = { index, preference ->
                    val preferenceKey = preference.key ?: index
                    "permission_${categoryIndex}_$preferenceKey"
                },
                contentType = { _, _ -> "permission_preference" },
            ) { index, preference ->
                SettingsPreferenceItem(
                    icon = preference.icon,
                    useIconContainer = preference.useIconContainer,
                    iconColor = preference.iconColor,
                    iconContainerColor = preference.iconContainerColor,
                    title = preference.title,
                    summary = preference.summary,
                    onClick = preference.action?.let { action -> { action() } },
                    modifier = Modifier.groupedPreferenceItem(
                        position = groupedItemPosition(
                            index = index,
                            size = category.preferences.size,
                        ),
                        outerRadius = SizeConstants.LargeMediumSize,
                    ),
                )
            }
        }
    }
}
