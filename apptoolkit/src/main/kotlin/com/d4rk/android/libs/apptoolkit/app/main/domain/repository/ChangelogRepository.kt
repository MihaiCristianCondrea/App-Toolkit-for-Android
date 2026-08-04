/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.libs.apptoolkit.app.main.domain.repository

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/** Provides Markdown changelog content for a host application package. */
interface ChangelogRepository {

    /** Fetches the preferred changelog for [packageName], applying the data-layer fallback. */
    fun fetchChangelog(packageName: String): Flow<DataState<String, Errors>>
}
