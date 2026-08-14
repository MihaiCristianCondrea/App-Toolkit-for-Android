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

/** Persistent source for the last successfully downloaded developer-app catalogue. */
interface DeveloperAppsLocalDataSource {
    suspend fun read(): AppsListResponseDto?

    suspend fun write(value: AppsListResponseDto)
}
