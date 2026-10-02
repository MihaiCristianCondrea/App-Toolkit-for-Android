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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.providers

import androidx.compose.runtime.Composable

/**
 * Host extension for selecting a product-specific startup route. The default implementation
 * exposes no startup selector.
 */
interface DisplaySettingsProvider {

    /**
     * Whether the host supplies [StartupPageDialog]; defaults to `false`.
     */
    val supportsStartupPage: Boolean
        get() = false

    /**
     * Host-supplied startup selector. Implementations define the stored route format and report
     * selections through [onStartupSelected]; [onDismiss] closes the presentation.
     */
    @Composable
    fun StartupPageDialog(
        currentRoute: String,
        onDismiss: () -> Unit,
        onStartupSelected: (String) -> Unit,
    ) {

    }
}
