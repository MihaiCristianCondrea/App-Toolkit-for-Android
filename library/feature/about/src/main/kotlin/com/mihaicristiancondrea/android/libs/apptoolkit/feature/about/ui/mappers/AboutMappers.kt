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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.mappers

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.UiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.contracts.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.formatters.formatAboutBuildVersion
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR

/** Maps About metadata to rows and assigns grouped positions after filtering unavailable values. */
internal fun AboutInfo.toAboutItems(): ImmutableList<AboutItem> {
    val appInfoPreferences = buildList {
        val appNameTitle = UiText.StringResource(resourceId = CommonR.string.app_full_name)
        add(
            AboutItem.Preference(
                key = AboutItemKey.APP_NAME,
                title = appNameTitle,
                summary = UiText.StringResource(resourceId = CommonR.string.copyright),
                action = AboutItemAction.CopyToClipboard(
                    label = appNameTitle,
                    text = appNameTitle,
                ),
            )
        )
        add(
            AboutItem.Preference(
                key = AboutItemKey.APP_BUILD_VERSION,
                title = UiText.StringResource(resourceId = R.string.app_build_version),
                summary = UiText.DynamicString(
                    content = formatAboutBuildVersion(versionName = appVersion, versionCode = appVersionCode),
                ),
                countsVersionTap = true,
            )
        )
        if (appToolkitVersion.isNotBlank()) {
            val title = UiText.StringResource(resourceId = R.string.app_toolkit_version)
            add(
                AboutItem.Preference(
                    key = AboutItemKey.APP_TOOLKIT_VERSION,
                    title = title,
                    summary = UiText.DynamicString(content = appToolkitVersion),
                    action = AboutItemAction.CopyToClipboard(
                        label = title,
                        text = UiText.DynamicString(content = appToolkitVersion),
                    ),
                )
            )
        }
        if (!googlePlayServicesVersion.isNullOrBlank()) {
            val title = UiText.StringResource(resourceId = R.string.google_play_services_version)
            add(
                AboutItem.Preference(
                    key = AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION,
                    title = title,
                    summary = UiText.DynamicString(content = googlePlayServicesVersion),
                    action = AboutItemAction.CopyToClipboard(
                        label = title,
                        text = UiText.DynamicString(content = googlePlayServicesVersion),
                    ),
                )
            )
        }
        add(
            AboutItem.Preference(
                key = AboutItemKey.OSS_LICENSES,
                title = UiText.StringResource(resourceId = R.string.oss_license_title),
                summary = UiText.StringResource(resourceId = R.string.summary_preference_settings_oss),
                action = AboutItemAction.OpenLicenses,
            )
        )
    }.assignPositions()

    val deviceInfoPreferences = buildList {
        if (deviceInfo.isNotBlank()) {
            val title = UiText.StringResource(resourceId = R.string.device_info)
            add(
                AboutItem.Preference(
                    key = AboutItemKey.DEVICE_INFO,
                    title = title,
                    summary = UiText.DynamicString(content = deviceInfo),
                    action = AboutItemAction.CopyToClipboard(
                        label = title,
                        text = UiText.DynamicString(content = deviceInfo),
                        successMessage = UiText.StringResource(
                            resourceId = R.string.snack_device_info_copied,
                        ),
                    ),
                )
            )
        }
    }.assignPositions()

    return buildList {
        add(
            AboutItem.Header(
                key = AboutItemKey.HEADER_APP_INFO,
                title = UiText.StringResource(resourceId = R.string.app_info),
            )
        )
        addAll(elements = appInfoPreferences)
        if (deviceInfoPreferences.isNotEmpty()) {
            add(
                AboutItem.Header(
                    key = AboutItemKey.HEADER_DEVICE_INFO,
                    title = UiText.StringResource(resourceId = R.string.device_info),
                )
            )
            addAll(elements = deviceInfoPreferences)
        }
    }.toImmutableList()
}

private fun List<AboutItem.Preference>.assignPositions(): List<AboutItem.Preference> =
    mapIndexed { index, item ->
        item.copy(position = groupedItemPosition(index = index, size = size))
    }
