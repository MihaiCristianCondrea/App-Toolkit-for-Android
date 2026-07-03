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

package com.d4rk.android.apps.apptoolkit.app.tiles.data.repository

import android.content.Context
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.model.CaffeineState
import com.d4rk.android.apps.apptoolkit.app.tiles.domain.repository.CaffeineRepository
import com.d4rk.android.apps.apptoolkit.app.tiles.service.CaffeineService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CaffeineRepositoryImpl(
    private val context: Context
) : CaffeineRepository {

    private val _currentState = MutableStateFlow<CaffeineState>(CaffeineState.Off)
    override val currentState: StateFlow<CaffeineState> = _currentState.asStateFlow()

    private val stateCycle = listOf(
        CaffeineState.Off,
        CaffeineState.FiveMinutes,
        CaffeineState.TenMinutes,
        CaffeineState.ThirtyMinutes,
        CaffeineState.OneHour,
        CaffeineState.Infinite
    )

    override fun cycleState() {
        val currentIndex = stateCycle.indexOf(_currentState.value)
        val nextState = stateCycle[(currentIndex + 1) % stateCycle.size]
        _currentState.value = nextState
        
        if (nextState == CaffeineState.Off) {
            CaffeineService.stop(context)
        } else {
            CaffeineService.start(context, nextState.durationMillis)
        }
    }

    override fun reset() {
        _currentState.value = CaffeineState.Off
        CaffeineService.stop(context)
    }
}
