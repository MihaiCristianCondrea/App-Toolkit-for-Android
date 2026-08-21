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

import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.repositories.DefaultFaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.help.ui.HelpViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.local.DeviceInfoLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repositories.DefaultIssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.data.repositories.DefaultChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.main.ui.ChangelogViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.onboarding.ui.OnboardingThemeViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.ForceInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.ui.StartupViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.app.support.ui.SupportViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.GithubToken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.github.GithubConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.help.HelpConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.faqCatalogUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.toToken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.AppVersionInfo
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.review.data.repositories.DefaultReviewRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.playservices.review.data.repositories.ReviewRepository
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
    appToolkitCoreModule(
        hostBuildConfig = hostBuildConfig,
        startupProviderFactory = startupProviderFactory
    ),
    onboardingModule(),
    supportModule(),
    helpModule(hostBuildConfig = hostBuildConfig),
    changelogModule(),
    issueReporterModule(hostBuildConfig = hostBuildConfig),
    reviewModule(),
)

/**
 * DI lifetime checklist (see the Internal implementations section in this module's README):
 * - Use `single` for heavy/stateful managers (billing, datastore, network clients).
 * - Use `factory` for lightweight builders or objects that may later capture Activity/screen refs.
 * - Keep `viewModel` bindings unchanged because lifecycle is managed by Koin ViewModel DSL.
 * - Add one-line rationale for non-obvious bindings, especially `*Factory` types.
 */
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
    single<String>(qualifier = named(name = AppToolkitDiConstants.GITHUB_REPOSITORY)) {
        hostBuildConfig.githubRepository
    }
    single<String>(qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG)) {
        GithubConstants.githubChangelog(get<String>(named(AppToolkitDiConstants.GITHUB_REPOSITORY)))
    }

    viewModel {
        StartupViewModel(firebaseController = get())
    }
}

/**
 * Bindings for the onboarding pages the toolkit itself draws.
 *
 * [OnboardingThemeViewModel] is resolved by the library's own ThemeOnboardingPageTab, so shipping
 * the definition here is the only way a host can use that page without knowing it exists. Hosts
 * still register their own OnboardingViewModel: that one takes host-supplied pages, so it cannot
 * be built from the library side.
 */
private fun onboardingModule(): Module = module {
    viewModel {
        OnboardingThemeViewModel(preferences = get())
    }
}

private fun supportModule(): Module = module {
    viewModel {
        SupportViewModel(billingRepository = get(), firebaseController = get())
    }
}

private fun helpModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<HelpLocalDataSource> { HelpLocalDataSource(context = get()) }
    single<HelpRemoteDataSource> { HelpRemoteDataSource(client = get()) }
    single<FaqRepository> {
        DefaultFaqRepository(
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

private fun changelogModule(): Module = module {
    single<ChangelogRepository> {
        DefaultChangelogRepository(
            client = get(),
            apiBaseUrl = get(
                qualifier = named(AppToolkitDiConstants.ANDROID_APPS_METADATA_API_BASE_URL),
            ),
            legacyChangelogUrl = get(
                qualifier = named(AppToolkitDiConstants.GITHUB_CHANGELOG),
            ),
            firebaseController = get(),
        )
    }
    single<GetChangelogUseCase> {
        GetChangelogUseCase(
            repository = get(),
            buildInfoProvider = get(),
        )
    }
    viewModel {
        ChangelogViewModel(
            getChangelogUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }
}

private fun issueReporterModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<IssueReporterRemoteDataSource> { IssueReporterRemoteDataSource(client = get()) }
    single<DeviceInfoProvider> { DeviceInfoLocalDataSource(get(), get()) }
    single<IssueReporterRepository> { DefaultIssueReporterRepository(get(), get(), get(), get()) }
    single<SendIssueReportUseCase> { SendIssueReportUseCase(get(), get(), get()) }
    single<GithubTarget> {
        GithubTarget(
            username = GithubConstants.GITHUB_USER,
            repository = get(qualifier = named(AppToolkitDiConstants.GITHUB_REPOSITORY)),
        )
    }
    single<String>(githubTokenQualifier) { hostBuildConfig.githubToken.toToken() }

    viewModel {
        IssueReporterViewModel(
            sendIssueReport = get(),
            githubTarget = get(),
            githubToken = get(githubTokenQualifier),
            repository = get(),
            firebaseController = get(),
            dispatchers = get(),
        )
    }
}

private fun reviewModule(): Module = module {
    single<ReviewRepository> { DefaultReviewRepository(dataStore = get()) }
    single<RequestInAppReviewUseCase> { RequestInAppReviewUseCase(reviewRepository = get()) }
    single<ForceInAppReviewUseCase> { ForceInAppReviewUseCase(reviewRepository = get()) }
}
