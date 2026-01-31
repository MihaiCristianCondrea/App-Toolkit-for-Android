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