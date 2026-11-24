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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.models.OnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states.OnboardingUiState
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenContentPreview() {
    MaterialTheme {
        OnboardingScreenContent(
            state = OnboardingUiState(),
            pages = persistentListOf(
                OnboardingPage.DefaultPage(
                    key = "welcome",
                    title = "Welcome",
                    description = "A short tour of what the app can do.",
                    imageVector = Icons.Outlined.Star,
                ),
                OnboardingPage.DefaultPage(
                    key = "theme",
                    title = "Make it yours",
                    description = "Pick a theme and a palette.",
                    imageVector = Icons.Outlined.Palette,
                ),
            ),
            onEvent = {},
        )
    }
}
