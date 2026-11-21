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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.advanced.ui.transformers

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.SeasonalThemeState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DeveloperOptionsUnlockTransformerTest {

    @Test
    fun `toDeveloperOptionsUnlocked projects unlock state and deduplicates repeated values`() = runTest {
        val emissions = flowOf(
            SeasonalThemeState(unlocked = false),
            SeasonalThemeState(unlocked = false),
            SeasonalThemeState(unlocked = true),
            SeasonalThemeState(unlocked = true),
            SeasonalThemeState(unlocked = false),
        ).toDeveloperOptionsUnlocked().toList()

        assertEquals(listOf(false, true, false), emissions)
    }
}
