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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.help.HelpConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.faqCatalogUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.local.FaqLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.remote.FaqRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.DefaultFaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.domain.usecases.GetFaqUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds the FAQ catalog: its local resource fallback, the remote catalog, and the use case that
 * cleans the result for display.
 */
fun faqModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
    single<FaqLocalDataSource> { FaqLocalDataSource(context = get()) }
    single<FaqRemoteDataSource> { FaqRemoteDataSource(client = get()) }
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
}
