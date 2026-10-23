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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.gms.data.repositories.GooglePlayServicesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.local.AboutDeviceInfoDataSource

/**
 * Provides the raw application and device metadata shown on the About screen.
 *
 * Turning that metadata into rendered entries is the UI layer's responsibility, and copying it is a
 * presentation interaction owned by the ViewModel, so this repository holds neither.
 *
 * @param deviceInfoDataSource Supplies the host-formatted device report.
 * @param buildInfoRepository Supplies the host application version name and code.
 * @param googlePlayServices Supplies the installed Google Play services runtime version, if present.
 * @param toolkitVersionProvider Supplies the App Toolkit library publishing version.
 */
class DefaultAboutRepository(
    private val deviceInfoDataSource: AboutDeviceInfoDataSource,
    private val buildInfoRepository: BuildInfoRepository,
    private val diagnostics: DiagnosticsReporter,
    private val googlePlayServices: GooglePlayServicesRepository,
    private val toolkitVersionProvider: () -> String = { BuildConfig.APP_TOOLKIT_VERSION },
) : AboutRepository {

    override suspend fun getAboutInfo(): AboutInfo {
        diagnostics.logBreadcrumb(
            message = "About info load started",
            attributes = mapOf("source" to "AboutRepository"),
        )
        return AboutInfo(
            appVersion = buildInfoRepository.appVersion,
            appVersionCode = buildInfoRepository.appVersionCode,
            appToolkitVersion = toolkitVersionProvider(),
            googlePlayServicesVersion = googlePlayServices.getVersion(),
            deviceInfo = deviceInfoDataSource.deviceInfo,
        )
    }
}
