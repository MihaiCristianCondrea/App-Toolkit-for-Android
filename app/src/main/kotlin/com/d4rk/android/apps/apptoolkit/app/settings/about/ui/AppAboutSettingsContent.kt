package com.d4rk.android.apps.apptoolkit.app.settings.about.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.d4rk.android.apps.apptoolkit.components.ui.ComponentsUnlockViewModel
import com.d4rk.android.libs.apptoolkit.app.about.ui.AboutSettingsList
import org.koin.compose.viewmodel.koinViewModel

/**
 * App-specific About screen content that wires the components showcase unlock behavior.
 */
@Composable
fun AppAboutSettingsContent(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val unlockViewModel: ComponentsUnlockViewModel = koinViewModel()

    AboutSettingsList(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        onVersionTap = { tapCount -> unlockViewModel.onVersionTap(tapCount) },
    )
}
