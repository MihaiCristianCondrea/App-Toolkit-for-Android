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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repository.BillingCore
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repository.BillingRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.billing.data.repository.DefaultBillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for the billing integration.
 */
val billingModule: Module = module {
    single<BillingRepository>(createdAtStart = true) {
        val dispatchers = get<DispatcherProvider>()
        DefaultBillingRepository.getInstance(
            context = get(),
            dispatchers = dispatchers,
            firebaseController = get(),
            externalScope = CoroutineScope(SupervisorJob() + dispatchers.io),
        )
    }

    single<BillingCore> { get<BillingRepository>() }
}
