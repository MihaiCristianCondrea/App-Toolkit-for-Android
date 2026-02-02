package com.d4rk.android.apps.apptoolkit.core.di.modules.apptoolkit.modules

import com.d4rk.android.libs.apptoolkit.app.review.data.repository.ReviewRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.review.domain.repository.ReviewRepository
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.ForceInAppReviewUseCase
import com.d4rk.android.libs.apptoolkit.app.review.domain.usecases.RequestInAppReviewUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val reviewModule: Module =
    module {
        single<ReviewRepository> { ReviewRepositoryImpl(dataStore = get()) }
        single { RequestInAppReviewUseCase(reviewRepository = get()) }
        single { ForceInAppReviewUseCase(reviewRepository = get()) }
    }
