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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.models.AboutItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.states.AboutUiState

/**
 * Maps the [AboutInfo] domain model to the [AboutUiState] rendered by the About screen.
 *
 * Entries that have nothing to show are dropped, so grouped card positions are assigned after
 * filtering and always describe the list the user actually sees.
 */
internal fun AboutInfo.toUiState(): AboutUiState = AboutUiState(items = toAboutItems())

private fun AboutInfo.toAboutItems(): List<AboutItem> {
    val appInfoPreferences = buildList {
        add(
            AboutItem.Preference(
                key = AboutItemKey.APP_NAME,
                title = UiTextHelper.StringResource(CommonR.string.app_full_name),
                summary = UiTextHelper.StringResource(CommonR.string.copyright),
            )
        )
        add(
            AboutItem.Preference(
                key = AboutItemKey.APP_BUILD_VERSION,
                title = UiTextHelper.StringResource(R.string.app_build_version),
                summary = UiTextHelper.DynamicString("$appVersion ($appVersionCode)"),
                action = AboutItemAction.VersionEasterEgg,
            )
        )
        if (appToolkitVersion.isNotBlank()) {
            add(
                AboutItem.Preference(
                    key = AboutItemKey.APP_TOOLKIT_VERSION,
                    title = UiTextHelper.StringResource(R.string.app_toolkit_version),
                    summary = UiTextHelper.DynamicString(appToolkitVersion),
                )
            )
        }
        if (!googlePlayServicesVersion.isNullOrBlank()) {
            add(
                AboutItem.Preference(
                    key = AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION,
                    title = UiTextHelper.StringResource(R.string.google_play_services_version),
                    summary = UiTextHelper.DynamicString(googlePlayServicesVersion),
                )
            )
        }
        add(
            AboutItem.Preference(
                key = AboutItemKey.OSS_LICENSES,
                title = UiTextHelper.StringResource(R.string.oss_license_title),
                summary = UiTextHelper.StringResource(R.string.summary_preference_settings_oss),
                action = AboutItemAction.OpenLicenses,
            )
        )
    }.assignPositions()

    val deviceInfoPreferences = buildList {
        if (deviceInfo.isNotBlank()) {
            add(
                AboutItem.Preference(
                    key = AboutItemKey.DEVICE_INFO,
                    title = UiTextHelper.StringResource(R.string.device_info),
                    summary = UiTextHelper.DynamicString(deviceInfo),
                    action = AboutItemAction.CopyDeviceInfo(deviceInfo = deviceInfo),
                )
            )
        }
    }.assignPositions()

    return buildList {
        add(
            AboutItem.Header(
                key = AboutItemKey.HEADER_APP_INFO,
                title = UiTextHelper.StringResource(R.string.app_info),
            )
        )
        addAll(appInfoPreferences)
        if (deviceInfoPreferences.isNotEmpty()) {
            add(
                AboutItem.Header(
                    key = AboutItemKey.HEADER_DEVICE_INFO,
                    title = UiTextHelper.StringResource(R.string.device_info),
                )
            )
            addAll(deviceInfoPreferences)
        }
    }
}

private fun List<AboutItem.Preference>.assignPositions(): List<AboutItem.Preference> =
    mapIndexed { index, item ->
        val position = when {
            size == 1 -> GroupedItemPosition.SINGLE
            index == 0 -> GroupedItemPosition.FIRST
            index == lastIndex -> GroupedItemPosition.LAST
            else -> GroupedItemPosition.MIDDLE
        }
        item.copy(position = position)
    }
