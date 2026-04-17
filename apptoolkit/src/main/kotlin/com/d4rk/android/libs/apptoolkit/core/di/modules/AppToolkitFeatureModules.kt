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

import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.repository.FaqRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpViewModel
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.local.DeviceInfoLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.repository.IssueReporterRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterViewModel
import com.d4rk.android.libs.apptoolkit.app.review.data.repository.ReviewRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.ForceInAppReviewUseCase
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import com.d4rk.android.libs.apptoolkit.app.startup.ui.StartupViewModel
import com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportViewModel
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.AppToolkitDiConstants
import com.d4rk.android.libs.apptoolkit.core.di.GithubToken
import com.d4rk.android.libs.apptoolkit.core.di.model.AppToolkitHostBuildConfig
import com.d4rk.android.libs.apptoolkit.core.ui.model.AppVersionInfo
import com.d4rk.android.libs.apptoolkit.core.utils.constants.github.GithubConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.help.HelpConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.faqCatalogUrl
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.toToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

private val githubTokenQualifier = qualifier<GithubToken>()

/**
 * Returns AppToolkit feature modules that can be loaded by the host app's Koin bootstrap.
 *
 * Change rationale:
 * - Before: these bindings lived in the sample app module, coupling toolkit DI to a specific host.
 * - After: bindings live in the library and only consume host values through [hostBuildConfig] and
 *   [startupProviderFactory], making integration cleaner for any host app.
 */
fun appToolkitFeatureModules(
    hostBuildConfig: AppToolkitHostBuildConfig,
    startupProviderFactory: () -> StartupProvider,
): List<Module> = listOf(
    appToolkitCoreModule(hostBuildConfig = hostBuildConfig, startupProviderFactory = startupProviderFactory),
    supportModule(),
    helpModule(hostBuildConfig = hostBuildConfig),
    issueReporterModule(hostBuildConfig = hostBuildConfig),
    reviewModule(),
)

private fun appToolkitCoreModule(
    hostBuildConfig: AppToolkitHostBuildConfig,
    startupProviderFactory: () -> StartupProvider,
): Module = module {
    single<StartupProvider> { startupProviderFactory() }
    single<AppVersionInfo> {
        AppVersionInfo(
            versionName = hostBuildConfig.versionName,
            versionCode = hostBuildConfig.versionCode,
        )
    }

    viewModel {
        StartupViewModel(firebaseController = get())
    }
}

private fun supportModule(): Module = module {
    single<BillingRepository>(createdAtStart = true) {
        val dispatchers = get<DispatcherProvider>()
        BillingRepository.getInstance(
            context = get(),
            dispatchers = dispatchers,
            firebaseController = get(),
            externalScope = CoroutineScope(SupervisorJob() + dispatchers.io),
        )
    }

    viewModel {
        SupportViewModel(billingRepository = get(), firebaseController = get())
    }
}

private fun helpModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<HelpLocalDataSource> { HelpLocalDataSource(context = get()) }
    single<HelpRemoteDataSource> { HelpRemoteDataSource(client = get()) }
    single<FaqRepository> {
        FaqRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            catalogUrl = HelpConstants.FAQ_BASE_URL.faqCatalogUrl(
                isDebugBuild = hostBuildConfig.isDebugBuild,
            ),
            productId = hostBuildConfig.faqProductId,
            firebaseController = get(),
        )
    }
    single<GetFaqUseCase> { GetFaqUseCase(repository = get()) }

    viewModel {
        HelpViewModel(
            getFaqUseCase = get(),
            forceInAppReviewUseCase = get<ForceInAppReviewUseCase>(),
            dispatchers = get<DispatcherProvider>(),
            firebaseController = get(),
        )
    }
}

private fun issueReporterModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<IssueReporterRemoteDataSource> { IssueReporterRemoteDataSource(client = get()) }
    single<DeviceInfoProvider> { DeviceInfoLocalDataSource(get(), get()) }
    single<IssueReporterRepository> { IssueReporterRepositoryImpl(get(), get(), get()) }
    single<SendIssueReportUseCase> { SendIssueReportUseCase(get(), get(), get()) }
    single<String>(qualifier = named(name = AppToolkitDiConstants.GITHUB_REPOSITORY)) {
        hostBuildConfig.githubRepository
    }
    single<GithubTarget> {
        GithubTarget(
            username = GithubConstants.GITHUB_USER,
            repository = get(qualifier = named(AppToolkitDiConstants.GITHUB_REPOSITORY)),
        )
    }
    single<String>(qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG)) {
        GithubConstants.githubChangelog(get<String>(named(AppToolkitDiConstants.GITHUB_REPOSITORY)))
    }
    single<String>(githubTokenQualifier) { hostBuildConfig.githubToken.toToken() }

    viewModel {
        IssueReporterViewModel(
            sendIssueReport = get(),
            githubTarget = get(),
            githubToken = get(githubTokenQualifier),
            deviceInfoProvider = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}

private fun reviewModule(): Module = module {
    single<ReviewRepository> { ReviewRepositoryImpl(dataStore = get()) }
    single<RequestInAppReviewUseCase> { RequestInAppReviewUseCase(reviewRepository = get()) }
    single<ForceInAppReviewUseCase> { ForceInAppReviewUseCase(reviewRepository = get()) }
}
