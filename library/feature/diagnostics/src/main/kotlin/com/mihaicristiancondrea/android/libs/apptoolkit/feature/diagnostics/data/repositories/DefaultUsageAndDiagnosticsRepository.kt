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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.interfaces.UsageAndDiagnosticsPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
 */
class DefaultUsageAndDiagnosticsRepository(
    private val dataSource: UsageAndDiagnosticsPreferencesDataSource,
    private val configProvider: BuildInfoProvider,
    private val dispatchers: DispatcherProvider,
    private val firebaseController: FirebaseController,
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
                firebaseController.logBreadcrumb(
                    message = "Usage diagnostics observe",
                    attributes = mapOf("defaultEnabled" to (!configProvider.isDebugBuild).toString()),
                )
            }
            .flowOn(dispatchers.io)

    override suspend fun setUsageAndDiagnostics(enabled: Boolean) =
        withContext(dispatchers.io) {
            firebaseController.logBreadcrumb(
                message = "Usage diagnostics updated",
                attributes = mapOf("usageAndDiagnostics" to enabled.toString()),
            )
            dataSource.saveUsageAndDiagnostics(isChecked = enabled)
        }

    override suspend fun setAnalyticsConsent(granted: Boolean) =
        withContext(dispatchers.io) {
            firebaseController.logBreadcrumb(
                message = "Analytics consent updated",
                attributes = mapOf("granted" to granted.toString()),
            )
            dataSource.saveAnalyticsConsent(isGranted = granted)
        }

    override suspend fun setAdStorageConsent(granted: Boolean) =
        withContext(dispatchers.io) {
            firebaseController.logBreadcrumb(
                message = "Ad storage consent updated",
                attributes = mapOf("granted" to granted.toString()),
            )
            dataSource.saveAdStorageConsent(isGranted = granted)
        }

    override suspend fun setAdUserDataConsent(granted: Boolean) =
        withContext(dispatchers.io) {
            firebaseController.logBreadcrumb(
                message = "Ad user data consent updated",
                attributes = mapOf("granted" to granted.toString()),
            )
            dataSource.saveAdUserDataConsent(isGranted = granted)
        }

    override suspend fun setAdPersonalizationConsent(granted: Boolean) =
        withContext(dispatchers.io) {
            firebaseController.logBreadcrumb(
                message = "Ad personalization consent updated",
                attributes = mapOf("granted" to granted.toString()),
            )
            dataSource.saveAdPersonalizationConsent(isGranted = granted)
        }
}


