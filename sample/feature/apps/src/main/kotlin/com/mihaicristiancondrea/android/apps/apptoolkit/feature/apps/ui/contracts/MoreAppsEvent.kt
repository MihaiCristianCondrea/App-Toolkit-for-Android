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

/** What the "More apps" section can ask its ViewModel to do. */
sealed interface MoreAppsEvent {
    /** Loads the catalogue, on start and from the failed state's retry. */
    data object Load : MoreAppsEvent

    /** Rechecks local package state without fetching the catalogue. */
    data object RefreshInstalled : MoreAppsEvent

    /** UI confirmed a foreground exposure; loading data alone is not an impression. */
    data object CarouselExposed : MoreAppsEvent
    data object CarouselHidden : MoreAppsEvent

    /** Opens the details sheet of an app that is not installed. */
    data class AppSelected(val packageName: String) : MoreAppsEvent

    /** Adds the app to the favorites from its details sheet, or removes it. */
    data class FavoriteToggled(val packageName: String) : MoreAppsEvent

    data object RetryAppDetails : MoreAppsEvent

    data object AppDetailsDismissed : MoreAppsEvent
}
