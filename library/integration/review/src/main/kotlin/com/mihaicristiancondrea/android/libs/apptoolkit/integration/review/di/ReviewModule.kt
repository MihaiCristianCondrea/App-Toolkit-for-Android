package com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.di

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.repositories.DefaultReviewRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.data.repositories.ReviewRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.RequestInAppReviewUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val reviewModule: Module = module {
    single<ReviewRepository> { DefaultReviewRepository(dataStore = get()) }
    single<RequestInAppReviewUseCase> { RequestInAppReviewUseCase(reviewRepository = get()) }
    single<ForceInAppReviewUseCase> { ForceInAppReviewUseCase(reviewRepository = get()) }
}
