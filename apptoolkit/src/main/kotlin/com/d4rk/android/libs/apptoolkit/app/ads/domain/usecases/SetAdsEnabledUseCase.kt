package com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository

class SetAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) = repo.setAdsEnabled(enabled)
}