package com.d4rk.android.libs.apptoolkit.app.advanced.data

import android.content.Context
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

/**
 * Default implementation of [CacheRepository] that clears cache directories using the provided [Context].
 */
class DefaultCacheRepository(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : CacheRepository {

    override fun clearCache(): Flow<Result<Unit>> = flow {
        val cacheDirectories: List<File> = listOf(
            context.cacheDir,
            context.codeCacheDir,
            context.filesDir,
        )

        val result = runCatching {
            val allDeleted = coroutineScope {
                cacheDirectories
                    .map { directory -> async { directory.deleteRecursively() } }
                    .awaitAll()
                    .all { it }
            }
            if (!allDeleted) {
                throw Exception("Failed to clear cache")
            }
        }
        emit(
            result.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(it as Exception) }
        ))
    }
        .catch { e -> emit(Result.Error(e as Exception)) }
        .flowOn(dispatchers.io)
}