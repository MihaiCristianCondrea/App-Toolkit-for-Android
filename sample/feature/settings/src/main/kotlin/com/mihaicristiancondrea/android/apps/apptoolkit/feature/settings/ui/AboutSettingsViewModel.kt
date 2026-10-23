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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.analytics.ABOUT_SCREEN_NAME
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.analytics.logShowcaseUnlocked
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.contracts.AboutSettingsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.LoggedScreenViewModel

/** Owns the host's unlock operation; the Toolkit About screen owns its rendered state. */
class AboutSettingsViewModel(
    private val repository: ShowcaseUnlockRepository,
    telemetryRepository: TelemetryRepository,
) : LoggedScreenViewModel<Unit, AboutSettingsEvent>(
    initialState = Unit,
    telemetryRepository = telemetryRepository,
    screenName = ABOUT_SCREEN_NAME,
    viewModelName = "AboutSettingsViewModel",
) {
    override fun handleEvent(event: AboutSettingsEvent) {
        when (event) {
            is AboutSettingsEvent.VersionTapped -> launchReport(action = Actions.UNLOCK_SHOWCASE) {
                if (repository.unlockAfterVersionTaps(event.tapCount)) {
                    telemetryRepository.logShowcaseUnlocked()
                }
            }
        }
    }

    private object Actions {
        const val UNLOCK_SHOWCASE = "unlock_components_showcase"
    }
}
