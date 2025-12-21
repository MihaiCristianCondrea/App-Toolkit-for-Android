package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository

/**
 * Use case responsible for delegating the copy of the device information to the
 * [AboutRepository]. Extracting this logic into a use case keeps the
 * [AboutViewModel][com.d4rk.android.libs.apptoolkit.app.about.ui.AboutViewModel]
 * focused on coordinating UI events.
 */
class CopyDeviceInfoUseCase(
    private val repository: AboutRepository,
) {
    /**
     * Copy the device information using the repository.
     */
    suspend operator fun invoke(label: String, deviceInfo: String) {
        repository.copyDeviceInfo(label = label, deviceInfo = deviceInfo)
    }
}

