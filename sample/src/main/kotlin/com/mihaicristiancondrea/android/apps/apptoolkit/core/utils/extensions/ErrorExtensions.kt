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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.utils.extensions

import com.mihaicristiancondrea.android.apps.apptoolkit.R
import com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.model.network.AppErrors
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.remote.extensions.asUiText
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper

/**
 * App-specific overrides for mapping [AppErrors] to UI text.
 *
 * The app owns the messaging for its own data fetch flows, while delegating to the shared
 * library mapping for everything else.
 */
fun AppErrors.toErrorMessage(): UiTextHelper = when (this) {
    is AppErrors.Common -> value.asUiText()
    AppErrors.UseCase.FAILED_TO_LOAD_APPS -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
    AppErrors.UseCase.FAILED_TO_LOAD_APP_DETAILS -> UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
}
