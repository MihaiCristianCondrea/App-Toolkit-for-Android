package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CopyDeviceInfoUseCase(private val repository: AboutRepository) {

    operator fun invoke(label: String, deviceInfo: String): Flow<DataState<Boolean, Errors>> =
        flow {
            val copied = repository.copyDeviceInfo(label = label, deviceInfo = deviceInfo)
            emit(DataState.Success(copied))
        }
}
