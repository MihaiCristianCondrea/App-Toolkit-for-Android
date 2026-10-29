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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerEdge
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerSettings
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.models.RulerUnits
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Stores Toolkit Tiles catalogue preferences in the app's shared Preferences DataStore. */
class DefaultToolkitTilesPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : ToolkitTilesPreferencesDataSource {

    override val expandedCategoryIds: Flow<Set<String>?> = dataStore.data.map { preferences ->
        preferences[EXPANDED_CATEGORY_IDS]
    }

    override suspend fun saveExpandedCategoryIds(categoryIds: Set<String>) {
        dataStore.edit { preferences ->
            preferences[EXPANDED_CATEGORY_IDS] = categoryIds
        }
    }

    override val counterValue: Flow<Int> = dataStore.data.map { preferences ->
        preferences[COUNTER_VALUE] ?: 0
    }.distinctUntilChanged()

    override suspend fun incrementCounter() {
        dataStore.edit { preferences ->
            preferences[COUNTER_VALUE] = (preferences[COUNTER_VALUE] ?: 0) + 1
        }
    }

    override suspend fun decrementCounter() {
        dataStore.edit { preferences ->
            preferences[COUNTER_VALUE] = (preferences[COUNTER_VALUE] ?: 0).coerceAtLeast(1) - 1
        }
    }

    override val rulerScale: Flow<Float> = dataStore.data.map { preferences ->
        preferences[RULER_SCALE]?.takeIf { it.isFinite() && it in 0.5f..2f } ?: 1f
    }.distinctUntilChanged()

    override suspend fun saveRulerScale(scale: Float) {
        require(scale.isFinite() && scale in 0.5f..2f)
        dataStore.edit { it[RULER_SCALE] = scale }
    }

    override val rulerSettings: Flow<RulerSettings> = dataStore.data.map { preferences ->
        val defaults = RulerSettings()
        RulerSettings(
            units = RulerUnits.entries.firstOrNull { it.name == preferences[RULER_UNITS] } ?: defaults.units,
            edge = RulerEdge.entries.firstOrNull { it.name == preferences[RULER_EDGE] } ?: defaults.edge,
            countFromEnd = preferences[RULER_COUNT_FROM_END] ?: defaults.countFromEnd,
            inchDivisions = preferences[RULER_INCH_DIVISIONS]?.takeIf { it in RulerSettings.InchDivisions } ?: defaults.inchDivisions,
            advanced = preferences[RULER_ADVANCED] ?: defaults.advanced,
        )
    }.distinctUntilChanged()

    override suspend fun saveRulerSettings(settings: RulerSettings) {
        require(settings.inchDivisions in RulerSettings.InchDivisions)
        dataStore.edit { preferences ->
            preferences[RULER_UNITS] = settings.units.name
            preferences[RULER_EDGE] = settings.edge.name
            preferences[RULER_COUNT_FROM_END] = settings.countFromEnd
            preferences[RULER_INCH_DIVISIONS] = settings.inchDivisions
            preferences[RULER_ADVANCED] = settings.advanced
        }
    }

    override suspend fun resetCounter() {
        dataStore.edit { preferences -> preferences.remove(COUNTER_VALUE) }
    }

    private companion object {
        val EXPANDED_CATEGORY_IDS = stringSetPreferencesKey("expanded_toolkit_tile_category_ids")
        val RULER_SCALE = floatPreferencesKey("toolkit_ruler_scale")
        val RULER_UNITS = stringPreferencesKey("toolkit_ruler_units")
        val RULER_EDGE = stringPreferencesKey("toolkit_ruler_edge")
        val RULER_COUNT_FROM_END = booleanPreferencesKey("toolkit_ruler_count_from_end")
        val RULER_INCH_DIVISIONS = intPreferencesKey("toolkit_ruler_inch_divisions")
        val RULER_ADVANCED = booleanPreferencesKey("toolkit_ruler_advanced")
        val COUNTER_VALUE = intPreferencesKey("toolkit_tile_counter_value")
    }
}
