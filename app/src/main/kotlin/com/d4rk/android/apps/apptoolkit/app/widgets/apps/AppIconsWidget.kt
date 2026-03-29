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

package com.d4rk.android.apps.apptoolkit.app.widgets.apps

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
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
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
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
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.widgets.apps.domain.actions.OpenAppOrStoreAction
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext

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
        val apps = loadApps(context = context)
        provideContent {
            AppIconsWidgetContent(apps = apps)
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable,
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_app_icons_error)
        remoteViews.setOnClickPendingIntent(
            R.id.widget_error_action_refresh,
            createRefreshIntent(context),
        )
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews)
    }

    private suspend fun loadApps(context: Context): ImmutableList<WidgetAppEntry> = withContext(Dispatchers.IO) {
        val fetchAppsUseCase = GlobalContext.get().get<FetchDeveloperAppsUseCase>()
        val apps = when (val state = fetchAppsUseCase().first { it !is DataState.Loading }) {
            is DataState.Success -> state.data
            is DataState.Error -> state.data ?: emptyList()
            is DataState.Loading -> emptyList()
        }

        apps
            .ifEmpty { listOf(createFallbackEntry(context)) }
            .take(MAX_GRID_ITEMS)
            .map { app ->
                WidgetAppEntry(
                    app = app,
                    icon = resolveAppIcon(context = context, packageName = app.packageName),
                )
            }
            .toImmutableList()
    }

    private fun createFallbackEntry(context: Context): AppInfo {
        val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
        return AppInfo(
            name = appName,
            packageName = context.packageName,
            iconUrl = "",
            description = "",
            screenshots = emptyList(),
            category = null,
        )
    }

    private fun resolveAppIcon(context: Context, packageName: String): Bitmap {
        val drawable = runCatching {
            context.packageManager.getApplicationIcon(packageName)
        }.getOrElse {
            context.packageManager.getApplicationIcon(context.packageName)
        }

        return drawable.toBitmap(sizePx = DEFAULT_ICON_BITMAP_SIZE_PX)
    }

    private fun createRefreshIntent(context: Context): PendingIntent {
        val intent = Intent(context, AppIconsWidgetReceiver::class.java).setAction(
            AppIconsWidgetReceiver.ACTION_REFRESH_WIDGET,
        )
        return PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    companion object {
        const val GRID_COLUMNS: Int = 3
        const val GRID_ROWS: Int = 3
        private const val MAX_GRID_ITEMS: Int = GRID_COLUMNS * GRID_ROWS
        private const val DEFAULT_ICON_BITMAP_SIZE_PX: Int = 72

        val SMALL_SIZE: DpSize = DpSize(width = 120.dp, height = 120.dp)
        val MEDIUM_SIZE: DpSize = DpSize(width = 180.dp, height = 180.dp)
        val LARGE_SIZE: DpSize = DpSize(width = 250.dp, height = 250.dp)
    }
}

@Composable
private fun AppIconsWidgetContent(apps: ImmutableList<WidgetAppEntry>) {
    GlanceTheme {
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
                        text = LocalContext.current.getString(R.string.widget_apps_title),
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
                                        .appWidgetClickAction(item.app.packageName),
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
}

@Immutable
private data class WidgetAppEntry(
    val app: AppInfo,
    val icon: Bitmap,
)

private fun GlanceModifier.appWidgetClickAction(packageName: String): GlanceModifier =
    clickable(
        onClick = actionRunCallback<OpenAppOrStoreAction>(
            parameters = actionParametersOf(OpenAppOrStoreAction.PACKAGE_NAME_KEY to packageName),
        ),
    )

private fun Drawable.toBitmap(sizePx: Int): Bitmap {
    val width = intrinsicWidth.takeIf { it > 0 } ?: sizePx
    val height = intrinsicHeight.takeIf { it > 0 } ?: sizePx
    return createBitmap(width, height).also { bitmap ->
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}
