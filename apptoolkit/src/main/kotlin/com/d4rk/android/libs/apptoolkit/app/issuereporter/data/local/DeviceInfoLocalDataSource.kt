package com.d4rk.android.libs.apptoolkit.app.issuereporter.data.local

import android.app.Application
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.DeviceInfo
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.withContext

class DeviceInfoLocalDataSource(
    private val app: Application,
    private val dispatchers: DispatcherProvider,
) : DeviceInfoProvider {
    override suspend fun capture(): DeviceInfo = withContext(dispatchers.io) {
        DeviceInfo.create(app)
    }
}
