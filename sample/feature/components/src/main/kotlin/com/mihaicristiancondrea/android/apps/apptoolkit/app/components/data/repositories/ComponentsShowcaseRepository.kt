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

import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.DatastoreInterface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import kotlinx.coroutines.flow.Flow

/**
 * Owns whether the hidden components showcase has been unlocked.
 *
 * Keeps the ViewModel independent from the local DataStore source without adding a one-production-
 * implementation interface.
 */
class ComponentsShowcaseRepository(
    private val dataStore: DatastoreInterface,
    private val firebaseController: FirebaseController,
) {

    /** Emits whether the showcase entry should be offered. */
    val isUnlocked: Flow<Boolean> = dataStore.componentsShowcaseUnlocked

    /** Marks the showcase as unlocked so it appears in navigation. */
    suspend fun unlock() {
        firebaseController.logBreadcrumb(
            message = "Components showcase unlocked",
            attributes = mapOf("source" to "ComponentsShowcaseRepository"),
        )
        dataStore.saveComponentsShowcaseUnlocked(true)
    }
}
