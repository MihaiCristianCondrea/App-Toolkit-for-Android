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

package com.d4rk.android.apps.apptoolkit.app.main.ui.navigation

import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A manager that allows different components to request navigation events
 * that can be handled by the main navigation container.
 */
class NavigationManager {
    private val _navigationRequests = MutableSharedFlow<AppNavKey>(extraBufferCapacity = 1)
    val navigationRequests: SharedFlow<AppNavKey> = _navigationRequests.asSharedFlow()

    fun navigateTo(route: AppNavKey) {
        _navigationRequests.tryEmit(route)
    }
}
