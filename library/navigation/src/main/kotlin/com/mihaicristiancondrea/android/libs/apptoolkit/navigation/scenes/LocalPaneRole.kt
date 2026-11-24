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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.runtime.compositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.PaneRole

/**
 * The pane the current page is drawn in. A page inside a pane leaves its app bar to the scene,
 * and a detail beside its list has no back button, since the list is already on screen.
 */
val LocalPaneRole = compositionLocalOf { PaneRole.None }
