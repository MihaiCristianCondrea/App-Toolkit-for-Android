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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.widgets.apps

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
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
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.app.apps.common.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext
import java.net.URL
import androidx.core.graphics.scale
import androidx.core.net.toUri

/**
 * A highly expressive, resizable 3x3 grid widget that focuses on fast app launching.
 *
 * Change rationale:
 * - Previously, the widget resolved package icons for the full app list even though only the first
 *   3x3 slots were rendered.
 * - Now, app loading is capped to the visible 9 entries before icon decoding, reducing background
 *   work and memory pressure for each update.
 */
class AppIconsWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_app_icons_error) {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SIZE, MEDIUM_SIZE, LARGE_SIZE),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = loadApps(context = context)
        provideContent {
            AppIconsWidgetContent(state = state)
        }
    }

    private suspend fun loadApps(context: Context): AppIconsWidgetState =
        withContext(Dispatchers.IO) {
            runCatching {
                val developerAppsRepository = GlobalContext.get().get<DeveloperAppsRepository>()
                val state = developerAppsRepository.fetchDeveloperApps().first { it !is DataState.Loading }
                val apps = when (state) {
                    is DataState.Success -> state.data
                    is DataState.Error -> return@withContext state.data
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { AppIconsWidgetState.Content(createEntries(context, it)) }
                        ?: AppIconsWidgetState.Error
                    is DataState.Loading -> return@withContext AppIconsWidgetState.Loading
                }

                if (apps.isEmpty()) AppIconsWidgetState.Empty
                else AppIconsWidgetState.Content(createEntries(context, apps))
            }.getOrElse { throwable ->
                if (throwable is CancellationException) throw throwable
                AppIconsWidgetState.Error
            }
        }

    private fun createEntries(context: Context, apps: List<AppInfo>): ImmutableList<WidgetAppEntry> =
        apps.take(MAX_GRID_ITEMS).map { app ->
            val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
            WidgetAppEntry(
                app = app,
                icon = resolveAppIcon(context, app),
                destination = launchIntent ?: Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=${Uri.encode(app.packageName)}".toUri(),
                ),
            )
        }.toImmutableList()

    private fun resolveAppIcon(context: Context, app: AppInfo): Bitmap {
        val installedIcon = runCatching {
            context.packageManager.getApplicationIcon(app.packageName)
        }.getOrNull()
        if (installedIcon != null) return installedIcon.toBitmap(DEFAULT_ICON_BITMAP_SIZE_PX)

        val remoteIcon = runCatching {
            URL(app.iconUrl).openConnection().run {
                connectTimeout = ICON_REQUEST_TIMEOUT_MILLIS
                readTimeout = ICON_REQUEST_TIMEOUT_MILLIS
                getInputStream().use(BitmapFactory::decodeStream)
            }
        }.getOrNull()
        if (remoteIcon != null) {
            return remoteIcon.scale(DEFAULT_ICON_BITMAP_SIZE_PX, DEFAULT_ICON_BITMAP_SIZE_PX).also { scaled -> if (scaled !== remoteIcon) remoteIcon.recycle() }
        }
        return context.packageManager.getApplicationIcon(context.packageName)
            .toBitmap(DEFAULT_ICON_BITMAP_SIZE_PX)
    }

    companion object {
        const val GRID_COLUMNS: Int = 3
        const val GRID_ROWS: Int = 3
        private const val MAX_GRID_ITEMS: Int = GRID_COLUMNS * GRID_ROWS
        private const val DEFAULT_ICON_BITMAP_SIZE_PX: Int = 72
        private const val ICON_REQUEST_TIMEOUT_MILLIS: Int = 5_000

        val SMALL_SIZE: DpSize = DpSize(width = 120.dp, height = 120.dp)
        val MEDIUM_SIZE: DpSize = DpSize(width = 180.dp, height = 180.dp)
        val LARGE_SIZE: DpSize = DpSize(width = 250.dp, height = 250.dp)
    }
}

@Composable
internal fun AppIconsWidgetContent(state: AppIconsWidgetState) { // FIXME: Parameter 'state' has runtime-determined stability
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

@Immutable
internal data class WidgetAppEntry(
    val app: AppInfo,
    val icon: Bitmap,
    val destination: Intent,
)

internal sealed interface AppIconsWidgetState {
    data object Loading : AppIconsWidgetState
    data object Empty : AppIconsWidgetState
    data object Error : AppIconsWidgetState
    data class Content(val apps: ImmutableList<WidgetAppEntry>) : AppIconsWidgetState
}

@Immutable
internal data class WidgetStrings(
    val title: String,
    val errorMessage: String,
    val retryLabel: String,
) {
    companion object {
        fun from(context: Context): WidgetStrings = WidgetStrings(
            title = context.getString(R.string.widget_apps_title),
            errorMessage = context.getString(R.string.widget_apps_error_message),
            retryLabel = context.getString(R.string.widget_apps_error_retry),
        )
    }
}

private fun GlanceModifier.appWidgetClickAction(destination: Intent): GlanceModifier =
    clickable(onClick = actionStartActivity(destination))

private fun Drawable.toBitmap(sizePx: Int): Bitmap {
    return createBitmap(sizePx, sizePx).also { bitmap ->
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}
