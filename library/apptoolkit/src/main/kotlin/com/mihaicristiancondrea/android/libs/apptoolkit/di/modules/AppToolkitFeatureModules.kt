/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.di.modules

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.github.GithubConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di.aboutModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.di.helpModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.di.issueReporterModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.di.onboardingModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.permissions.di.permissionsModule
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.support.di.supportModule
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.di.reviewModule
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Assembles the library-owned feature DI modules for the host application's Koin bootstrap.
 * Feature bindings remain in the corresponding feature module; this function only composes them.
 */
fun appToolkitFeatureModules(
    hostBuildConfig: AppToolkitHostBuildConfig,
    startupProviderFactory: () -> StartupProvider,
): List<Module> = listOf(
    appToolkitCoreModule(hostBuildConfig = hostBuildConfig),
    onboardingModule(startupProviderFactory = startupProviderFactory),
    supportModule,
    helpModule(hostBuildConfig = hostBuildConfig),
    aboutModule,
    issueReporterModule(hostBuildConfig = hostBuildConfig),
    permissionsModule,
    reviewModule,
)

private fun appToolkitCoreModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<AppVersionInfo> {
        AppVersionInfo(
            versionName = hostBuildConfig.versionName,
            versionCode = hostBuildConfig.versionCode,
        )
    }
    single<String>(qualifier = named(AppToolkitDiConstants.GITHUB_REPOSITORY)) {
        hostBuildConfig.githubRepository
    }
    single<String>(qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG)) {
        GithubConstants.githubChangelog(get<String>(named(AppToolkitDiConstants.GITHUB_REPOSITORY)))
    }
}
