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
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
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
 * Scrollable widget that lists developer app icons and opens app/install page on tap.
 */
class AppIconsWidget : GlanceAppWidget(errorUiLayout = R.layout.widget_app_icons_error) {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes = setOf(SMALL_SIZE, MEDIUM_SIZE, LARGE_SIZE),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val apps = loadApps(context = context)
        provideContent {
            AppIconsWidgetContent(
                apps = apps,
                widgetTitle = context.getString(R.string.widget_apps_title),
            )
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val previewApps = previewEntries(context = context)
        provideContent {
            AppIconsWidgetContent(
                apps = previewApps,
                widgetTitle = context.getString(R.string.widget_apps_title),
            )
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

        apps.ifEmpty { listOf(createFallbackEntry(context)) }
            .map { app ->
                WidgetAppEntry(
                    app = app,
                    icon = resolveAppIcon(context = context, packageName = app.packageName),
                )
            }
            .toImmutableList()
    }

    private fun previewEntries(context: Context): ImmutableList<WidgetAppEntry> = listOf(
        createFallbackEntry(context),
        createFallbackEntry(context).copy(
            name = context.getString(R.string.widget_apps_preview_secondary_item),
        ),
        createFallbackEntry(context).copy(
            name = context.getString(R.string.widget_apps_preview_third_item),
        ),
    ).map { app ->
        WidgetAppEntry(
            app = app,
            icon = resolveAppIcon(context = context, packageName = app.packageName),
        )
    }.toImmutableList()

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

        return drawable.toBitmap(sizePx = 96)
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
        val SMALL_SIZE: DpSize = DpSize(width = 120.dp, height = 120.dp)
        val MEDIUM_SIZE: DpSize = DpSize(width = 180.dp, height = 180.dp)
        val LARGE_SIZE: DpSize = DpSize(width = 250.dp, height = 250.dp)
    }
}

@Composable
private fun AppIconsWidgetContent(
    apps: ImmutableList<WidgetAppEntry>,
    widgetTitle: String,
) {
    val widgetSize = LocalSize.current
    val showHeader = widgetSize.width >= AppIconsWidget.MEDIUM_SIZE.width
    val maxVisibleItems = when {
        widgetSize.height >= AppIconsWidget.LARGE_SIZE.height -> 7
        widgetSize.height >= AppIconsWidget.MEDIUM_SIZE.height -> 5
        else -> 3
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        if (showHeader) {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(text = widgetTitle, maxLines = 1)
            }
            Spacer(modifier = GlanceModifier.height(2.dp))
        }

        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
        ) {
            items(items = apps.take(maxVisibleItems)) { item ->
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .appWidgetClickAction(item.app.packageName),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        provider = ImageProvider(item.icon),
                        contentDescription = item.app.name,
                        modifier = GlanceModifier.size(36.dp),
                    )
                    Spacer(modifier = GlanceModifier.size(10.dp))
                    Text(
                        text = item.app.name,
                        maxLines = 1,
                    )
                }
                Spacer(modifier = GlanceModifier.height(4.dp))
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
