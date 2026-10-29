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

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerUnits
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultToolkitTilesPreferencesDataSourceTest {

    @Test
    fun `ruler settings start at the defaults and keep every choice`(@TempDir directory: Path) = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { directory.resolve("ruler.preferences_pb").toFile() },
        )
        val source = DefaultToolkitTilesPreferencesDataSource(dataStore)

        assertEquals(RulerSettings(), source.rulerSettings.first())

        val chosen = RulerSettings(
            units = RulerUnits.Imperial,
            edge = RulerEdge.End,
            countFromEnd = true,
            inchDivisions = 32,
            advanced = true,
        )
        source.saveRulerSettings(chosen)

        assertEquals(chosen, source.rulerSettings.first())
    }

    @Test
    fun `stored expansion distinguishes no preference from all collapsed`(
        @TempDir directory: Path,
    ) = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { directory.resolve("toolkit-tiles.preferences_pb").toFile() },
        )
        val source = DefaultToolkitTilesPreferencesDataSource(dataStore)

        assertNull(source.expandedCategoryIds.first())

        source.saveExpandedCategoryIds(emptySet())

        assertEquals(emptySet(), source.expandedCategoryIds.first())

        source.saveExpandedCategoryIds(setOf("utilities", "system"))

        assertEquals(setOf("utilities", "system"), source.expandedCategoryIds.first())
    }

    @Test
    fun `counter persists increments and reset`(@TempDir directory: Path) = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { directory.resolve("toolkit-tiles.preferences_pb").toFile() },
        )
        val source = DefaultToolkitTilesPreferencesDataSource(dataStore)

        assertEquals(0, source.counterValue.first())

        source.incrementCounter()
        source.incrementCounter()

        assertEquals(2, source.counterValue.first())

        source.resetCounter()

        assertEquals(0, source.counterValue.first())
    }
    @Test
    fun `counter decrement persists without going below zero`(@TempDir directory: Path) = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { directory.resolve("counter.preferences_pb").toFile() },
        )
        val source = DefaultToolkitTilesPreferencesDataSource(dataStore)
        source.decrementCounter()
        assertEquals(0, source.counterValue.first())
        repeat(3) { source.incrementCounter() }
        source.decrementCounter()
        val reopened = DefaultToolkitTilesPreferencesDataSource(dataStore)
        assertEquals(2, reopened.counterValue.first())
        repeat(3) { reopened.decrementCounter() }
        assertEquals(0, source.counterValue.first())
    }

}
