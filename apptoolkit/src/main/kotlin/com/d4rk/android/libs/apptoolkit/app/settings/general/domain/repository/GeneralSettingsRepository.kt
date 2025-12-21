package com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for providing the content key for the General Settings screen.
 *
 * Implementations should validate the provided key and emit it through a
 * [Flow]. This keeps the data operations asynchronous and testable.
 */
interface GeneralSettingsRepository {
    /**
     * Returns a [Flow] that emits a valid content key. If the provided key is
     * null or blank, the flow throws an [IllegalArgumentException] when
     * collected. Using a [Flow] allows the repository to expose asynchronous
     * data streams in line with recommended architecture guidelines.
     */
    fun getContentKey(contentKey: String?): Flow<String>
}


