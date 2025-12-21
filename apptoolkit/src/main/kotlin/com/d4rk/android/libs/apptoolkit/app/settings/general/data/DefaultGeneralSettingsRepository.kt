package com.d4rk.android.libs.apptoolkit.app.settings.general.data

import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Default implementation of [GeneralSettingsRepository].
 *
 * This implementation performs basic validation on the provided key and runs
 * the work on the supplied [CoroutineDispatcher] to keep it off the main
 * thread.
 */
class DefaultGeneralSettingsRepository(
    private val dispatchers: DispatcherProvider,
) : GeneralSettingsRepository {

    override fun getContentKey(contentKey: String?): Flow<String> = flow {
        if (contentKey.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid content key")
        }
        emit(contentKey)
    }.flowOn(dispatchers.default)
}

