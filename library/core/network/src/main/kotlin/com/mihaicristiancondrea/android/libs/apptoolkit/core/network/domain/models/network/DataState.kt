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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.network.domain.models.network

typealias RootError = Error

/**
 * Outcome of a data operation. Loading can carry previously available data; error values remain
 * typed for callers.
 */
sealed interface DataState<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : DataState<D, E>
    data class Error<out D, out E : RootError>(val data: D? = null, val error: E) : DataState<D, E>
    data class Loading<out D, out E : RootError>(val data: D? = null) : DataState<D, E>
}

/**
 * Invokes [action] for a successful result and returns this unchanged for chaining.
 */
inline fun <D, E : RootError> DataState<D, E>.onSuccess(action: (D) -> Unit): DataState<D, E> {
    return when (this) {
        is DataState.Success -> {
            action(data)
            this
        }

        else -> this
    }
}

/**
 * Invokes [action] for an error result and returns this unchanged for chaining.
 */
inline fun <D, E : RootError> DataState<D, E>.onFailure(action: (E) -> Unit): DataState<D, E> {
    return when (this) {
        is DataState.Error -> {
            action(error)
            this
        }

        else -> this
    }
}

/**
 * Invokes [action] while loading, passing any available data, and returns this unchanged.
 */
inline fun <D, E : RootError> DataState<D, E>.onLoading(action: (D?) -> Unit): DataState<D, E> {
    return when (this) {
        is DataState.Loading -> {
            action(data)
            this
        }

        else -> this
    }
}
