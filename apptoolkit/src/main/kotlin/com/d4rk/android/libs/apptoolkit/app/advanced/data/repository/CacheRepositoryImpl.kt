package com.d4rk.android.libs.apptoolkit.app.advanced.data.repository

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.advanced.domain.repository.CacheRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class CacheRepositoryImpl(
    private val context : Context ,
) : CacheRepository {

    override fun clearCache() : Flow<Result<Unit>> = flow {
        val cacheDirs : List<File> = buildList {
            add(context.cacheDir)
            add(context.codeCacheDir)
            context.externalCacheDir?.let(::add)
        }.distinct()

        val failed = cacheDirs.filterNot { it.deleteRecursively() }

        emit(if (failed.isEmpty()) Result.Success(Unit) else Result.Error(Exception("Failed to clear cache")))
    }
}