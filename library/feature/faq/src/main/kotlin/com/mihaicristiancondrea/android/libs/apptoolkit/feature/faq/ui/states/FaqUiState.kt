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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Everything the help page renders.
 *
 * @property questions The questions, remote or bundled.
 * @property openStoreListing The in-app review could not be shown, so the screen should open the
 * app's Play Store listing instead. The screen sends `FaqEvent.StoreListingOpened` once it has, so
 * the flag survives a configuration change but fires once.
 */
@Immutable
data class FaqUiState(
    val questions: Loadable<ImmutableList<FaqItem>> = Loadable.Loading,
    val openStoreListing: Boolean = false,
)
