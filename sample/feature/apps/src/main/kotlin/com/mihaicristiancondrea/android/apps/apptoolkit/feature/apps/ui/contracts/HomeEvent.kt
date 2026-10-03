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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.AppsListFilter

/**
 * What the user can ask the Apps List screen's ViewModel to do.
 */
sealed interface HomeEvent {
    /** Loads the catalogue, on start, on retry and from the stale catalogue's Try again action. */
    data object Load : HomeEvent

    /** Picks a random app from the catalogue for the screen to open. */
    data object OpenRandomApp : HomeEvent

    /** The screen opened the app that `AppListUiState.randomAppToOpen` asked for. */
    data object RandomAppOpened : HomeEvent

    data class FilterSelected(val filter: AppsListFilter) : HomeEvent

    /** Adds the app to the favorites, or removes it when it is one already. */
    data class FavoriteToggled(val packageName: String) : HomeEvent

    /** Opens the details sheet for the app. */
    data class AppSelected(val packageName: String) : HomeEvent

    data object RetryAppDetails : HomeEvent

    data object AppDetailsDismissed : HomeEvent
}
