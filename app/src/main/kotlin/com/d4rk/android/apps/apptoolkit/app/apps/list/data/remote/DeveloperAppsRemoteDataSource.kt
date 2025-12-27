package com.d4rk.android.apps.apptoolkit.app.apps.list.data.remote

import com.d4rk.android.apps.apptoolkit.app.apps.list.data.model.ApiResponse

interface DeveloperAppsRemoteDataSource {
    suspend fun fetchDeveloperApps(): ApiResponse
}