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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackScreenState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.TrackScreenView
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.contentPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R

/** Describes permissions. Text resolves against the current UI locale on every configuration change. */
@Composable
fun PermissionsScreen() {
    TrackScreenView(screenName = "Permissions", screenClass = "PermissionsScreen")
    val config = permissionsConfig()
    TrackScreenState(screenName = "Permissions", state = Loadable.Ready(config))
    PermissionsContent(paddingValues = contentPadding(), settingsConfig = config)
}

@Composable
internal fun permissionsConfig(): SettingsConfig {
    return SettingsConfig(
        title = stringResource(R.string.permissions),
        categories = listOf(
            SettingsCategory(
                title = stringResource(R.string.normal),
                preferences = listOf(
                    SettingsPreference(
                        title = stringResource(R.string.access_network_state),
                        summary = stringResource(R.string.summary_preference_permissions_access_network_state),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.ad_id),
                        summary = stringResource(R.string.summary_preference_permissions_ad_id),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.billing),
                        summary = stringResource(R.string.summary_preference_permissions_billing),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.check_license),
                        summary = stringResource(R.string.summary_preference_permissions_check_license),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.foreground_service),
                        summary = stringResource(R.string.summary_preference_permissions_foreground_service),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.internet),
                        summary = stringResource(R.string.summary_preference_permissions_internet),
                    ),
                    SettingsPreference(
                        title = stringResource(R.string.wake_lock),
                        summary = stringResource(R.string.summary_preference_permissions_wake_lock),
                    ),
                ),
            ),
            SettingsCategory(
                title = stringResource(R.string.runtime),
                preferences = listOf(
                    SettingsPreference(
                        title = stringResource(R.string.post_notifications),
                        summary = stringResource(R.string.summary_preference_permissions_post_notifications),
                    ),
                ),
            ),
        ),
    )
}

/** The permission catalog: one titled group per category. */
@Composable
fun PermissionsContent(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        settingsConfig.categories.forEachIndexed { categoryIndex, category ->
            val categoryKey = category.title ?: "category_$categoryIndex"

            item(
                key = "permission_category_$categoryKey",
                contentType = "permission_category",
            ) {
                category.title?.let { title ->
                    PreferenceCategoryItem(title = title)
                }
            }

            itemsIndexed(
                items = category.preferences,
                key = { index, preference ->
                    val preferenceKey = preference.key ?: preference.title ?: index
                    "permission_${categoryKey}_$preferenceKey"
                },
                contentType = { _, _ -> "permission_preference" },
            ) { index, preference ->
                SettingsPreferenceItem(
                    title = preference.title,
                    summary = preference.summary,
                    onClick = { preference.action?.invoke() },
                    modifier = Modifier.groupedPreferenceItem(
                        position = groupedItemPosition(
                            index = index,
                            size = category.preferences.size
                        ),
                        outerRadius = SizeConstants.LargeMediumSize,
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PermissionsContentPreview() {
    MaterialTheme {
        PermissionsContent(paddingValues = PaddingValues(), settingsConfig = permissionsConfig())
    }
}
