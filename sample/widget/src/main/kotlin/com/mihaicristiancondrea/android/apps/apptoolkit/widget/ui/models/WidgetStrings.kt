/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.models

import android.content.Context
import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.R

@Immutable
internal data class WidgetStrings(
    val title: String,
    val errorMessage: String,
    val retryLabel: String,
) {
    companion object {
        fun from(context: Context): WidgetStrings = WidgetStrings(
            title = context.getString(R.string.widget_apps_title),
            errorMessage = context.getString(R.string.widget_apps_error_message),
            retryLabel = context.getString(R.string.widget_apps_error_retry),
        )
    }
}
