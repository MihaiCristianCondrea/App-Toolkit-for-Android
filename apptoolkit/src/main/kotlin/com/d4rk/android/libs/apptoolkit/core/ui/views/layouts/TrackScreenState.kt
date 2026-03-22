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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsEvent
import com.d4rk.android.libs.apptoolkit.core.domain.model.analytics.AnalyticsValue
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState

@Composable
fun TrackScreenState(
    firebaseController: FirebaseController,
    screenName: String,
    screenState: ScreenState,
) {
    val stateLabel: String = when (screenState) {
        is ScreenState.IsLoading -> "loading"
        is ScreenState.Success -> "success"
        is ScreenState.NoData -> "no_data"
        is ScreenState.Error -> "error"
    }

    LaunchedEffect(screenName, stateLabel) {
        firebaseController.logEvent(
            AnalyticsEvent(
                name = "screen_state",
                params = mapOf(
                    "screen" to AnalyticsValue.Str(screenName),
                    "state" to AnalyticsValue.Str(stateLabel),
                ),
            ),
        )
    }
}