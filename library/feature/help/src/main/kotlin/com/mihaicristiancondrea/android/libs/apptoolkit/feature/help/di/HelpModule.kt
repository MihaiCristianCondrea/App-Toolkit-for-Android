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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.help.ui.HelpViewModel
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.domain.usecases.ForceInAppReviewUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Binds the Help screen. The FAQ catalog it renders is bound by
 * [com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.di.faqModule].
 */
val helpModule: Module = module {
    viewModel {
        HelpViewModel(
            getFaqUseCase = get(),
            forceInAppReviewUseCase = get<ForceInAppReviewUseCase>(),
            dispatchers = get<DispatcherProvider>(),
            firebaseController = get(),
        )
    }
}
