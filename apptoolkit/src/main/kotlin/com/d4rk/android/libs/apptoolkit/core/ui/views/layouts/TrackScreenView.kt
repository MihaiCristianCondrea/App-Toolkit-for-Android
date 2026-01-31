package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

@Composable
fun TrackScreenView(
    firebaseController: FirebaseController,
    screenName: String,
    screenClass: String? = null,
) {
    LaunchedEffect(screenName) {
        firebaseController.logScreenView(
            screenName = screenName,
            screenClass = screenClass,
        )
    }
}