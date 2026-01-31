package com.d4rk.android.apps.apptoolkit.core.di.modules.app.modules

import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.UmpConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.data.repository.ConsentRepositoryImpl
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.app.consent.domain.usecases.RequestConsentUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val consentModule: Module = module {
    single<ConsentRemoteDataSource> { UmpConsentRemoteDataSource() }
    single<ConsentRepository> { ConsentRepositoryImpl(remote = get()) }
    single { RequestConsentUseCase(repository = get(), firebaseController = get()) }
}
