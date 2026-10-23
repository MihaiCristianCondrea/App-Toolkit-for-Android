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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.services

import android.content.ComponentName
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.CallSuper
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.data.repositories.ToolkitTilesRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Base for the feature's Quick Settings tiles.
 *
 * Records system-confirmed membership when secure Quick Settings configuration is unreadable, and
 * lets a tile follow shared state only while System UI is listening, so changes made elsewhere
 * (the in-app tool, another tile, the system flashlight) show up while the panel is open.
 */
abstract class TrackedTileService : TileService(), KoinComponent {
    private val tilesRepository: ToolkitTilesRepository by inject()
    private val diagnostics: DiagnosticsReporter by inject()
    private val serviceScope = MainScope()
    private var listeningJob: Job? = null

    override fun onTileAdded() {
        super.onTileAdded()
        recordMembership(true)
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        recordMembership(false)
    }

    override fun onStartListening() {
        super.onStartListening()
        recordMembership(true)
    }

    @CallSuper
    override fun onStopListening() {
        listeningJob?.cancel()
        listeningJob = null
        super.onStopListening()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    /**
     * Renders every value of [flow] until System UI stops listening. Call from [onStartListening];
     * a second call replaces the first.
     */
    protected fun <T> renderWhileListening(flow: Flow<T>, render: (T) -> Unit) {
        listeningJob?.cancel()
        listeningJob = flow.onEach(render).launchIn(serviceScope)
    }

    /** Publishes [text] and [state] with the text placed where this Android version can show it. */
    protected fun publishTile(state: Int, text: TileText) {
        val tile = qsTile ?: return
        val visibleText = text.forSubtitleSupport(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        tile.state = state
        tile.label = visibleText.label
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = visibleText.subtitle
        }
        tile.updateTile()
    }

    /** Reports an action failure without letting it escape the service's coroutine scope. */
    protected fun launchTileAction(action: String, block: suspend () -> Unit) {
        serviceScope.launch {
            try {
                block()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                diagnostics.recordNonFatal(error, attributes = mapOf("action" to action))
            }
        }
    }

    private fun recordMembership(added: Boolean) {
        tilesRepository.recordTileMembership(ComponentName(this, javaClass).flattenToString(), added)
    }
}

/**
 * What a tile shows: its [title], secondary [subtitle] text, and the [result] of the last tap.
 *
 * Tile subtitles exist only from Android 10. Before that the label is the only visible text, so a
 * [result] replaces [title] there; otherwise a tap would change nothing the user can read.
 */
data class TileText(
    val title: String,
    val subtitle: String,
    val result: String? = null,
) {
    internal fun forSubtitleSupport(supportsSubtitle: Boolean): VisibleTileText =
        if (supportsSubtitle) {
            VisibleTileText(label = title, subtitle = result ?: subtitle)
        } else {
            VisibleTileText(label = result ?: title, subtitle = null)
        }
}

internal data class VisibleTileText(val label: String, val subtitle: String?)
