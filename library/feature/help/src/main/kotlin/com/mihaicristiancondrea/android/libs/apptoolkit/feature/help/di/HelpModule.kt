package com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.models.AppToolkitHostBuildConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.help.HelpConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.string.faqCatalogUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.data.local.HelpLocalDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.data.remote.HelpRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.data.repositories.DefaultFaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.data.repositories.FaqRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.domain.usecases.GetFaqUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.HelpViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun helpModule(hostBuildConfig: AppToolkitHostBuildConfig): Module = module {
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
