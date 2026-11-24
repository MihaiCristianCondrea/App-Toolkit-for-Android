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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.utils.extensions

import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.DisplayPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Projects the stored startup screen identifier without depending on a navigation framework.
 *
 * A blank identifier uses [defaultScreenId]; callers decide how to handle unknown or legacy
 * identifiers.
 * Consecutive equal mapped values are emitted only once.
 */
fun <T> DisplayPreferencesRepository.startupValueFlow(
    defaultScreenId: String,
    mapToValue: (String) -> T,
): Flow<T> = startupPage(default = defaultScreenId)
    .map { screenId -> mapToValue(screenId.ifBlank { defaultScreenId }) }
    .distinctUntilChanged()
