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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.analytics

import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.contracts.AppGa4Contract
import com.mihaicristiancondrea.android.apps.apptoolkit.core.analytics.models.AppScreenTracking
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

/** Builds the click event a component showcase logs for [component] and its optional [variant]. */
internal fun componentClickEvent(component: String, variant: String? = null): Ga4EventData {
    val params = buildMap {
        put(AppGa4Contract.Param.SCREEN, AnalyticsValue.Str(AppScreenTracking.Screens.COMPONENTS.name))
        put(AppGa4Contract.Param.COMPONENT, AnalyticsValue.Str(component))
        variant?.let { put(AppGa4Contract.Param.VARIANT, AnalyticsValue.Str(it)) }
    }
    return Ga4EventData(name = AppGa4Contract.EventName.COMPONENTS_CLICK, params = params)
}
