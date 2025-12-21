package com.d4rk.android.libs.apptoolkit.app.permissions.domain.repository

import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import kotlinx.coroutines.flow.Flow

/**
 * Repository that exposes the permissions configuration.
 *
 * Implementations should be free of Android framework dependencies so that
 * the UI layer can obtain the configuration without requiring a [Context].
 */
interface PermissionsRepository {
    /**
     * Returns a stream of the permissions configuration to be displayed by the UI.
     */
    fun getPermissionsConfig(): Flow<SettingsConfig>
}
