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

package com.wstxda.toolkit.manager.battery

import kotlin.math.abs

data class BatteryInfo(
    val level: Int = 0,
    val voltageMv: Int = 0,
    val currentMa: Int = 0,
    val temperatureTenths: Int = 0,
    val healthCode: Int = android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN,
    val isCharging: Boolean = false,
    val isFull: Boolean = false,
    val isPowerSave: Boolean = false,
    val chargingSource: BatteryChargingSource = BatteryChargingSource.NONE,
) {
    val voltageV: Float get() = voltageMv / 1000f
    val temperatureC: Float get() = temperatureTenths / 10f
    val isLow: Boolean get() = !isCharging && level < 25
    val signedCurrentMa: Int get() = if (isCharging) abs(currentMa) else -abs(currentMa)
    val wattageW: Float get() = voltageV * (abs(currentMa) / 1000f)
}