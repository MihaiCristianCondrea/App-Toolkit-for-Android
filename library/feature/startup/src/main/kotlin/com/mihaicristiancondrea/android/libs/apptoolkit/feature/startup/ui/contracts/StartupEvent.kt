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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.startup.ui.contracts

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentHost

/**
 * What the user can ask the startup screen's ViewModel to do.
 */
sealed interface StartupEvent {
    /**
     * Asks for consent, showing the form when the consent SDK requires it. The screen sends it on
     * every resume; once consent has settled it does nothing.
     *
     * @property host The activity the form shows over, used only for this request, or null when
     * the screen has none, which settles consent at once.
     */
    data class RequestConsent(val host: ConsentHost?) : StartupEvent
}
