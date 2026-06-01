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

package com.wstxda.toolkit.manager.diceroll

import android.content.Context
import com.wstxda.toolkit.ui.utils.Haptics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceRollManager(context: Context) {

    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val haptics = Haptics(context.applicationContext)

    private val _currentRoll = MutableStateFlow<Int?>(null)
    val currentRoll = _currentRoll.asStateFlow()

    private val _isRolling = MutableStateFlow(false)
    val isRolling = _isRolling.asStateFlow()

    private var animationJob: Job? = null

    fun roll() {
        if (_isRolling.value) return

        animationJob?.cancel()
        animationJob = managerScope.launch {
            _isRolling.value = true

            val finalRoll = Random.nextInt(1, 7)

            for (i in 0 until 12) {
                _currentRoll.value = Random.nextInt(1, 7)
                haptics.low()
                delay(60L + (i * 30))
            }

            _currentRoll.value = finalRoll
            haptics.veryHigh()
            _isRolling.value = false
        }
    }

    fun clearState() {
        animationJob?.cancel()
        animationJob = null
        _isRolling.value = false
        _currentRoll.value = null
    }

    fun cleanup() {
        clearState()
        managerScope.cancel()
    }
}