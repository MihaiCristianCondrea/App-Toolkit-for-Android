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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab

/**
 * Where a screen's floating action buttons go: the Toolkit scaffold around the screen holds one
 * and draws what the screen puts in it, with [ToolkitFabColumn].
 */
@Stable
class FabHost {
    private var owner: Any? by mutableStateOf(null)

    /** The buttons the screen inside the scaffold declares now. */
    var fabs: List<ToolkitFab> by mutableStateOf(emptyList())
        private set

    /** The list the screen last declared, whose actions the drawn buttons run. */
    private var latest: List<ToolkitFab> = emptyList()

    /**
     * Takes the screen's buttons. A screen declares them again on every recomposition, each time
     * with new click lambdas, so the drawn list only changes when a button looks different; its
     * clicks always run the latest actions.
     */
    internal fun set(owner: Any, fabs: List<ToolkitFab>) {
        this.owner = owner
        latest = fabs
        if (!looksTheSame(this.fabs, fabs)) {
            this.fabs = fabs.mapIndexed { index, fab -> fab.runningLatestClick(index) }
        }
    }

    internal fun clear(owner: Any) {
        if (this.owner === owner) {
            this.owner = null
            latest = emptyList()
            fabs = emptyList()
        }
    }

    /** This button, running the action of the button declared last at [index] (by [ToolkitFab.id] if the list moved). */
    private fun ToolkitFab.runningLatestClick(index: Int): ToolkitFab = ToolkitFab(
        icon = icon,
        onClick = {
            val current = latest.getOrNull(index)?.takeIf { it.id == id } ?: latest.firstOrNull { it.id == id } ?: this
            current.onClick()
        },
        label = label,
        contentDescription = contentDescription,
        size = size,
        color = color,
        expanded = expanded,
        visible = visible,
        id = id,
    )

    private fun looksTheSame(drawn: List<ToolkitFab>, declared: List<ToolkitFab>): Boolean =
        drawn.size == declared.size && drawn.indices.all { index ->
            val a = drawn[index]
            val b = declared[index]
            a.id == b.id && a.icon == b.icon && a.label == b.label &&
                a.contentDescription == b.contentDescription && a.size == b.size &&
                a.color == b.color && a.expanded == b.expanded && a.visible == b.visible
        }
}

/** The [FabHost] of the Toolkit scaffold around this point of the composition, if any. */
val LocalFabHost = staticCompositionLocalOf<FabHost?> { null }

/**
 * Puts [fabs] in the floating action button column of the Toolkit scaffold around this screen: a
 * page's frame or the shell's tabs. They follow the screen's state, since this is called from the
 * screen, and leave with it.
 *
 * ```
 * ScaffoldFabs(listOf(ToolkitFab(icon, onClick = { showSheet = true }, label = stringResource(R.string.feedback))))
 * ```
 *
 * Outside a Toolkit scaffold it draws nothing.
 */
@Composable
fun ScaffoldFabs(fabs: List<ToolkitFab>) {
    val host = LocalFabHost.current ?: return
    val owner = remember { Any() }
    SideEffect { host.set(owner, fabs) }
    DisposableEffect(host) { onDispose { host.clear(owner) } }
}
