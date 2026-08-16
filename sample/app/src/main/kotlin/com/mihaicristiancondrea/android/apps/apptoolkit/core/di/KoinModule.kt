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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.di

import android.content.Context
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.app.startup.utils.interfaces.providers.AppStartupProvider
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.adsModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.appModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.appsListModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.dataStoreModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.onboardingModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.app.modules.startupModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.settings.modules.generalSettingsModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.di.modules.settings.modules.hostSettingsProvidersModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.utils.constants.help.HelpConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.di.modules.appToolkitModules
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
                // The toolkit's own graph, complete. Everything after this overrides it.
                addAll(
                    appToolkitModules(
                        hostBuildConfig = appToolkitBuildConfig,
                        startupProviderFactory = ::AppStartupProvider,
                    )
                )
                add(dataStoreModule)
                add(appModule)
                add(hostSettingsProvidersModule)
                add(generalSettingsModule)
                add(adsModule)
                add(appsListModule)
                add(onboardingModule)
                add(startupModule)
            }
        )
    }
}
