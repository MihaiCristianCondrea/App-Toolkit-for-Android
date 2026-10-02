/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.domain.models.UsageAndDiagnosticsSettings
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.UsageAndDiagnosticsPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.domain.models.ConsentSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/**
 * Implementation of [UsageAndDiagnosticsRepository] that manages user consent and diagnostic settings.
 *
 * This repositories coordinates the flow of data between the [UsageAndDiagnosticsPreferencesDataSource]
 * and the domain layer, ensuring that settings are persisted and retrieved correctly.
 * Default values for settings are determined based on the build type provided by [BuildInfoProvider].
 *
 * @property dataSource The local data source for persisting usage and diagnostics preferences.
 * @property configProvider Provider used to determine build-specific configurations like debug status.
 * @property dispatchers Provider for coroutine dispatchers to ensure operations run on the appropriate thread.
 * @property consentRepository Applies the stored choices to the consent SDKs after every write.
 * Doing it here, rather than in a screen observing the settings, applies a change whether or not
 * that screen is still open, and applies a whole-bundle answer once, after it is stored.
 */
class DefaultUsageAndDiagnosticsRepository(
    private val dataSource: UsageAndDiagnosticsPreferencesDataSource,
    private val configProvider: BuildInfoProvider,
    private val dispatchers: DispatcherProvider,
    private val telemetryRepository: TelemetryRepository,
    private val consentRepository: ConsentRepository,
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
        }
            .onStart {
                telemetryRepository.logBreadcrumb(
                    message = "Usage diagnostics observe",
                    attributes = mapOf("defaultEnabled" to (!configProvider.isDebugBuild).toString()),
                )
            }
            .flowOn(dispatchers.io)

    override suspend fun setUsageAndDiagnostics(enabled: Boolean) =
        save(
            message = "Usage diagnostics updated",
            attributes = mapOf("usageAndDiagnostics" to enabled.toString()),
        ) { dataSource.saveUsageAndDiagnostics(isChecked = enabled) }

    override suspend fun setAnalyticsConsent(granted: Boolean) =
        save(message = "Analytics consent updated", attributes = mapOf("granted" to granted.toString())) {
            dataSource.saveAnalyticsConsent(isGranted = granted)
        }

    override suspend fun setAdStorageConsent(granted: Boolean) =
        save(message = "Ad storage consent updated", attributes = mapOf("granted" to granted.toString())) {
            dataSource.saveAdStorageConsent(isGranted = granted)
        }

    override suspend fun setAdUserDataConsent(granted: Boolean) =
        save(message = "Ad user data consent updated", attributes = mapOf("granted" to granted.toString())) {
            dataSource.saveAdUserDataConsent(isGranted = granted)
        }

    override suspend fun setAdPersonalizationConsent(granted: Boolean) =
        save(
            message = "Ad personalization consent updated",
            attributes = mapOf("granted" to granted.toString()),
        ) { dataSource.saveAdPersonalizationConsent(isGranted = granted) }

    override suspend fun setAll(settings: UsageAndDiagnosticsSettings) =
        save(
            message = "Usage diagnostics bundle updated",
            attributes = mapOf(
                "usageAndDiagnostics" to settings.usageAndDiagnostics.toString(),
                "analyticsConsent" to settings.analyticsConsent.toString(),
                "adStorageConsent" to settings.adStorageConsent.toString(),
                "adUserDataConsent" to settings.adUserDataConsent.toString(),
                "adPersonalizationConsent" to settings.adPersonalizationConsent.toString(),
            ),
        ) {
            dataSource.saveAll(
                usageAndDiagnostics = settings.usageAndDiagnostics,
                analyticsConsent = settings.analyticsConsent,
                adStorageConsent = settings.adStorageConsent,
                adUserDataConsent = settings.adUserDataConsent,
                adPersonalizationConsent = settings.adPersonalizationConsent,
            )
        }

    /** Logs [message], runs [write], then applies everything now stored to the consent SDKs. */
    private suspend fun save(
        message: String,
        attributes: Map<String, String>,
        write: suspend () -> Unit,
    ) {
        withContext(dispatchers.io) {
            telemetryRepository.logBreadcrumb(message = message, attributes = attributes)
            write()
        }
        val stored: UsageAndDiagnosticsSettings = observeSettings().first()
        consentRepository.applyConsentSettings(
            ConsentSettings(
                usageAndDiagnostics = stored.usageAndDiagnostics,
                analyticsConsent = stored.analyticsConsent,
                adStorageConsent = stored.adStorageConsent,
                adUserDataConsent = stored.adUserDataConsent,
                adPersonalizationConsent = stored.adPersonalizationConsent,
            )
        )
    }
}


