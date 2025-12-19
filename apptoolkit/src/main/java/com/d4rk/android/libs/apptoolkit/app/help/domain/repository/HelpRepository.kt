package com.d4rk.android.libs.apptoolkit.app.help.domain.repository

import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository for retrieving help and FAQ information.
 */
interface HelpRepository {
    /**
     * Fetches the frequently asked questions as a cold [Flow].
     */
    fun fetchFaq(): Flow<List<FaqItem>>
}
