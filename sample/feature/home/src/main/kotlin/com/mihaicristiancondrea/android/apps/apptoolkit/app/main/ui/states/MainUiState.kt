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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.main.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationDrawerItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MainUiState(
    val showSnackbar: Boolean = false,
    val snackbarMessage: UiTextHelper = UiTextHelper.DynamicString(""),
    val showDialog: Boolean = false,
    val navigationDrawerItems: ImmutableList<NavigationDrawerItem> = persistentListOf()
)
