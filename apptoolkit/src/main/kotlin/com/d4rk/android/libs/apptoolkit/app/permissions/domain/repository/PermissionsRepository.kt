/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
