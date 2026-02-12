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

package com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeneralSettingsRepositoryImpl(
    private val firebaseController: FirebaseController,
) : GeneralSettingsRepository {

    override fun getContentKey(contentKey: String?): Flow<String> = flow {
        firebaseController.logBreadcrumb(
            message = "General settings content requested",
            attributes = mapOf("hasContentKey" to (!contentKey.isNullOrBlank()).toString()),
        )
        val key = contentKey?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Invalid content key")
        emit(key)
    }
}
