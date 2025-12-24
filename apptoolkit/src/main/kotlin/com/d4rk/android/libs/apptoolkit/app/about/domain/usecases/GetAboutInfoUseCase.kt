package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository

class GetAboutInfoUseCase(
    private val repository: AboutRepository,
) {
    suspend operator fun invoke(): AboutInfo = repository.getAboutInfo()
}
