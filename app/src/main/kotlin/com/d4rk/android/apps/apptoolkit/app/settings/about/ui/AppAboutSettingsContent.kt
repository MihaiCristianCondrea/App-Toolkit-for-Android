package com.d4rk.android.apps.apptoolkit.app.settings.about.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.d4rk.android.apps.apptoolkit.app.components.ui.ComponentsUnlockViewModel
import com.d4rk.android.apps.apptoolkit.app.components.ui.contract.ComponentsUnlockEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.AboutScreen
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

    AboutScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        onVersionTap = { tapCount ->
            unlockViewModel.onEvent(ComponentsUnlockEvent.VersionTapped(tapCount))
        },
    )
}
