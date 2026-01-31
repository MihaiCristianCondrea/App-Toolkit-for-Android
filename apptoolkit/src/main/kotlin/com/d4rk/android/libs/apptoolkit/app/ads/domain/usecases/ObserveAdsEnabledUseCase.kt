package com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Exposes a stream of the ads-enabled flag from the repository.
 */
class ObserveAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
    private val firebaseController: FirebaseController,
) {
    operator fun invoke(): Flow<Boolean> = repo.observeAdsEnabled()
        .onStart {
            firebaseController.logBreadcrumb(
                message = "Observe ads enabled started",
                attributes = mapOf("source" to "ObserveAdsEnabledUseCase"),
            )
        }
}
