package com.d4rk.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

/**
 * Implementation of the [CacheRepository] interface.
 * This class handles the logic for clearing the application's cache directories.
 *
 * @property context The application context used to access cache directories.
 */
class CacheRepositoryImpl(
    private val context: Context,
    private val firebaseController: FirebaseController,
) : CacheRepository {

    override fun clearCache(): Flow<Result<Unit>> = flow {
        firebaseController.logBreadcrumb(
            message = "Cache clear requested",
            attributes = mapOf("source" to "CacheRepositoryImpl"),
        )
        val cacheDirs: List<File> = buildList {
            add(context.cacheDir)
            add(context.codeCacheDir)
            context.externalCacheDir?.let(::add)
        }.distinct()

        val failed = cacheDirs.filterNot { it.deleteRecursively() }

        emit(if (failed.isEmpty()) Result.Success(Unit) else Result.Error(Exception("Failed to clear cache")))
    }
}
