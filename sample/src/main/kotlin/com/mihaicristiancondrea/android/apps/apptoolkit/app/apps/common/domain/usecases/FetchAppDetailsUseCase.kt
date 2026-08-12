/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.usecases

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.model.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.repository.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.Flow

/** Loads the complete public metadata document for one application package. */
class FetchAppDetailsUseCase(private val repository: DeveloperAppsRepository) {

    /** Delegates package-specific metadata loading to the domain repository. */
    operator fun invoke(packageName: String): Flow<DataState<AppDetails, AppErrors>> =
        repository.fetchAppDetails(packageName)
}
