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

package com.wstxda.toolkit.manager.caffeine

sealed class CaffeineState(val timeout: Int) {

    object Off : CaffeineState(-1)
    object FiveMinutes : CaffeineState(5 * 60 * 1000)
    object TenMinutes : CaffeineState(10 * 60 * 1000)
    object ThirtyMinutes : CaffeineState(30 * 60 * 1000)
    object OneHour : CaffeineState(60 * 60 * 1000)
    object Infinite : CaffeineState(Integer.MAX_VALUE)
}