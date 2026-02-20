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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.model.AppInfo
import com.d4rk.android.apps.apptoolkit.app.apps.common.domain.usecases.FetchDeveloperAppsUseCase
import com.d4rk.android.apps.apptoolkit.app.widgets.apps.domain.actions.OpenAppOrStoreAction
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import kotlinx.coroutines.flow.first
import org.koin.core.context.GlobalContext

/**
 * Scrollable widget that lists developer app icons and opens app/install page on tap.
 */
class AppIconsWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes = setOf(DpSize(width = 120.dp, height = 120.dp)),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val apps = loadApps(context = context)
        provideContent { AppIconsWidgetContent(apps = apps) }
    }

    private suspend fun loadApps(context: Context): List<WidgetAppEntry> {
        val fetchAppsUseCase = GlobalContext.get().get<FetchDeveloperAppsUseCase>()
        val apps = when (val state = fetchAppsUseCase().first { it !is DataState.Loading }) {
            is DataState.Success -> state.data
            is DataState.Error -> state.data ?: emptyList()
            is DataState.Loading -> emptyList()
        }

        val source = if (apps.isNotEmpty()) apps else listOf(createFallbackEntry(context))
        return source.map { app ->
            WidgetAppEntry(
                app = app,
                icon = resolveAppIcon(context = context, packageName = app.packageName),
            )
        }
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

        return drawable.toBitmap(sizePx = 96)
    }
}

@Composable
private fun AppIconsWidgetContent(apps: List<WidgetAppEntry>) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        items(apps) { item ->
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
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}
