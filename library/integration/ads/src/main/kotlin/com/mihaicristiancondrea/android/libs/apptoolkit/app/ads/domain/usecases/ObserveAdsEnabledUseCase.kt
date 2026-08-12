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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

/**
 * Exposes a stream of the ads-enabled flag from the repository.
 */
class ObserveAdsEnabledUseCase(
    private val repo: AdsSettingsRepository,
    private val firebaseController: FirebaseController,
) {
    operator fun invoke(): Flow<Boolean> = repo.observeAdsEnabled()
        .onStart {
            firebaseController.logBreadcrumb(
                message = "Observe ads enabled started",
                attributes = mapOf("source" to "ObserveAdsEnabledUseCase"),
            )
        }
}
