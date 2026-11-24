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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import org.koin.core.context.GlobalContext

/**
 * Suppresses [IssueReporterShakeManager] while this composable stays in composition and [enabled]
 * is true.
 *
 * Use this inside screens or sheets that consume shaking themselves, so vigorous motion does not
 * open the issue reporter over them. Resolves [IssueReporterShakeManager] optionally from Koin and
 * does nothing when Koin is not started or the manager is not bound, keeping previews and isolated
 * tests self-contained.
 *
 * @param enabled Whether shake-to-report should be suppressed.
 */
@Composable
fun SuppressShakeToReport(
    enabled: Boolean = true,
) {
    val shakeManager: IssueReporterShakeManager? = remember {
        GlobalContext.getOrNull()?.getOrNull<IssueReporterShakeManager>()
    }
    DisposableEffect(shakeManager, enabled) {
        val handle = if (enabled) shakeManager?.suppress() else null
        onDispose { handle?.close() }
    }
}
