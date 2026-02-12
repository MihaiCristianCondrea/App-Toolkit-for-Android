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

package com.d4rk.android.apps.apptoolkit.app.components.domain.usecase

import com.d4rk.android.apps.apptoolkit.core.data.local.DataStore

/**
 * Persists the unlocked state for the components showcase so it can be shown in release builds.
 *
 * This use case isolates the DataStore write from UI layers and can be triggered after
 * meeting the unlock criteria (e.g., multiple About screen taps).
 */
class UnlockComponentsShowcaseUseCase(
    private val dataStore: DataStore,
) {
    suspend operator fun invoke() {
        dataStore.saveComponentsShowcaseUnlocked(true)
    }
}
