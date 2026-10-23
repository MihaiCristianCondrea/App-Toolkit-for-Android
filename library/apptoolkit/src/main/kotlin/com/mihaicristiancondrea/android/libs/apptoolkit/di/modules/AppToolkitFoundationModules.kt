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

package com.mihaicristiancondrea.android.libs.apptoolkit.di.modules

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AdLoadReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.DefaultClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.telemetryModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.AdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.BuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.ManifestAdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.di.dataStoreModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.network.data.remote.client.KtorClient
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.di.adsIntegrationModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.di.consentModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.update.di.updateModule
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.DefaultBuildInfoRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.DefaultInstalledAppsRepository
import org.koin.dsl.module
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.gms.di.googlePlayServicesModule

/**
 * Assembles shared providers, networking, and ads, consent, GMS, and update integrations for the
 * host graph. Preference bindings come once from `:library:core:datastore`, and shared ad
 * reporting is supplied here rather than required from each host.
 */
fun appToolkitFoundationModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> =
    listOf(
        dispatchersModule(),
        telemetryModule,
        dataStoreModule(),
        corePlatformModule(hostBuildConfig = hostBuildConfig),
        consentModule(),
        mainSharedModule(),
        adsIntegrationModule(),
        updateModule(),
        googlePlayServicesModule,
    )

private fun dispatchersModule(): Module = module {
    single<DispatcherProvider> { StandardDispatchers() }
}

private fun corePlatformModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<InstalledAppsRepository> { DefaultInstalledAppsRepository(context = get(), dispatchers = get()) }
    single<AdMobAppIdDataSource> { ManifestAdMobAppIdDataSource(context = get(), dispatchers = get()) }
    single<ClipboardRepository> { DefaultClipboardRepository(context = get()) }
    single { AdLoadReporter(telemetryRepository = get(), buildInfoRepository = get()) }
    single { KtorClient.createClient(enableLogging = hostBuildConfig.isDebugBuild) }
    single<BuildInfoRepository> { DefaultBuildInfoRepository(config = hostBuildConfig) }
}

private fun mainSharedModule(): Module = module {
    single<String>(qualifier = named(name = AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL)) {
        ApiHost.BASE_URL
    }
}
