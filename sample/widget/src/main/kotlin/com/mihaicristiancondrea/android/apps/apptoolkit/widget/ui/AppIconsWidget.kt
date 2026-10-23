/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.R
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.data.repositories.WidgetAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.mappers.toWidgetAppEntry
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.models.WidgetAppEntry
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.models.WidgetStrings
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.states.AppIconsWidgetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flow
import org.koin.core.context.GlobalContext

/**
 * Resizable app-launch grid. Icon decoding is bounded to the nine visible entries, avoiding
 * work for catalog apps the widget cannot show.
 */
class AppIconsWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_app_icons_error) {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SIZE, MEDIUM_SIZE, LARGE_SIZE),
    )

    /**
     * Displays the saved catalog while fetching its replacement. Loading content is used only
     * when no saved catalog is available, and a failed fetch keeps the saved catalog on screen.
     */
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val savedState: AppIconsWidgetState? = loadSavedApps()
        provideContent {
            val freshState = remember { flow { emit(loadApps(savedState = savedState)) } }
            val state: AppIconsWidgetState by freshState.collectAsState(
                initial = savedState ?: AppIconsWidgetState.Loading,
            )
            AppIconsWidgetContent(state = state)
        }
    }

    /**
     * The catalogue the apps screen saved, read without the network, or null when there is none or
     * it cannot be read. The widget has no failure of its own to show here: the fetch that follows
     * decides between the grid and the error content.
     */
    private suspend fun loadSavedApps(): AppIconsWidgetState? =
        try {
            widgetAppsRepository().savedApps(limit = GRID_COLUMNS * GRID_ROWS)
                ?.takeIf { it.isNotEmpty() }
                ?.let { apps -> AppIconsWidgetState.Content(apps.map { it.toWidgetAppEntry() }.toImmutableList()) }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            null
        }

    /**
     * Downloads the catalogue. A failed download falls back to [savedState], and shows the error
     * content with its retry action only when nothing was saved.
     */
    private suspend fun loadApps(
        savedState: AppIconsWidgetState?,
    ): AppIconsWidgetState =
        try {
            val apps = widgetAppsRepository().fetchApps(limit = GRID_COLUMNS * GRID_ROWS)
            if (apps.isEmpty()) {
                AppIconsWidgetState.Empty
            } else {
                AppIconsWidgetState.Content(apps.map { it.toWidgetAppEntry() }.toImmutableList())
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            savedState ?: AppIconsWidgetState.Error
        }

    private fun widgetAppsRepository(): WidgetAppsRepository =
        GlobalContext.get().get<WidgetAppsRepository>()

    companion object {
        const val GRID_COLUMNS: Int = 3
        const val GRID_ROWS: Int = 3

        val SMALL_SIZE: DpSize = DpSize(width = 120.dp, height = 120.dp)
        val MEDIUM_SIZE: DpSize = DpSize(width = 180.dp, height = 180.dp)
        val LARGE_SIZE: DpSize = DpSize(width = 250.dp, height = 250.dp)
    }
}

@Composable
internal fun AppIconsWidgetContent(state: AppIconsWidgetState) {
    GlanceTheme {
        val strings = WidgetStrings.from(LocalContext.current)
        when (state) {
            AppIconsWidgetState.Loading -> WidgetMessage(strings.title)
            AppIconsWidgetState.Empty -> WidgetMessage(strings.errorMessage)
            AppIconsWidgetState.Error -> WidgetError(strings)
            is AppIconsWidgetState.Content -> WidgetGrid(state.apps, strings.title)
        }
    }
}

@Composable
internal fun WidgetError(strings: WidgetStrings) {
    Box(
        modifier = GlanceModifier.fillMaxSize().appWidgetBackground(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(strings.errorMessage)
            Text(
                text = strings.retryLabel,
                modifier = GlanceModifier.clickable(actionRunCallback<RefreshWidgetAction>()),
            )
        }
    }
}

@Composable
internal fun WidgetMessage(message: String) {
    Box(
        modifier = GlanceModifier.fillMaxSize().appWidgetBackground(),
        contentAlignment = Alignment.Center,
    ) {
        Text(message)
    }
}

@Composable
private fun WidgetGrid(apps: ImmutableList<WidgetAppEntry>, title: String) {
    val widgetSize = LocalSize.current
    val showTitle = widgetSize.width >= AppIconsWidget.MEDIUM_SIZE.width
    val iconSize = when {
        widgetSize.width < 150.dp -> 28.dp
        widgetSize.width < 200.dp -> 36.dp
        else -> 48.dp
    }

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(GlanceTheme.colors.surface)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.secondaryContainer)
                .cornerRadius(16.dp)
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            if (showTitle) {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Medium),
                    modifier = GlanceModifier.padding(start = 4.dp, top = 2.dp, bottom = 2.dp)
                )
            }

            val rows = apps.chunked(AppIconsWidget.GRID_COLUMNS)

            for (rowIndex in 0 until AppIconsWidget.GRID_ROWS) {
                val rowApps = rows.getOrNull(rowIndex) ?: emptyList()

                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight()
                ) {
                    for (colIndex in 0 until AppIconsWidget.GRID_COLUMNS) {
                        val item = rowApps.getOrNull(colIndex)

                        if (item != null) {
                            Box(
                                modifier = GlanceModifier
                                    .defaultWeight()
                                    .fillMaxHeight()
                                    .padding(4.dp)
                                    .background(GlanceTheme.colors.primaryContainer)
                                    .cornerRadius(8.dp)
                                    .appWidgetClickAction(item.destination),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    provider = ImageProvider(item.icon),
                                    contentDescription = item.app.name,
                                    modifier = GlanceModifier.size(iconSize)
                                )
                            }
                        } else {
                            Box(
                                modifier = GlanceModifier
                                    .defaultWeight()
                                    .fillMaxHeight()
                                    .padding(4.dp)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

private fun GlanceModifier.appWidgetClickAction(destination: Intent): GlanceModifier =
    clickable(onClick = actionStartActivity(destination))
