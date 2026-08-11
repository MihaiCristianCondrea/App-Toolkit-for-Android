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

package com.d4rk.android.libs.apptoolkit.core.utils.extensions.activity

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseViewModel
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import kotlinx.coroutines.launch

/**
 * Observes actions emitted by the [viewModel] and executes the [onAction] block.
 *
 * This extension abstracts the boilerplate of launching a coroutine and repeating on lifecycle.
 */
fun <A : ActionEvent> ComponentActivity.observeActions(
    viewModel: BaseViewModel<*, *, A>,
    onAction: suspend (A) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.actionEvent.collect { action ->
                onAction(action)
            }
        }
    }
}
