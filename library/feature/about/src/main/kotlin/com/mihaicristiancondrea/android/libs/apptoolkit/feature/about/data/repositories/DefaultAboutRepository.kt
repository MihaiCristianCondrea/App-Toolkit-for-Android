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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories

import android.content.Context
import android.os.Build
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.R as CommonR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CLIPBOARD_HELPER_LOG_TAG
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.copyTextToClipboard
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItemAction
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutItemKey
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.CopyDeviceInfoResult
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider

/**
 * Provides About-related data and handles clipboard interactions for device info.
 *
 * Prepares display-ready [AboutItem] entries with pre-computed titles, summaries, actions,
 * and grouped card positions for consumption by the UI layer.
 *
 * @param sdkIntProvider Supplies the current SDK version for clipboard feedback decisions.
 * @param gmsVersionProvider Supplies the installed Google Play services runtime version, if present.
 * @param toolkitVersionProvider Supplies the App Toolkit library publishing version.
 */
class DefaultAboutRepository(
    private val deviceProvider: AboutSettingsProvider,
    private val buildInfoProvider: BuildInfoProvider,
    private val context: Context,
    private val firebaseController: FirebaseController,
    private val sdkIntProvider: () -> Int = { Build.VERSION.SDK_INT },
    private val gmsVersionProvider: GooglePlayServicesVersionProvider = GooglePlayServicesVersionProvider(context),
    private val toolkitVersionProvider: () -> String = { BuildConfig.APP_TOOLKIT_VERSION },
) : AboutRepository {

    override suspend fun getAboutInfo(): AboutInfo {
        firebaseController.logBreadcrumb(
            message = "About info load started",
            attributes = mapOf("source" to "AboutRepository"),
        )
        val rawDeviceInfo = deviceProvider.deviceInfo
        val items = buildAboutItems(
            appVersion = buildInfoProvider.appVersion,
            appVersionCode = buildInfoProvider.appVersionCode,
            toolkitVersion = toolkitVersionProvider(),
            gmsVersion = gmsVersionProvider.getVersion(),
            deviceInfo = rawDeviceInfo,
        )
        return AboutInfo(
            items = items,
            deviceInfo = rawDeviceInfo,
        )
    }

    override fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult {
        firebaseController.logBreadcrumb(
            message = "Copy device info requested",
            attributes = mapOf("label" to label),
        )
        val allowFeedback = sdkIntProvider() <= Build.VERSION_CODES.S_V2
        var shouldShowFeedback = false
        val copied = runCatching {
            context.copyTextToClipboard(
                label = label,
                text = deviceInfo,
                onCopyFallback = {
                    if (allowFeedback) {
                        shouldShowFeedback = true
                    }
                }
            )
        }.onFailure { throwable ->
            Log.w(CLIPBOARD_HELPER_LOG_TAG, "Failed to copy device info", throwable)
            if (allowFeedback) {
                shouldShowFeedback = true
            }
        }.getOrDefault(false)
        return CopyDeviceInfoResult(
            copied = copied,
            shouldShowFeedback = shouldShowFeedback
        )
    }

    private fun buildAboutItems(
        appVersion: String,
        appVersionCode: Int,
        toolkitVersion: String,
        gmsVersion: String?,
        deviceInfo: String,
    ): List<AboutItem> {
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
            if (toolkitVersion.isNotBlank()) {
                add(
                    AboutItem.Preference(
                        key = AboutItemKey.APP_TOOLKIT_VERSION,
                        title = UiTextHelper.StringResource(R.string.app_toolkit_version),
                        summary = UiTextHelper.DynamicString(toolkitVersion),
                    )
                )
            }
            if (!gmsVersion.isNullOrBlank()) {
                add(
                    AboutItem.Preference(
                        key = AboutItemKey.GOOGLE_PLAY_SERVICES_VERSION,
                        title = UiTextHelper.StringResource(R.string.google_play_services_version),
                        summary = UiTextHelper.DynamicString(gmsVersion),
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

        val deviceInfoPreferences = listOf(
            AboutItem.Preference(
                key = AboutItemKey.DEVICE_INFO,
                title = UiTextHelper.StringResource(R.string.device_info),
                summary = UiTextHelper.DynamicString(deviceInfo),
                action = AboutItemAction.CopyDeviceInfo,
            )
        ).assignPositions()

        return buildList {
            add(
                AboutItem.Header(
                    key = AboutItemKey.HEADER_APP_INFO,
                    title = UiTextHelper.StringResource(R.string.app_info),
                )
            )
            addAll(appInfoPreferences)
            add(
                AboutItem.Header(
                    key = AboutItemKey.HEADER_DEVICE_INFO,
                    title = UiTextHelper.StringResource(R.string.device_info),
                )
            )
            addAll(deviceInfoPreferences)
        }
    }

    private fun List<AboutItem.Preference>.assignPositions(): List<AboutItem.Preference> {
        return mapIndexed { index, item ->
            val position = when {
                size == 1 -> GroupedItemPosition.SINGLE
                index == 0 -> GroupedItemPosition.FIRST
                index == lastIndex -> GroupedItemPosition.LAST
                else -> GroupedItemPosition.MIDDLE
            }
            item.copy(position = position)
        }
    }
}
