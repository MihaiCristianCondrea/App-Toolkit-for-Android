package com.d4rk.android.libs.apptoolkit.app.about.domain.repository

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.CopyDeviceInfoResult

/**
 * Repository interface for managing and retrieving application-related information
 * and performing device-specific utility operations.
 */
interface AboutRepository {
    suspend fun getAboutInfo(): AboutInfo
    fun copyDeviceInfo(label: String, deviceInfo: String): CopyDeviceInfoResult
}
