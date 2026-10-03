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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states

import androidx.compose.runtime.Immutable

/**
 * Everything the onboarding screen renders. The pages come from the host's provider and are there
 * from the start, so nothing here loads.
 *
 * @property currentTabIndex The page the pager last settled on, where it opens again.
 * @property isOnboardingCompleted Whether the stored flag says onboarding is done. The screen does
 * not navigate on it: it is already true when onboarding is opened again from the developer options.
 * @property completion Where finishing stands. [OnboardingCompletion.Saved] enters the shell.
 */
@Immutable
data class OnboardingUiState(
    val currentTabIndex: Int = 0,
    val isOnboardingCompleted: Boolean = false,
    val completion: OnboardingCompletion = OnboardingCompletion.Pending,
)
