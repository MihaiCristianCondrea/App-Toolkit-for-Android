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

package com.mihaicristiancondrea.android.apps.apptoolkit.di

import android.content.Context
import com.mihaicristiancondrea.android.apps.apptoolkit.BuildConfig
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.di.appsFeatureModule
import com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.di.settingsFeatureModule
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.di.tilesFeatureModule
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.di.componentsFeatureModule
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.di.adsIntegrationModule
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.di.onboardingFeatureModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.data.local.datastore.di.dataStoreModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.di.navigationModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.shell.di.shellModule
import com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.di.appToolkitHostModules
import com.mihaicristiancondrea.android.apps.apptoolkit.core.utils.constants.help.HelpConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/** The exact Koin graph started by the sample application. */
fun sampleAppModules(hostBuildConfig: AppToolkitHostBuildConfig): List<Module> = buildList {
    addAll(appToolkitHostModules(hostBuildConfig = hostBuildConfig))
    add(dataStoreModule)
    add(navigationModule)
    add(shellModule)
    add(appModule)
    add(settingsFeatureModule)
    add(tilesFeatureModule)
    add(appsFeatureModule)
    add(componentsFeatureModule)
    add(adsIntegrationModule)
    add(onboardingFeatureModule)
}

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
        modules(modules = sampleAppModules(hostBuildConfig = appToolkitBuildConfig))
    }
}
