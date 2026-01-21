package com.d4rk.android.apps.apptoolkit.components.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.components.domain.usecase.UnlockComponentsShowcaseUseCase
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.launch

/**
 * Handles unlocking the components showcase in release builds.
 *
 * The unlock is triggered after a configurable number of taps on the About screen's
 * app version entry, keeping the setting hidden from casual users.
 */
class ComponentsUnlockViewModel(
    private val unlockComponentsShowcase: UnlockComponentsShowcaseUseCase,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {
    private var unlockRequested: Boolean = false

    fun onVersionTap(tapCount: Int) {
        if (BuildConfig.DEBUG || unlockRequested) return
        if (tapCount < COMPONENTS_UNLOCK_TAP_THRESHOLD) return

        unlockRequested = true
        viewModelScope.launch(dispatchers.io) {
            unlockComponentsShowcase()
        }
    }

    private companion object {
        const val COMPONENTS_UNLOCK_TAP_THRESHOLD: Int = 7
    }
}
