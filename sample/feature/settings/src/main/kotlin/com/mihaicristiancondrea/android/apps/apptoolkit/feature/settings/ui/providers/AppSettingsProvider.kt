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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.providers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.utils.constants.SettingsConstants

import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AboutRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.AdvancedSettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.DisplaySettingsRoute
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.routes.PrivacySettingsRoute
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Security
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openAppNotificationSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsCategory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.settings.SettingsPreference
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.ui.providers.SettingsProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.settings.R as SettingsR

/**
 * Builds the sample's root settings categories using an application context.
 *
 * Each row names the Toolkit page it opens. Notification settings prefer the platform
 * app-notification page and fall back to the Toolkit's privacy page when that intent is unavailable.
 */
class AppSettingsProvider(context: Context) : SettingsProvider {
    private val context: Context = context.applicationContext

    override fun provideSettingsConfig(): SettingsConfig {
        return SettingsConfig(
            title = context.getString(SettingsR.string.settings),
            categories = listOf(
                SettingsCategory(
                    preferences = listOf(
                        SettingsPreference(
                            key = SettingsConstants.KEY_SETTINGS_NOTIFICATION,
                            icon = Icons.Outlined.Notifications,
                            useIconContainer = true,
                            iconColor = Color(0xFF8D0154),
                            iconContainerColor = Color(0xFFFFD8EF),
                            title = context.getString(SettingsR.string.notifications),
                            summary = context.getString(SettingsR.string.summary_preference_settings_notifications),
                            destination = PrivacySettingsRoute,
                            action = { context.openAppNotificationSettings() },
                        ),
                        SettingsPreference(
                            key = SettingsConstants.KEY_SETTINGS_DISPLAY,
                            icon = Icons.Outlined.Palette,
                            useIconContainer = true,
                            iconColor = Color(0xFF763504),
                            iconContainerColor = Color(0xFFFFDCC3),
                            title = context.getString(SettingsR.string.display),
                            summary = context.getString(SettingsR.string.summary_preference_settings_display),
                            destination = DisplaySettingsRoute,
                        ),
                    ),
                ),
                SettingsCategory(
                    preferences = listOf(
                        SettingsPreference(
                            key = SettingsConstants.KEY_SETTINGS_SECURITY_AND_PRIVACY,
                            icon = Icons.Outlined.Security,
                            useIconContainer = true,
                            iconColor = Color(0xFF014E69),
                            iconContainerColor = Color(0xFFBDE9FF),
                            title = context.getString(SettingsR.string.security_and_privacy),
                            summary = context.getString(SettingsR.string.summary_preference_settings_privacy_and_security),
                            destination = PrivacySettingsRoute,
                        ),
                        SettingsPreference(
                            key = SettingsConstants.KEY_SETTINGS_ADVANCED,
                            icon = Icons.Outlined.Build,
                            useIconContainer = true,
                            iconColor = Color(0xFF572AA4),
                            iconContainerColor = Color(0xFFEEDCFE),
                            title = context.getString(SettingsR.string.advanced),
                            summary = context.getString(SettingsR.string.summary_preference_settings_advanced),
                            destination = AdvancedSettingsRoute,
                        ),
                        SettingsPreference(
                            key = SettingsConstants.KEY_SETTINGS_ABOUT,
                            icon = Icons.Outlined.Info,
                            useIconContainer = true,
                            iconColor = Color(0xFF484848),
                            iconContainerColor = Color(0xFFE3E3E3),
                            title = context.getString(CoreUiR.string.about),
                            summary = context.getString(SettingsR.string.summary_preference_settings_about),
                            destination = AboutRoute,
                        ),
                    ),
                ),
            ),
        )
    }
}
