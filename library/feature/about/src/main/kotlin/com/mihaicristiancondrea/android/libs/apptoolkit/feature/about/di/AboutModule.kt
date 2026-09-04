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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.AboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.ChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.DefaultAboutRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.DefaultChangelogRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.data.repositories.DefaultNavigationRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.usecases.CopyDeviceInfoUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.domain.usecases.GetChangelogUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.ChangelogViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.factory.GmsHostFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aboutModule: Module = module {
    single<DefaultAboutRepository> {
        DefaultAboutRepository(
            deviceProvider = get(),
            buildInfoProvider = get(),
            context = get(),
            firebaseController = get(),
        )
    }
    single<AboutRepository> { get<DefaultAboutRepository>() }

    single<DefaultChangelogRepository> {
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
    single<ChangelogRepository> { get<DefaultChangelogRepository>() }

    single<DefaultNavigationRepository> {
        DefaultNavigationRepository(
            dispatchers = get(),
        )
    }

    single<GetChangelogUseCase> {
        GetChangelogUseCase(
            repository = get(),
            buildInfoProvider = get(),
        )
    }

    single<CopyDeviceInfoUseCase> {
        CopyDeviceInfoUseCase(
            repository = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        AboutViewModel(
            aboutRepository = get(),
            copyDeviceInfo = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    viewModel {
        ChangelogViewModel(
            getChangelogUseCase = get(),
            dispatchers = get(),
            firebaseController = get(),
        )
    }

    single { GmsHostFactory() }
}
