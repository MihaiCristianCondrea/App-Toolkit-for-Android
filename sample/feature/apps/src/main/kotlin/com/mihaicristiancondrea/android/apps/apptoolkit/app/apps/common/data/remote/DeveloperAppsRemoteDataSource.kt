package com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.remote

import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppDetails
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppSummary

/** Network source for the developer-app catalogue and package details. */
interface DeveloperAppsRemoteDataSource {
    suspend fun fetchDeveloperApps(): List<AppSummary>

    suspend fun fetchAppDetails(packageName: String): AppDetails
}

enum class DeveloperAppsRemoteError {
    RequestTimeout,
    RateLimited,
    Redirect,
    Client,
    Server,
    NoInternet,
    Connection,
    Serialization,
    Unknown,
}

class DeveloperAppsRemoteException(
    val error: DeveloperAppsRemoteError,
    cause: Throwable? = null,
) : Exception(error.name, cause)
