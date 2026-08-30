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
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Owns the persisted availability and hidden unlock policy for the Components showcase. */
class ComponentsShowcaseRepository(
    private val dataStore: DatastoreInterface,
    private val firebaseController: FirebaseController,
    private val isDebugBuild: Boolean = BuildConfig.DEBUG,
) {
    val isUnlocked: Flow<Boolean> = dataStore.componentsShowcaseUnlocked

    private val unlockMutex = Mutex()

    /** Persists the unlock after the About screen reports the required number of version taps. */
    suspend fun unlockAfterVersionTaps(tapCount: Int) {
        if (isDebugBuild || tapCount < COMPONENTS_UNLOCK_TAP_THRESHOLD) return

        unlockMutex.withLock {
            if (isUnlocked.first()) return
            firebaseController.logBreadcrumb(
                message = "Components showcase unlocked",
                attributes = mapOf("source" to "ComponentsShowcaseRepository"),
            )
            dataStore.saveComponentsShowcaseUnlocked(isUnlocked = true)
        }
    }

    private companion object {
        const val COMPONENTS_UNLOCK_TAP_THRESHOLD: Int = 7
    }
}
