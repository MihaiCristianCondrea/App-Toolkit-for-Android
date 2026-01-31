package com.d4rk.android.libs.apptoolkit.app.settings.general.data.repository

import com.d4rk.android.libs.apptoolkit.app.settings.general.domain.repository.GeneralSettingsRepository
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeneralSettingsRepositoryImpl(
    private val firebaseController: FirebaseController,
) : GeneralSettingsRepository {

    override fun getContentKey(contentKey: String?): Flow<String> = flow {
        firebaseController.logBreadcrumb(
            message = "General settings content requested",
            attributes = mapOf("hasContentKey" to (!contentKey.isNullOrBlank()).toString()),
        )
        val key = contentKey?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Invalid content key")
        emit(key)
    }
}
