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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories

import com.mihaicristiancondrea.android.apps.apptoolkit.core.datastore.data.local.DatastoreInterface
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.first

/**
 * Turns the About screen's version taps into the persisted showcase-unlock flag.
 *
 * The flag itself is owned by [DatastoreInterface] in `:sample:core:datastore`, which is what lets
 * the gesture live with the Settings surface that hosts it while the showcase feature reads the
 * same flag without either module depending on the other.
 *
 * Debug builds skip persistence: the showcase is always reachable there, so recording an unlock
 * would leak into a later release install of the same app.
 */
class ShowcaseUnlockRepository(
    private val dataStore: DatastoreInterface,
    private val firebaseController: FirebaseController,
    private val isDebugBuild: Boolean = BuildConfig.DEBUG,
) {
    private val unlockMutex = Mutex()

    /** Persists the unlock once [tapCount] reaches the threshold. Repeat calls are no-ops. */
    suspend fun unlockAfterVersionTaps(tapCount: Int) {
        if (isDebugBuild || tapCount < UNLOCK_TAP_THRESHOLD) return

        unlockMutex.withLock {
            if (dataStore.componentsShowcaseUnlocked.first()) return
            firebaseController.logBreadcrumb(
                message = "Components showcase unlocked",
                attributes = mapOf("source" to "ShowcaseUnlockRepository"),
            )
            dataStore.saveComponentsShowcaseUnlocked(isUnlocked = true)
        }
    }

    private companion object {
        const val UNLOCK_TAP_THRESHOLD: Int = 7
    }
}
