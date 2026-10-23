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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

/**
 * A search field in a tab's app bar, where the title would be.
 *
 * The field is part of the shell's app bar, so moving to the tab only crossfades the title into
 * the field: the navigation button, the bar and its height stay exactly where they were. The
 * query lives in the shell; the tab reads it from
 * [com.mihaicristiancondrea.android.libs.apptoolkit.navigation.LocalShellSearch]. Back clears a
 * query before it does anything else.
 */
@Immutable
class TabSearch(@param:StringRes val hint: Int)
