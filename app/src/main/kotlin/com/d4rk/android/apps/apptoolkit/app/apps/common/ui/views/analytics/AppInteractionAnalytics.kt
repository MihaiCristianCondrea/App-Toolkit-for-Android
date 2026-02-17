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

package com.d4rk.android.apps.apptoolkit.app.apps.common.ui.views.analytics

import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * App list interaction types reported to GA4.
 */
enum class AppInteractionType {
    AddFavorite,
    RemoveFavorite,
    Share,
    OpenDetailsBottomSheet,
    OpenInPlayStore,
    OpenInstalledApp,
}

/**
 * Logs app-card interactions from app list style screens in a consistent GA4 format.
 */
fun FirebaseController.logAppInteraction(
    source: String,
    appInfo: AppInfo,
    interaction: AppInteractionType,
) {
    logEvent(
        event = AnalyticsEvent(
            name = "app_card_interaction",
            params = mapOf(
                "source" to AnalyticsValue.Str(source),
                "interaction" to AnalyticsValue.Str(interaction.name.lowercase()),
                "package_name" to AnalyticsValue.Str(appInfo.packageName),
                "app_name" to AnalyticsValue.Str(appInfo.name),
            )
        )
    )
}
