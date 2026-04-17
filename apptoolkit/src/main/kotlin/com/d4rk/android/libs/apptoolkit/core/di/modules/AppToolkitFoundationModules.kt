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

package com.d4rk.android.libs.apptoolkit.core.di.modules

import com.d4rk.android.libs.apptoolkit.app.consent.data.local.ConsentPreferencesDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.UmpConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.repository.ConsentRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.ApplyConsentSettingsUseCase
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.ApplyInitialConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.InAppUpdateRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.InAppUpdateRepository
import com.d4rk.android.libs.apptoolkit.app.main.domain.usecases.RequestInAppUpdateUseCase
import com.d4rk.android.libs.apptoolkit.app.main.ui.factory.GmsHostFactory
import com.d4rk.android.libs.apptoolkit.app.ads.data.repository.AdsSettingsRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.ads.domain.repository.AdsSettingsRepository
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.ObserveAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.domain.usecases.SetAdsEnabledUseCase
import com.d4rk.android.libs.apptoolkit.app.ads.ui.AdsSettingsViewModel
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.AppToolkitDiConstants
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.StandardDispatchers
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.remote.ads.AdsCoreManager
import com.d4rk.android.libs.apptoolkit.core.data.remote.client.KtorClient
import com.d4rk.android.libs.apptoolkit.core.data.remote.firebase.FirebaseControllerImpl
import com.d4rk.android.libs.apptoolkit.core.di.model.AppToolkitHostBuildConfig
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.developerAppsApiUrl
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
fun appToolkitFoundationModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> = listOf(
    dispatchersModule(),
    corePlatformModule(hostBuildConfig = hostBuildConfig),
    consentModule(),
    mainSharedModule(hostBuildConfig = hostBuildConfig),
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
    single<AdsCoreManager> {
        AdsCoreManager(
            context = get(),
            buildInfoProvider = get(),
            dispatchers = get(),
        )
    }
    single<FirebaseController> { FirebaseControllerImpl() }
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
    single<ConsentPreferencesDataSource> { get<CommonDataStore>() }
    single<ConsentRemoteDataSource> { UmpConsentRemoteDataSource() }
    single<ConsentRepository> {
        ConsentRepositoryImpl(
            remote = get(),
            local = get(),
            configProvider = get(),
            firebaseController = get(),
        )
    }
    single { RequestConsentUseCase(repository = get(), firebaseController = get()) }
    single { ApplyInitialConsentUseCase(repository = get(), firebaseController = get()) }
    single { ApplyConsentSettingsUseCase(repository = get(), firebaseController = get()) }
}

private fun mainSharedModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single { GmsHostFactory() }
    single<InAppUpdateRepository> { InAppUpdateRepositoryImpl() }
    single { RequestInAppUpdateUseCase(repository = get()) }
    single<String>(qualifier = named(name = AppToolkitDiConstants.DEVELOPER_APPS_API_URL)) {
        hostBuildConfig.isDebugBuild
            .toApiEnvironment()
            .developerAppsApiUrl(language = ApiLanguages.DEFAULT)
    }
}

private fun adsSettingsSharedModule(): Module = module {
    single<AdsSettingsRepository> {
        AdsSettingsRepositoryImpl(
            dataStore = get(),
            buildInfoProvider = get(),
            firebaseController = get(),
        )
    }
    single<ObserveAdsEnabledUseCase> {
        ObserveAdsEnabledUseCase(
            repo = get(),
            firebaseController = get(),
        )
    }
    single<SetAdsEnabledUseCase> { SetAdsEnabledUseCase(repo = get(), firebaseController = get()) }
    viewModel {
        AdsSettingsViewModel(
            repository = get(),
            dispatchers = get(),
            observeAdsEnabled = get(),
            setAdsEnabled = get(),
            requestConsentUseCase = get(),
            firebaseController = get(),
        )
    }
}
