package com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import kotlinx.coroutines.flow.Flow

// TODO && FIXME: May be miss from DI Koin
class ObserveAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
) {
    operator fun invoke(): Flow<Boolean> = repo.observeAdsEnabled()
}