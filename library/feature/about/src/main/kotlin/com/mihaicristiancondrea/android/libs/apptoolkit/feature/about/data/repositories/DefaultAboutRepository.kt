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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.BuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.providers.GooglePlayServicesVersionProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.models.AboutInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.providers.AboutSettingsProvider

/**
 * Provides the raw application and device metadata shown on the About screen.
 *
 * Turning that metadata into rendered entries is the UI layer's responsibility, and copying it is a
 * presentation interaction owned by the ViewModel, so this repository holds neither.
 *
 * @param deviceProvider Supplies the host-formatted device report.
 * @param buildInfoProvider Supplies the host application version name and code.
 * @param gmsVersionProvider Supplies the installed Google Play services runtime version, if present.
 * @param toolkitVersionProvider Supplies the App Toolkit library publishing version.
 */
class DefaultAboutRepository(
    private val deviceProvider: AboutSettingsProvider,
    private val buildInfoProvider: BuildInfoProvider,
    private val firebaseController: FirebaseController,
    private val gmsVersionProvider: GooglePlayServicesVersionProvider,
    private val toolkitVersionProvider: () -> String = { BuildConfig.APP_TOOLKIT_VERSION },
) : AboutRepository {

    override suspend fun getAboutInfo(): AboutInfo {
        firebaseController.logBreadcrumb(
            message = "About info load started",
            attributes = mapOf("source" to "AboutRepository"),
        )
        return AboutInfo(
            appVersion = buildInfoProvider.appVersion,
            appVersionCode = buildInfoProvider.appVersionCode,
            appToolkitVersion = toolkitVersionProvider(),
            googlePlayServicesVersion = gmsVersionProvider.getVersion(),
            deviceInfo = deviceProvider.deviceInfo,
        )
    }
}
