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
