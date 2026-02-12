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

/**
 * Repository implementations for permissions-related settings.
 *
 * Repositories act as data boundaries that can persist or observe state via suspend functions
 * or [kotlinx.coroutines.flow.Flow]. Prefer providers for static, read-only configuration, and
 * use repository implementations in this package when the consumer needs observable or async data.
 */
package com.d4rk.android.libs.apptoolkit.app.permissions.data.repository
