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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One page supplied by [OnboardingProvider][com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.OnboardingProvider].
 *
 * @property key Stable identifier reported in onboarding step and completion analytics.
 * @property isEnabled When `false`, the onboarding screen omits this page.
 */
sealed class OnboardingPage {
    abstract val key: String
    abstract val isEnabled: Boolean

    /** Standard icon, title, and description onboarding page. */
    data class DefaultPage(
        override val key: String,
        val title: String,
        val description: String,
        val imageVector: ImageVector,
        override val isEnabled: Boolean = true,
    ) : OnboardingPage()

    /**
     * Represents a custom onboarding page whose content can react to selection state.
     *
     * @property content Composable content for the page, notified when it is the active page.
     */
    data class CustomPage(
        override val key: String,
        val content: @Composable (isSelected: Boolean) -> Unit,
        override val isEnabled: Boolean = true,
    ) : OnboardingPage()
}


