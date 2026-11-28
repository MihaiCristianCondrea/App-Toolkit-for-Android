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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.mappers

import androidx.annotation.StringRes
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconLoopTrigger
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconReplayMode

/** Label of the replay mode as it is offered in the animation showcase menu. */
@StringRes
internal fun ToolkitIconReplayMode.labelResId(): Int = when (this) {
    ToolkitIconReplayMode.Restart -> R.string.components_animation_replay_restart
    ToolkitIconReplayMode.Reverse -> R.string.components_animation_replay_reverse
}

/** Label of the loop trigger as it is offered in the animation showcase menu. */
@StringRes
internal fun ToolkitIconLoopTrigger.labelResId(): Int = when (this) {
    ToolkitIconLoopTrigger.Immediately -> R.string.components_animation_loop_start_immediately
    ToolkitIconLoopTrigger.OnInteraction -> R.string.components_animation_loop_start_on_tap
}
