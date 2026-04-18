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

package com.d4rk.android.apps.apptoolkit.core.di

import android.content.Context
import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.app.startup.utils.interfaces.providers.AppStartupProvider
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.adsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.appModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.appsListModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.dataStoreModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.onboardingModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules.startupModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.generalSettingsModule
import com.d4rk.android.apps.apptoolkit.core.di.modules.settings.modules.hostSettingsProvidersModule
import com.d4rk.android.apps.apptoolkit.core.utils.constants.help.HelpConstants
import com.d4rk.android.libs.apptoolkit.core.di.model.AppToolkitHostBuildConfig
import com.d4rk.android.libs.apptoolkit.core.di.modules.appToolkitFeatureModules
import com.d4rk.android.libs.apptoolkit.core.di.modules.appToolkitFoundationModules
import com.d4rk.android.libs.apptoolkit.core.di.modules.appToolkitSettingsModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initializeKoin(context: Context) {
    val appToolkitBuildConfig = AppToolkitHostBuildConfig(
        applicationId = BuildConfig.APPLICATION_ID,
        isDebugBuild = BuildConfig.DEBUG,
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE.toLong(),
        githubToken = BuildConfig.GITHUB_TOKEN,
        faqProductId = HelpConstants.FAQ_PRODUCT_ID,
    )

    startKoin {
        androidContext(androidContext = context)
        modules(
            modules = buildList {
                addAll(appToolkitFoundationModules(hostBuildConfig = appToolkitBuildConfig))
                add(dataStoreModule)
                add(appModule)
                add(hostSettingsProvidersModule)
                addAll(appToolkitSettingsModules())
                add(generalSettingsModule)
                add(adsModule)
                add(appsListModule)
                addAll(
                    appToolkitFeatureModules(
                        hostBuildConfig = appToolkitBuildConfig,
                        startupProviderFactory = ::AppStartupProvider,
                    )
                )
                add(onboardingModule)
                add(startupModule)
            }
        )
    }
}
