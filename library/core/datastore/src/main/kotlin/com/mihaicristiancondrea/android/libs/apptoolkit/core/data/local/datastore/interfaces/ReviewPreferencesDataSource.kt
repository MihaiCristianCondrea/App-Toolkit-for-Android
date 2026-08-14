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


package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * Persisted counters backing the in-app review eligibility rules.
 */
interface ReviewPreferencesDataSource {

    /** Emits how many sessions have been recorded. */
    val sessionCount: Flow<Int>

    /** Emits whether the review prompt has already been shown. */
    val hasPromptedReview: Flow<Boolean>

    /** Increments the recorded session count. */
    suspend fun incrementSessionCount()

    /** Records whether the review prompt has been shown. */
    suspend fun setHasPromptedReview(value: Boolean)
}
