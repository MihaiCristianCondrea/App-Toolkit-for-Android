package com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeneralSettingsRepositoryImpl : GeneralSettingsRepository {

    override fun getContentKey(contentKey: String?): Flow<String> = flow {
        val key = contentKey?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Invalid content key")
        emit(key)
    }
}
