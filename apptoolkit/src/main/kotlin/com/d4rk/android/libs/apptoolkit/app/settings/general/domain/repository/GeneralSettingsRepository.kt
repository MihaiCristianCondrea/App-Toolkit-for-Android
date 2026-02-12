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

package com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for providing the content key for the General Settings screen.
 *
 * Implementations should validate the provided key and emit it through a
 * [Flow]. This keeps the data operations asynchronous and testable.
 */
interface GeneralSettingsRepository {
    /**
     * Returns a [Flow] that emits a valid content key. If the provided key is
     * null or blank, the flow throws an [IllegalArgumentException] when
     * collected. Using a [Flow] allows the repository to expose asynchronous
     * data streams in line with recommended architecture guidelines.
     */
    fun getContentKey(contentKey: String?): Flow<String>
}


