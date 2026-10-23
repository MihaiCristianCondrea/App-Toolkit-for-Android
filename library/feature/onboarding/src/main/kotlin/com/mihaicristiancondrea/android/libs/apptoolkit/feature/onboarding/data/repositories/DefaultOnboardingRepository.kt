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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.extensions.storageCall
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.data.local.OnboardingPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DefaultOnboardingRepository(
    private val preferences: OnboardingPreferencesDataSource,
) : OnboardingRepository {

    override fun observeOnboardingCompletion(): Flow<Boolean> =
        preferences.startup
            .map { isFirstTime -> !isFirstTime }
            .distinctUntilChanged()

    override suspend fun setOnboardingCompleted() {
        storageCall { preferences.saveStartup(isFirstTime = false) }
    }
}
