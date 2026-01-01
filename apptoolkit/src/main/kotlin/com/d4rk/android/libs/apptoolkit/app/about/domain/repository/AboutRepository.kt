package com.d4rk.android.libs.apptoolkit.app.about.domain.repository

import com.d4rk.android.libs.apptoolkit.app.about.domain.model.AboutInfo

interface AboutRepository {
    suspend fun getAboutInfo(): AboutInfo
    fun copyDeviceInfo(label: String, deviceInfo: String): Boolean
}