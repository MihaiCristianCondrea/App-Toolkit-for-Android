package com.d4rk.android.apps.apptoolkit.components.domain.usecase

import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore

/**
 * Persists the unlocked state for the components showcase so it can be shown in release builds.
 *
 * This use case isolates the DataStore write from UI layers and can be triggered after
 * meeting the unlock criteria (e.g., multiple About screen taps).
 */
class UnlockComponentsShowcaseUseCase(
    private val dataStore: DataStore,
) {
    suspend operator fun invoke() {
        dataStore.saveComponentsShowcaseUnlocked(true)
    }
}
