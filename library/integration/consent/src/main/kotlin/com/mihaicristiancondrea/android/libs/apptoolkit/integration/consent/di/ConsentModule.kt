package com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.di

import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource.ConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.remote.datasource.UmpConsentRemoteDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.ConsentRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.consent.data.repositories.DefaultConsentRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/** Binds consent persistence coordination and the UMP remote source. Requires foundation providers. */
fun consentModule(): Module = module {
    single<ConsentRemoteDataSource> { UmpConsentRemoteDataSource(adMobAppIdProvider = get()) }
    single<ConsentRepository> {
        DefaultConsentRepository(
            remote = get(),
            local = get(),
            configProvider = get(),
            firebaseController = get(),
        )
    }
}
