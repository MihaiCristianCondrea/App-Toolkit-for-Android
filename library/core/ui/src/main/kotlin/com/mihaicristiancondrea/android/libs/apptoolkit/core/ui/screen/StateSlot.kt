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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * What a [ScreenStateHandler] knows when it draws a case other than [Loadable.Ready].
 *
 * @property contentPadding The padding the content keeps, usually the screen's content padding.
 * @property icon The screen's icon for its empty and failure content.
 * @property onRetry Tries again, or null when trying again does not apply to this case.
 * @property showAd Whether the content may show its native ad.
 */
@Immutable
data class StateSlot(
    val contentPadding: PaddingValues,
    val icon: ToolkitIcon,
    val onRetry: (() -> Unit)?,
    val showAd: Boolean,
)
