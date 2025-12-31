package com.d4rk.android.libs.apptoolkit.app.help.domain.repository

import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import kotlinx.coroutines.flow.Flow

/**
 * Repository for retrieving FAQ information.
 */
interface FaqRepository {
    fun fetchFaq(): Flow<DataState<List<FaqItem>, Errors>>
}
