package com.d4rk.android.libs.apptoolkit.app.consent.data.repository

import com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource.ConsentRemoteDataSource
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.consent.domain.repository.ConsentRepository
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [ConsentRepository] that delegates UMP work to a remote data source.
 */
class ConsentRepositoryImpl(
    private val remote: ConsentRemoteDataSource,
) : ConsentRepository {

    override fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>> {
        return remote.requestConsent(host = host, showIfRequired = showIfRequired)
    }
}
