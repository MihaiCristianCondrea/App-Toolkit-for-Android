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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.ui.models

import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.display.R

/** A heading of the display settings page, in the order the page shows them. */
internal enum class DisplayCategory(@param:StringRes val title: Int) {
    Appearance(R.string.appearance),
    AppBehavior(R.string.app_behavior),
    Navigation(R.string.navigation),
    Language(R.string.language),
}
