package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.GithubToken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.github.GithubConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.toToken
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.local.DeviceInfoLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.remote.IssueReporterRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.DefaultIssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.data.repositories.IssueReporterRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.github.GithubTarget
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.providers.DeviceInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.usecases.SendIssueReportUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.IssueReporterViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

private val githubTokenQualifier = qualifier<GithubToken>()

fun issueReporterModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
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
