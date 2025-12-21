package com.d4rk.android.libs.apptoolkit.core.domain.model

/**
 * Simple wrapper for operation results.
 */
sealed class Result<out T> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
