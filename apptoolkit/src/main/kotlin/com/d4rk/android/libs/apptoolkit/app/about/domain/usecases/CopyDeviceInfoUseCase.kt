package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CopyDeviceInfoUseCase(private val repository: AboutRepository) {

    operator fun invoke(
        label: String,
        deviceInfo: String,
    ): Flow<DataState<CopyDeviceInfoResult, Errors>> =
        flow {
            val result = repository.copyDeviceInfo(
                label = label,
                deviceInfo = deviceInfo
            )
            emit(DataState.Success(result))
        }
}
