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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.issuereporter.ui.models

import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.RootError
import io.ktor.http.HttpStatusCode

/**
 * Why sending a report failed, in the terms the report screen renders.
 *
 * Was a private sealed interface nested inside the ViewModel, which is what kept `asDataState()`
 * there too: a mapper in `ui/mappers` could not name the type it produced.
 */
internal sealed interface IssueReporterError : RootError {
    val message: String?

    data class Http(val status: HttpStatusCode, override val message: String?) : IssueReporterError

    data class Generic(override val message: String?) : IssueReporterError
}
