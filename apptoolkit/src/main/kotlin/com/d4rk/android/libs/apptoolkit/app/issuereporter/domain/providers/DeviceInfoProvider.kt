package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers

import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo

interface DeviceInfoProvider {
    suspend fun capture(): DeviceInfo
}
