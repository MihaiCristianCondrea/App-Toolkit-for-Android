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

package com.wstxda.toolkit.ui.label

import android.content.Context
import android.os.BatteryManager as AndroidBatteryManager
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.battery.BatteryChargingSource
import com.wstxda.toolkit.manager.battery.BatteryDisplayState
import com.wstxda.toolkit.manager.battery.BatteryInfo

class BatteryLabelProvider(private val context: Context) {

    fun getLabel(info: BatteryInfo, state: BatteryDisplayState): CharSequence {
        if (info.level == 0 && info.voltageMv == 0) return context.getString(R.string.battery_tile)
        return when (state) {
            BatteryDisplayState.PERCENTAGE -> context.getString(
                R.string.battery_tile_percent, info.level
            )

            BatteryDisplayState.CURRENT -> context.getString(
                R.string.battery_tile_current, info.signedCurrentMa
            )

            BatteryDisplayState.VOLTAGE -> context.getString(
                R.string.battery_tile_voltage, info.voltageV
            )

            BatteryDisplayState.WATTAGE -> context.getString(
                R.string.battery_tile_wattage, info.wattageW
            )

            BatteryDisplayState.TEMPERATURE -> context.getString(
                R.string.battery_tile_temperature, info.temperatureC
            )
        }
    }

    fun getSubtitle(info: BatteryInfo, state: BatteryDisplayState): CharSequence? {
        if (state == BatteryDisplayState.TEMPERATURE) return when (info.healthCode) {
            AndroidBatteryManager.BATTERY_HEALTH_GOOD -> context.getString(R.string.battery_tile_health_good)
            AndroidBatteryManager.BATTERY_HEALTH_OVERHEAT -> context.getString(R.string.battery_tile_health_overheat)
            AndroidBatteryManager.BATTERY_HEALTH_DEAD -> context.getString(R.string.battery_tile_health_dead)
            AndroidBatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> context.getString(R.string.battery_tile_health_overvoltage)
            AndroidBatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> context.getString(R.string.battery_tile_health_failure)
            AndroidBatteryManager.BATTERY_HEALTH_COLD -> context.getString(R.string.battery_tile_health_cold)
            else -> context.getString(R.string.battery_tile_health_unknown)
        }
        return when {
            info.isFull -> context.getString(R.string.battery_tile_status_full)
            info.isPowerSave -> context.getString(R.string.battery_tile_status_saver)
            info.isCharging -> when (info.chargingSource) {
                BatteryChargingSource.AC -> context.getString(R.string.battery_tile_charging_ac)
                BatteryChargingSource.USB -> context.getString(R.string.battery_tile_charging_usb)
                BatteryChargingSource.WIRELESS -> context.getString(R.string.battery_tile_charging_wireless)
                BatteryChargingSource.DOCK -> context.getString(R.string.battery_tile_charging_dock)
                BatteryChargingSource.NONE -> context.getString(R.string.battery_tile_charging)
            }

            info.isLow -> context.getString(R.string.battery_tile_status_low)
            else -> context.getString(R.string.battery_tile_status_discharging)
        }
    }
}