/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.app.about.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case responsible for copying device information to the clipboard.
 *
 * This use case interacts with the [AboutRepository] to perform the copy operation
 * and returns a [Flow] emitting the state of the operation as [DataState].
 *
 * @property repository The repository used to perform the copy operation.
 */
class CopyDeviceInfoUseCase(
    private val repository: AboutRepository,
    private val firebaseController: FirebaseController,
) {

    operator fun invoke(
        label: String,
        deviceInfo: String,
    ): Flow<DataState<CopyDeviceInfoResult, Errors>> =
        flow {
            firebaseController.logBreadcrumb(
                message = "Copy device info started",
                attributes = mapOf(
                    "label" to label,
                    "deviceInfoLength" to deviceInfo.length.toString(),
                ),
            )
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
