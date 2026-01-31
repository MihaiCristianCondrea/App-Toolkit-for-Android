package com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * A use case for setting the enabled state of advertisements.
 *
 * This class acts as an intermediary between the UI/ViewModel layer and the data layer (repository).
 * It provides a single, focused action to enable or disable ads, abstracting the underlying
 * implementation details of how this setting is persisted.
 *
 * @property repo The repository responsible for handling ad settings persistence.
 */
class SetAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
    private val firebaseController: FirebaseController,
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        firebaseController.logBreadcrumb(
            message = "Set ads enabled",
            attributes = mapOf("enabled" to enabled.toString()),
        )
        return repo.setAdsEnabled(enabled)
    }
}
