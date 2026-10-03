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

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import java.util.concurrent.atomic.AtomicLong

/**
 * A message a screen shows in a snackbar, queued with [ScreenViewModel.showMessage].
 *
 * @property text What the message says.
 * @property isError Shows it in the error style, and for longer.
 * @property actionLabel The label of its action, or null for none; [MessageHost] reports a press.
 * @property id Tells two messages with the same text apart, so the same message queued twice is
 * shown twice. Unique within the process.
 */
@Immutable
data class UiMessage(
    val text: UiTextHelper,
    val isError: Boolean = false,
    val actionLabel: UiTextHelper? = null,
    val id: Long = nextId.incrementAndGet(),
) {
    private companion object {
        val nextId: AtomicLong = AtomicLong()
    }
}
