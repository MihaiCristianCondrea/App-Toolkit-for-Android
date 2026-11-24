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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.ThemePreferencesState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.repositories.ThemePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.koin.compose.koinInject

/**
 * Collects the repository's stable appearance flow at the design-system boundary. When the stored
 * preferences cannot be read, the app keeps [initialValue], the defaults, instead of crashing.
 */
@Composable
internal fun rememberThemePreferencesState(
    initialValue: ThemePreferencesState = ThemePreferencesState(),
): ThemePreferencesState {
    val repository: ThemePreferencesRepository = koinInject()
    val state: Flow<ThemePreferencesState> = remember(repository, initialValue) {
        repository.preferencesState.orDefaultOnStorageFailure(default = initialValue)
    }
    return state.collectAsStateWithLifecycle(initialValue = initialValue).value
}

/**
 * Replaces a [StorageException] from the upstream with [default] and then completes, so a failed
 * read of stored preferences falls back to defaults. Other failures still propagate.
 */
internal fun <T> Flow<T>.orDefaultOnStorageFailure(default: T): Flow<T> =
    catch { error ->
        if (error is StorageException) emit(default) else throw error
    }
