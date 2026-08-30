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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.AppGa4Contract
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue

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
    CloseDetailsBottomSheet,
    GridAppImpression,
}

/**
 * Logs app-card interactions from app list style screens in a consistent GA4 format.
 */
fun FirebaseController.logAppInteraction(
    source: String,
    appInfo: AppInfo,
    interaction: AppInteractionType,
    interactionContext: String? = null,
) {
    logEvent(
        event = AnalyticsEvent(
            name = AppGa4Contract.EventName.APP_CARD_INTERACTION,
            params = buildMap {
                put(AppGa4Contract.Param.SOURCE, AnalyticsValue.Str(source))
                put(AppGa4Contract.Param.INTERACTION, AnalyticsValue.Str(interaction.name.lowercase()))
                put(AppGa4Contract.Param.PACKAGE_NAME, AnalyticsValue.Str(appInfo.packageName))
                put(AppGa4Contract.Param.APP_NAME, AnalyticsValue.Str(appInfo.name))
                appInfo.category?.id?.let {
                    put(AppGa4Contract.Param.APP_CATEGORY_ID, AnalyticsValue.Str(it))
                }
                appInfo.category?.label?.let {
                    put(AppGa4Contract.Param.APP_CATEGORY_LABEL, AnalyticsValue.Str(it))
                }
                interactionContext?.let {
                    put(AppGa4Contract.Param.INTERACTION_CONTEXT, AnalyticsValue.Str(it))
                }
            }
        )
    )
}
