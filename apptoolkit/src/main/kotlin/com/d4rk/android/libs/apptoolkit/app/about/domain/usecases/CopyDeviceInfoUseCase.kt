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
            val result = runCatching {
                repository.copyDeviceInfo(
                    label = label,
                    deviceInfo = deviceInfo
                )
            }.getOrElse {
                emit(DataState.Error(error = Errors.UseCase.INVALID_STATE))
                return@flow
            }

            if (result.copied) {
                emit(DataState.Success(result))
            } else {
                emit(DataState.Error(data = result, error = Errors.UseCase.INVALID_STATE))
            }
        }
}
