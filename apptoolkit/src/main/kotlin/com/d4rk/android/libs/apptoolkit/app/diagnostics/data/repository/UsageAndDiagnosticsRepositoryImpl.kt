package com.d4rk.android.libs.apptoolkit.app.diagnostics.data.repository

import com.d4rk.android.libs.apptoolkit.app.diagnostics.data.local.UsageAndDiagnosticsPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.model.UsageAndDiagnosticsSettings
import com.d4rk.android.libs.apptoolkit.app.diagnostics.domain.repository.UsageAndDiagnosticsRepository
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class UsageAndDiagnosticsRepositoryImpl(
    private val dataSource: UsageAndDiagnosticsPreferencesDataSource,
    private val configProvider: BuildInfoProvider,
    private val dispatchers: DispatcherProvider,
) : UsageAndDiagnosticsRepository {

    override fun observeSettings(): Flow<UsageAndDiagnosticsSettings> =
        combine(
            dataSource.usageAndDiagnostics(default = !configProvider.isDebugBuild),
            dataSource.analyticsConsent(default = !configProvider.isDebugBuild),
            dataSource.adStorageConsent(default = !configProvider.isDebugBuild),
            dataSource.adUserDataConsent(default = !configProvider.isDebugBuild),
            dataSource.adPersonalizationConsent(default = !configProvider.isDebugBuild),
        ) { usage, analytics, adStorage, adUserData, adPersonalization ->
            UsageAndDiagnosticsSettings(
                usageAndDiagnostics = usage,
                analyticsConsent = analytics,
                adStorageConsent = adStorage,
                adUserDataConsent = adUserData,
                adPersonalizationConsent = adPersonalization,
            )
        }.flowOn(dispatchers.io)

    override suspend fun setUsageAndDiagnostics(enabled: Boolean) =
        withContext(dispatchers.io) { dataSource.saveUsageAndDiagnostics(isChecked = enabled) }

    override suspend fun setAnalyticsConsent(granted: Boolean) =
        withContext(dispatchers.io) { dataSource.saveAnalyticsConsent(isGranted = granted) }

    override suspend fun setAdStorageConsent(granted: Boolean) =
        withContext(dispatchers.io) { dataSource.saveAdStorageConsent(isGranted = granted) }

    override suspend fun setAdUserDataConsent(granted: Boolean) =
        withContext(dispatchers.io) { dataSource.saveAdUserDataConsent(isGranted = granted) }

    override suspend fun setAdPersonalizationConsent(granted: Boolean) =
        withContext(dispatchers.io) { dataSource.saveAdPersonalizationConsent(isGranted = granted) }
}
