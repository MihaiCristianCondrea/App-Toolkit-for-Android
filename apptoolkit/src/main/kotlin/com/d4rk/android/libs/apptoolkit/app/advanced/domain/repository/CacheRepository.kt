package com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for cache-related operations.
 */
interface CacheRepository {
    /**
     * Clears the application's cache directories.
     *
     * @return A [Flow] emitting [Result.Success] if all directories were deleted successfully or
     * [Result.Error] with the encountered [Exception].
     */
    fun clearCache(): Flow<Result<Unit>>
}
