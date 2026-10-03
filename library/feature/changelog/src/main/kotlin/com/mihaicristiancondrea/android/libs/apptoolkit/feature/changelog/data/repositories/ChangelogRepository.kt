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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.changelog.data.repositories

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.NetworkException

/** Provides Markdown changelog content for a host application package. */
interface ChangelogRepository {

    /**
     * Returns the changelog Markdown for [packageName], applying the data-layer fallback. Safe to
     * call from the main thread.
     *
     * @throws NetworkException when the changelog could not be fetched.
     */
    suspend fun getChangelog(packageName: String): String
}
