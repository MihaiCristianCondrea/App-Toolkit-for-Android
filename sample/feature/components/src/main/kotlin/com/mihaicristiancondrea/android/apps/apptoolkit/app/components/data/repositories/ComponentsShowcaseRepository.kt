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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.components.data.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Owns whether the hidden components showcase has been unlocked.
 *
 * Replaces `UnlockComponentsShowcaseUseCase`, which forwarded a single call to the DataStore. The
 * write needs an owner in the data layer rather than none at all: dropping the use case without one
 * would have left a ViewModel talking to a data source directly.
 */
interface ComponentsShowcaseRepository {

    /** Emits whether the showcase entry should be offered. */
    val isUnlocked: Flow<Boolean>

    /** Marks the showcase as unlocked so it appears in navigation. */
    suspend fun unlock()
}
