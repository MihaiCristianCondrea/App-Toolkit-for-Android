package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository

class CopyDeviceInfoUseCase(
    private val repository: AboutRepository,
) {
    operator fun invoke(label: String, deviceInfo: String): Boolean =
        repository.copyDeviceInfo(label = label, deviceInfo = deviceInfo)
}
