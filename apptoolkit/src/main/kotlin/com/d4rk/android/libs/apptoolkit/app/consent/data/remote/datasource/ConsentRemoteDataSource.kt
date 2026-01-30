package com.d4rk.android.libs.apptoolkit.app.consent.data.remote.datasource

import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Remote data source responsible for coordinating consent requests with UMP.
 */
interface ConsentRemoteDataSource {

    /**
     * Requests consent information and optionally shows the consent form.
     *
     * @param host The UI host needed by the UMP SDK.
     * @param showIfRequired When true, the form is shown only when required by UMP.
     */
    fun requestConsent(
        host: ConsentHost,
        showIfRequired: Boolean,
    ): Flow<DataState<Unit, Errors.UseCase>>
}
