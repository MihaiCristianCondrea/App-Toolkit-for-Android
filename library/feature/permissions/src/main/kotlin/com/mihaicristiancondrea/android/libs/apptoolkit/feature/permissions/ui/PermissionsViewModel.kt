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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.data.repositories.PermissionsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.contracts.PermissionsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.ui.states.PermissionsUiState
import kotlinx.coroutines.Job

/**
 * Loads the permission catalog for the permissions page.
 *
 * [PermissionsRepository] is main-safe, so it needs no dispatcher. A catalog with no category is
 * [Loadable.Empty]; a repository that throws is [Loadable.Failed], reported to telemetry.
 */
class PermissionsViewModel(
    private val permissionsRepository: PermissionsRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<PermissionsUiState, PermissionsEvent>(
    initialState = PermissionsUiState(),
    telemetryRepository = telemetryRepository,
    screenName = "Permissions",
    viewModelName = "PermissionsViewModel",
) {
    private var loadJob: Job? = null

    init {
        onEvent(PermissionsEvent.Load)
    }

    override fun handleEvent(event: PermissionsEvent) {
        when (event) {
            PermissionsEvent.Load -> loadPermissions()
        }
    }

    private fun loadPermissions() {
        loadJob = loadJob.restart {
            launchReport(
                action = Actions.LOAD_PERMISSIONS,
                onError = { error -> setState { copy(config = error.toFailed()) } },
            ) {
                setState { copy(config = Loadable.Loading) }
                val loaded = permissionsRepository.getPermissionsConfig()
                val content = if (loaded.categories.isEmpty()) {
                    Loadable.Empty(NoPermissionsText)
                } else {
                    Loadable.Ready(loaded)
                }
                setState { copy(config = content) }
            }
        }
    }

    private object Actions {
        const val LOAD_PERMISSIONS: String = "loadPermissions"
    }

    private companion object {
        val NoPermissionsText = UiTextHelper.StringResource(R.string.error_no_settings_found)
    }
}
