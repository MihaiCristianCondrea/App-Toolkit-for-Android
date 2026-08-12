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

import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repository.AdsSettingsRepositoryImpl
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.ui.AdsSettingsViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.remote.datasource.UmpConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.data.repository.ConsentRepositoryImpl
import com.mihaicristiancondrea.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.factory.GmsHostFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.StandardDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.AdsCoreManager
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.interfaces.ConsentPreferencesDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.client.KtorClient
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.model.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.repository.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.FirebaseControllerImpl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.api.ApiHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.ManifestAdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.data.repository.InAppUpdateRepositoryImpl
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.update.domain.repository.InAppUpdateRepository
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
        corePlatformModule(hostBuildConfig = hostBuildConfig),
        consentModule(),
        mainSharedModule(),
        adsSettingsSharedModule(),
    )

private fun dispatchersModule(): Module = module {
    single<DispatcherProvider> { StandardDispatchers() }
}

private fun corePlatformModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<CommonDataStore> {
        CommonDataStore(
            context = get(),
            dispatchers = get(),
            defaultAdsEnabled = !hostBuildConfig.isDebugBuild,
        )
    }
    single<AdMobAppIdProvider> { ManifestAdMobAppIdProvider(context = get()) }
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get(),
            adMobAppIdProvider = get(),
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
        ConsentRepositoryImpl(
            remote = get(),
            local = get(),
            configProvider = get(),
            firebaseController = get(),
        )
    }
}

private fun mainSharedModule(): Module = module {
    single { GmsHostFactory() } // Lightweight creator without screen references; safe as singleton.
    single<InAppUpdateRepository> { InAppUpdateRepositoryImpl() }
    single<String>(qualifier = named(name = AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL)) {
        ApiHost.BASE_URL
    }
}

private fun adsSettingsSharedModule(): Module = module {
    single<AdsSettingsRepository> {
        AdsSettingsRepositoryImpl(
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



