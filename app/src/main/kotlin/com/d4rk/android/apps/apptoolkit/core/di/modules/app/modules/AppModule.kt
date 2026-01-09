package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.app.main.ui.MainViewModel
import com.d4rk.android.libs.apptoolkit.app.main.data.repository.MainRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.main.domain.repository.NavigationRepository
import com.d4rk.android.libs.apptoolkit.core.utils.constants.api.ApiLanguages
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.boolean.toApiEnvironment
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.developerAppsApiUrl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule: Module = module {
    single<NavigationRepository> { MainRepositoryImpl(dispatchers = get()) }
    viewModel { MainViewModel(navigationRepository = get(), firebaseController = get()) }

    single<String>(qualifier = named(name = "developer_apps_api_url")) {
        val environment = BuildConfig.DEBUG.toApiEnvironment()
        environment.developerAppsApiUrl(language = ApiLanguages.DEFAULT)
    }
}
