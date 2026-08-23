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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.domain.models


sealed class CaffeineState {
    abstract val durationMillis: Long?

    data object Off : CaffeineState() {
        override val durationMillis: Long? = null
    }

    data object FiveMinutes : CaffeineState() {
        override val durationMillis: Long = 5 * 60 * 1000L
    }

    data object TenMinutes : CaffeineState() {
        override val durationMillis: Long = 10 * 60 * 1000L
    }

    data object ThirtyMinutes : CaffeineState() {
        override val durationMillis: Long = 30 * 60 * 1000L
    }

    data object OneHour : CaffeineState() {
        override val durationMillis: Long = 60 * 60 * 1000L
    }

    data object Infinite : CaffeineState() {
        override val durationMillis: Long? = null
    }
}
