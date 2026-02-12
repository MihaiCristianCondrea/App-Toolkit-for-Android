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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OnboardingPage {

    data class DefaultPage(
        val key: String,
        val title: String,
        val description: String,
        val imageVector: ImageVector,
        val isEnabled: Boolean = true
    ) : OnboardingPage()

    /**
     * Represents a custom onboarding page whose content can react to selection state.
     *
     * @property content Composable content for the page, notified when it is the active page.
     */
    data class CustomPage(
        val key: String,
        val content: @Composable (isSelected: Boolean) -> Unit,
        val isEnabled: Boolean = true
    ) : OnboardingPage()
}
