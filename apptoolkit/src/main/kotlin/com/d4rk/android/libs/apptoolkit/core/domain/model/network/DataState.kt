package com.d4rk.android.libs.apptoolkit.core.domain.model.network

typealias RootError = Error

/**
 * A sealed interface representing the different states of a data operation,
 * such as a network request or a database query. It helps manage UI states
 * based on the outcome of asynchronous operations.
 *
 * @param D The type of the successful data.
 * @param E The type of the error, which must be a subtype of [RootError].
 */
sealed interface DataState<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : DataState<D, E>
    data class Error<out D, out E : RootError>(val data: D? = null, val error: E) : DataState<D, E>
    data class Loading<out D, out E : RootError>(val data: D? = null) : DataState<D, E>
}

/**
 * Performs the given [action] if this [DataState] is [DataState.Success].
 * Returns the original [DataState] unchanged.
 *
 * @param action The action to be performed with the success data.
 * @return The original [DataState] instance.
 */
inline fun <D, E : RootError> DataState<D, E>.onSuccess(action: (D) -> Unit): DataState<D, E> { // TODO && FIXME: Use these across the entire library and app like shown in helper module
    return when (this) {
        is DataState.Success -> {
            action(data)
            this
        }

        else -> this
    }
}

/**
 * Executes the given [action] if the [DataState] is an [DataState.Error].
 * The action receives the error of type [E]. This function allows for handling
 * error cases in a chained manner.
 *
 * @param action The block of code to be executed with the error.
 * @return The original [DataState] instance, allowing for further chaining.
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
 * A chained function that is executed only when the [DataState] is [DataState.Loading].
 * It provides the nullable data that might be available during the loading state.
 *
 * @param action The block of code to be executed, receiving the optional data of type [D].
 * @return The original [DataState] instance, allowing for further chaining.
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