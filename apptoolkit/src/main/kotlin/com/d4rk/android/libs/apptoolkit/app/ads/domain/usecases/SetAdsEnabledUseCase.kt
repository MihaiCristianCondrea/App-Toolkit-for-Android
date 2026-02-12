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

package com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases

import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController

/**
 * A use case for setting the enabled state of advertisements.
 *
 * This class acts as an intermediary between the UI/ViewModel layer and the data layer (repository).
 * It provides a single, focused action to enable or disable ads, abstracting the underlying
 * implementation details of how this setting is persisted.
 *
 * @property repo The repository responsible for handling ad settings persistence.
 */
class SetAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
    private val firebaseController: FirebaseController,
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        firebaseController.logBreadcrumb(
            message = "Set ads enabled",
            attributes = mapOf("enabled" to enabled.toString()),
        )
        return repo.setAdsEnabled(enabled)
    }
}
