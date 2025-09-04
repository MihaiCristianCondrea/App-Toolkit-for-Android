package com.d4rk.android.libs.apptoolkit.app.about.data

import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.about.domain.model.ui.UiAboutScreen
import com.d4rk.android.libs.apptoolkit.app.about.domain.repository.AboutRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AboutSettingsProvider
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.ClipboardHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Default implementation of [AboutRepository] that gathers device and build
 * information on an I/O dispatcher.
 */
class DefaultAboutRepository(
    private val deviceProvider: AboutSettingsProvider,
    private val configProvider: BuildInfoProvider,
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : AboutRepository {

    override fun getAboutInfoStream(): Flow<UiAboutScreen> =
        flow {
            emit(
                UiAboutScreen(
                    appVersion = configProvider.appVersion,
                    appVersionCode = configProvider.appVersionCode,
                    deviceInfo = deviceProvider.deviceInfo,
                ),
            )
        }.flowOn(dispatchers.io)

    override suspend fun copyDeviceInfo(label: String, deviceInfo: String) {
        withContext(dispatchers.main) {
            ClipboardHelper.copyTextToClipboard(
                context = context,
                label = label,
                text = deviceInfo,
            )
        }
    }
}
