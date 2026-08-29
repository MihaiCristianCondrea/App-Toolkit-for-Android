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

import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.managers.AdsCoreManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.AdLoadReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories.DefaultAdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.AdsSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.UmpConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repositories.DefaultConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.ManifestAdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.di.dataStoreModule
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.client.KtorClient
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.data.repositories.DefaultInAppUpdateRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.data.repositories.InAppUpdateRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * AppToolkit foundation modules to be loaded by the host app.
 *
 * Includes dispatchers, datastore, firebase, ktor, consent wiring, and shared main-feature
 * dependencies such as [GmsHostFactory].
 */
fun appToolkitFoundationModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> =
    listOf(
        dispatchersModule(),
        // :library:core:datastore owns the CommonDataStore definition and the preference
        // data-source bindings that hang off it. Including it here keeps one registration for
        // every host instead of a second copy in corePlatformModule.
        dataStoreModule(isDebugBuild = hostBuildConfig.isDebugBuild),
        corePlatformModule(hostBuildConfig = hostBuildConfig),
        consentModule(),
        mainSharedModule(),
        adsSettingsSharedModule(),
    )

private fun dispatchersModule(): Module = module {
    single<DispatcherProvider> { StandardDispatchers() }
}

private fun corePlatformModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<AdMobAppIdProvider> { ManifestAdMobAppIdProvider(context = get()) }
    // Every toolkit ad surface resolves this, so it is bound here rather than left to the host:
    // an unbound reporter would turn a blank ad slot into a crash, which is the opposite of the
    // point.
    single { AdLoadReporter(firebaseController = get(), buildInfoProvider = get()) }
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get(),
            adMobAppIdProvider = get(),
            // Injected so the manager reads the same CommonDataStore the rest of the graph uses.
            // Its default falls back to the static singleton, which is a second wrapper over the
            // same preferences file with its own eagerly started adsEnabledFlow.
            dataStore = get(),
        )
    }
    single { KtorClient.createClient(enableLogging = hostBuildConfig.isDebugBuild) }
    single<BuildInfoProvider> {
        object : BuildInfoProvider {
            override val appVersion: String = hostBuildConfig.versionName
            override val appVersionCode: Int = hostBuildConfig.versionCode.toInt()
            override val packageName: String = hostBuildConfig.applicationId
            override val isDebugBuild: Boolean = hostBuildConfig.isDebugBuild
        }
    }
}

private fun consentModule(): Module = module {
    single<ConsentRemoteDataSource> { UmpConsentRemoteDataSource(adMobAppIdProvider = get()) }
    single<ConsentRepository> {
        DefaultConsentRepository(
            remote = get(),
            local = get(),
            configProvider = get(),
            firebaseController = get(),
        )
    }
}

private fun mainSharedModule(): Module = module {
    single { GmsHostFactory() } // Lightweight creator without screen references; safe as singleton.
    single<InAppUpdateRepository> { DefaultInAppUpdateRepository() }
    single<String>(qualifier = named(name = AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL)) {
        ApiHost.BASE_URL
    }
}

private fun adsSettingsSharedModule(): Module = module {
    single<AdsSettingsRepository> {
        DefaultAdsSettingsRepository(
            dataStore = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        AdsSettingsViewModel(
            repository = get(),
            consentRepository = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}


