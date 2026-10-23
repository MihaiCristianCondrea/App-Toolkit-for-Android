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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.local

import app.cash.turbine.test
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewPreferencesDataSourceTest {

    @Test
    fun `session count defaults to zero and increments`() = runTest {
        val dataStore = createDataStore()

        assertEquals(0, dataStore.sessionCount.first())

        dataStore.incrementSessionCount()
        dataStore.incrementSessionCount()

        assertEquals(2, dataStore.sessionCount.first())
    }

    @Test
    fun `has prompted review defaults to false and persists`() = runTest {
        val dataStore = createDataStore()

        assertFalse(dataStore.hasPromptedReview.first())

        dataStore.setHasPromptedReview(true)

        assertTrue(dataStore.hasPromptedReview.first())
    }

    private fun createDataStore(): DefaultReviewPreferencesDataSource =
        DefaultReviewPreferencesDataSource(dataStore = FakePreferencesDataStore())
}
