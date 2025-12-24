package com.d4rk.android.apps.apptoolkit.core.di.modules

import com.d4rk.android.apps.apptoolkit.BuildConfig
import com.d4rk.android.apps.apptoolkit.app.startup.utils.interfaces.providers.AppStartupProvider
import com.d4rk.android.apps.apptoolkit.core.utils.constants.HelpConstants
import com.d4rk.android.libs.apptoolkit.app.help.data.repository.FaqRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.help.data.local.HelpLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.help.data.remote.HelpRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.help.domain.repository.FaqRepository
import com.d4rk.android.libs.apptoolkit.app.help.domain.usecases.GetFaqUseCase
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpViewModel
import com.d4rk.android.libs.apptoolkit.app.help.ui.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.repository.DefaultIssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.local.DeviceInfoLocalDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github.GithubTarget
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.providers.DeviceInfoProvider
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.repository.IssueReporterRepository
import com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.usecases.SendIssueReportUseCase
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterViewModel
import com.d4rk.android.libs.apptoolkit.app.startup.ui.StartupViewModel
import com.d4rk.android.libs.apptoolkit.app.startup.utils.interfaces.providers.StartupProvider
import com.d4rk.android.libs.apptoolkit.app.support.billing.BillingRepository
import com.d4rk.android.libs.apptoolkit.app.support.ui.SupportViewModel
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.di.GithubToken
import com.d4rk.android.libs.apptoolkit.core.utils.constants.github.GithubConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.decodeBase64OrEmpty
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.faqCatalogUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val appToolkitModule: Module = module {
    single<StartupProvider> { AppStartupProvider() }

    single(createdAtStart = true) {
        val dispatchers = get<DispatcherProvider>()
        BillingRepository.getInstance(
            context = get(),
            dispatchers = dispatchers,
            externalScope = CoroutineScope(SupervisorJob() + dispatchers.io)
        )
    }
    viewModel {
        SupportViewModel(billingRepository = get())
    }
    viewModel { StartupViewModel() }

    single { HelpLocalDataSource(context = get()) }
    single { HelpRemoteDataSource(client = get()) }
    single<FaqRepository> {
        FaqRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            catalogUrl = com.d4rk.android.libs.apptoolkit.core.utils.constants.help.HelpConstants.FAQ_BASE_URL.faqCatalogUrl(isDebugBuild = BuildConfig.DEBUG),
            productId = HelpConstants.FAQ_PRODUCT_ID,
        )
    }
    single { GetFaqUseCase(repository = get()) }
    viewModel { HelpViewModel(getFaqUseCase = get(), dispatchers = get()) }

    single { IssueReporterRemoteDataSource(client = get()) }
    single<DeviceInfoProvider> { DeviceInfoLocalDataSource(get(), get()) }
    single<IssueReporterRepository> { DefaultIssueReporterRepository(get(), get()) }
    single { SendIssueReportUseCase(get(), get()) }

    val githubTokenQualifier = qualifier<GithubToken>()
    viewModel {
        IssueReporterViewModel(
            sendIssueReport = get(),
            githubTarget = get(),
            githubToken = get(githubTokenQualifier),
            deviceInfoProvider = get()
        )
    }

    single(qualifier = named(name = "github_repository")) { "App-Toolkit-for-Android" }
    single<GithubTarget> {
        GithubTarget(
            username = GithubConstants.GITHUB_USER,
            repository = get(qualifier = named("github_repository")),
        )
    }

    single(qualifier = named("github_changelog")) {
        GithubConstants.githubChangelog(get<String>(named("github_repository")))
    }

    single(githubTokenQualifier) { BuildConfig.GITHUB_TOKEN.decodeBase64OrEmpty() }

    single<HelpScreenConfig> {
        HelpScreenConfig(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )
    }
}
