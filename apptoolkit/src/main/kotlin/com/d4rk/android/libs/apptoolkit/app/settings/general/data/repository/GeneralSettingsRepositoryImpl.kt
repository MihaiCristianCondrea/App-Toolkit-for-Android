package com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GeneralSettingsRepositoryImpl(
    private val dispatchers: DispatcherProvider,
) : GeneralSettingsRepository {

    override fun getContentKey(contentKey: String?): Flow<String> = flow {
        if (contentKey.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid content key")
        }
        emit(contentKey)
    }.flowOn(dispatchers.default)
}
