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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.local.preferences

import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeToolkitTilesPreferencesDataSource(
    override val expandedCategoryIds: MutableStateFlow<Set<String>?> = MutableStateFlow(null),
) : ToolkitTilesPreferencesDataSource {
    val savedIds = MutableStateFlow<Set<String>?>(null)

    override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
        savedIds.value = categoryIds
        expandedCategoryIds.value = categoryIds
    }

    override val counterValue = MutableStateFlow(0)

    override suspend fun incrementCounter() {
        counterValue.value += 1
    }

    override suspend fun resetCounter() {
        counterValue.value = 0
    }
}
