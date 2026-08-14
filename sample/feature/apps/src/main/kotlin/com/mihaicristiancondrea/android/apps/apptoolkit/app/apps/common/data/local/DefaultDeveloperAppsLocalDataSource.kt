/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.local

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote.models.AppsListResponseDto
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

/** JSON file-backed catalogue cache. Corrupt entries are discarded and treated as a cache miss. */
class DefaultDeveloperAppsLocalDataSource(
    private val cacheFile: File,
    private val json: Json,
    private val dispatchers: DispatcherProvider,
) : DeveloperAppsLocalDataSource {

    override suspend fun read(): AppsListResponseDto? = withContext(dispatchers.io) {
        if (!cacheFile.isFile) return@withContext null
        runCatching {
            json.decodeFromString<AppsListResponseDto>(cacheFile.readText())
        }.getOrElse {
            cacheFile.delete()
            null
        }
    }

    override suspend fun write(value: AppsListResponseDto): Unit = withContext(dispatchers.io) {
        cacheFile.parentFile?.mkdirs()
        val temporaryFile = File(cacheFile.parentFile, "${cacheFile.name}.tmp")
        temporaryFile.writeText(json.encodeToString(value))
        check(temporaryFile.renameTo(cacheFile)) {
            "Unable to replace developer apps cache"
        }
    }
}
