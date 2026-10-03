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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
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
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.repositories.DeveloperAppsRepository
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
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
        val savedState: AppIconsWidgetState? = loadSavedApps(context = context)
        provideContent {
            val freshState = remember { flow { emit(loadApps(context = context, savedState = savedState)) } }
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
    private suspend fun loadSavedApps(context: Context): AppIconsWidgetState? =
        try {
            developerAppsRepository().savedDeveloperApps()
                ?.takeIf { it.isNotEmpty() }
                ?.let { apps -> AppIconsWidgetState.Content(createEntries(context, apps)) }
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
        context: Context,
        savedState: AppIconsWidgetState?,
    ): AppIconsWidgetState =
        try {
            val apps = developerAppsRepository().fetchDeveloperApps()
            if (apps.isEmpty()) {
                AppIconsWidgetState.Empty
            } else {
                AppIconsWidgetState.Content(createEntries(context, apps))
            }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (failure: Exception) {
            savedState ?: AppIconsWidgetState.Error
        }

    private fun developerAppsRepository(): DeveloperAppsRepository =
        GlobalContext.get().get<DeveloperAppsRepository>()

    private fun dispatchers(): DispatcherProvider = GlobalContext.get().get<DispatcherProvider>()

    /**
     * Builds the visible entries, resolving their icons in parallel. The package manager lookups
     * and bitmap drawing block, so they run on IO; the repository calls before them are main-safe.
     */
    private suspend fun createEntries(
        context: Context,
        apps: List<AppInfo>
    ): ImmutableList<WidgetAppEntry> = withContext(dispatchers().io) {
        apps.take(MAX_GRID_ITEMS).map { app ->
            async {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                WidgetAppEntry(
                    app = app,
                    icon = resolveAppIcon(context, app),
                    destination = launchIntent ?: Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=${Uri.encode(app.packageName)}".toUri(),
                    ),
                )
            }
        }.awaitAll().toImmutableList()
    }

    /**
     * The installed app's own icon, else its catalogue icon, else this app's icon.
     *
     * Catalogue icons go through the app's Coil [ImageLoader][coil3.ImageLoader], the same one the
     * Apps tab uses, so an icon the app has shown comes from its disk cache instead of the network.
     * The bitmap is a software one at the size the widget draws, as `RemoteViews` needs.
     */
    private suspend fun resolveAppIcon(context: Context, app: AppInfo): Bitmap {
        val installedIcon = runCatching {
            context.packageManager.getApplicationIcon(app.packageName)
        }.getOrNull()
        if (installedIcon != null) return installedIcon.toBitmap(DEFAULT_ICON_BITMAP_SIZE_PX)

        val request = ImageRequest.Builder(context)
            .data(app.iconUrl)
            .size(DEFAULT_ICON_BITMAP_SIZE_PX)
            .allowHardware(false)
            .build()
        val remoteIcon = (SingletonImageLoader.get(context).execute(request) as? SuccessResult)
            ?.image
            ?.toBitmap(DEFAULT_ICON_BITMAP_SIZE_PX, DEFAULT_ICON_BITMAP_SIZE_PX)
        if (remoteIcon != null) return remoteIcon

        return context.packageManager.getApplicationIcon(context.packageName)
            .toBitmap(DEFAULT_ICON_BITMAP_SIZE_PX)
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

@Immutable
internal data class WidgetAppEntry(
    val app: AppInfo,
    val icon: Bitmap,
    val destination: Intent,
)

@Immutable
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
