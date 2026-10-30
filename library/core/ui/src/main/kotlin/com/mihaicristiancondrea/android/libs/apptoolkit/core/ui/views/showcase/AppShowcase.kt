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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItems
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import coil3.compose.AsyncImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import kotlinx.collections.immutable.ImmutableList

/**
 * One app in an [AppShowcase].
 *
 * @property key Stable identity, the package name.
 * @property installed Whether the app is on this device: its card then opens it, and its action
 * reads as "open" rather than as the store.
 */
@Immutable
data class ShowcaseApp(
    val key: String,
    val name: String,
    val category: String,
    val description: String,
    val iconUrl: String,
    val installed: Boolean = false,
)

/** What an [AppShowcase] shows: the apps, or why there are none yet. */
@Immutable
sealed interface AppShowcaseState {
    data object Loading : AppShowcaseState
    data class Ready(val apps: ImmutableList<ShowcaseApp>) : AppShowcaseState
    data object Empty : AppShowcaseState
    data object Failed : AppShowcaseState
}

/**
 * The words an [AppShowcase] uses. The host supplies them, so the section speaks the app's
 * language and names its own store; loading and retry default to the Toolkit's strings.
 *
 * @property storeActionDescription Spoken for a card's store action, given the app's name.
 * @property openActionDescription Spoken for an installed app's action, given its name.
 */
@Stable
class AppShowcaseLabels(
    val title: String,
    val viewAll: String,
    val viewAllDescription: String,
    val store: String,
    val open: String,
    val empty: String,
    val failed: String,
    val loading: String,
    val retry: String,
    val storeActionDescription: (appName: String) -> String,
    val openActionDescription: (appName: String) -> String,
)

/** [AppShowcaseLabels] with the Toolkit's own loading and retry strings. */
@Composable
fun appShowcaseLabels(
    title: String,
    viewAll: String,
    viewAllDescription: String,
    store: String,
    open: String,
    empty: String,
    failed: String,
    storeActionDescription: (appName: String) -> String,
    openActionDescription: (appName: String) -> String,
    loading: String = stringResource(R.string.loading),
    retry: String = stringResource(R.string.try_again),
): AppShowcaseLabels = AppShowcaseLabels(
    title = title,
    viewAll = viewAll,
    viewAllDescription = viewAllDescription,
    store = store,
    open = open,
    empty = empty,
    failed = failed,
    loading = loading,
    retry = retry,
    storeActionDescription = storeActionDescription,
    openActionDescription = openActionDescription,
)

/**
 * A "more apps" section, the Compose counterpart of Material Next's `mnw-app-showcase`: a heading
 * with the section's icon and a "View all" action, then the apps as filled cards with their icon,
 * name, category, a full-width description and a tonal store (or open) action.
 *
 * It owns no data and opens nothing. The host loads [state] and decides what a tap does; the
 * sample opens an installed app and shows the details sheet of one that is not.
 *
 * The cards share one width and one height, so the row never jumps. A phone shows them as a
 * carousel that snaps to each card with the next one peeking in; wider windows show as many whole
 * cards side by side as fit at 260 dp or more, still scrolling when there are more.
 *
 * @param icon The section's icon, beside the title, as the screen's other sections draw theirs.
 * @param onAppClick A card was tapped.
 * @param onAppAction A card's action button was tapped.
 * @param onViewAll The heading's "View all" action was tapped.
 * @param onRetry The failed state's retry action was tapped.
 * @param contentPadding Horizontal padding of the row, so cards can scroll to the screen's edges
 * while resting aligned with the content.
 */
@Composable
fun AppShowcase(
    state: AppShowcaseState,
    labels: AppShowcaseLabels,
    icon: ImageVector,
    onAppClick: (ShowcaseApp) -> Unit,
    onAppAction: (ShowcaseApp) -> Unit,
    onViewAll: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppShowcaseHeading(labels, icon, onViewAll, Modifier.padding(contentPadding))
        when (state) {
            AppShowcaseState.Loading -> ShowcaseMessage(Modifier.padding(contentPadding)) {
                ShowcaseProgress()
                Text(labels.loading, style = MaterialTheme.typography.bodyMedium)
            }
            AppShowcaseState.Empty -> ShowcaseMessage(Modifier.padding(contentPadding)) {
                Text(labels.empty, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            AppShowcaseState.Failed -> ShowcaseMessage(Modifier.padding(contentPadding)) {
                Text(
                    labels.failed,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f, fill = false),
                )
                TextButton(onClick = onRetry) { Text(labels.retry) }
            }
            is AppShowcaseState.Ready -> ShowcaseRow(state.apps, labels, onAppClick, onAppAction, contentPadding)
        }
    }
}

@Composable
private fun AppShowcaseHeading(labels: AppShowcaseLabels, icon: ImageVector, onViewAll: () -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier.animateVisibility().fillMaxWidth().padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(
            text = labels.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f).semantics { heading() },
        )
        TextButton(
            onClick = onViewAll,
            modifier = Modifier.semantics { contentDescription = labels.viewAllDescription },
        ) {
            Text(labels.viewAll)
            Icon(
                Icons.AutoMirrored.Outlined.OpenInNew,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp).size(18.dp),
            )
        }
    }
}

@Composable
private fun ShowcaseMessage(modifier: Modifier, content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 96.dp).semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ShowcaseProgress() {
    CircularWavyProgressIndicator(modifier = Modifier.size(32.dp))
}

@Composable
private fun ShowcaseRow(
    apps: ImmutableList<ShowcaseApp>,
    labels: AppShowcaseLabels,
    onAppClick: (ShowcaseApp) -> Unit,
    onAppAction: (ShowcaseApp) -> Unit,
    contentPadding: PaddingValues,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val gap = 12.dp
        val cardWidth = showcaseCardWidth(maxWidth, gap)
        val listState = rememberLazyListState()
        LazyRow(
            state = listState,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(gap),
            flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Start),
        ) {
            animatedItems(apps, key = { it.key }) { app ->
                ShowcaseAppCard(app, labels, { onAppClick(app) }, { onAppAction(app) }, Modifier.width(cardWidth))
            }
        }
    }
}

/**
 * A card's width for a row [available] wide: on a phone, most of the row so the next card peeks
 * in; wider, as many whole cards of at least [MinCardWidth] as fit.
 */
internal fun showcaseCardWidth(available: Dp, gap: Dp): Dp {
    if (available < 600.dp) return min(available * 0.86f, 360.dp)
    val columns = ((available + gap) / (MinCardWidth + gap)).toInt().coerceAtLeast(1)
    return (available - gap * (columns - 1)) / columns
}

private val MinCardWidth = 260.dp

/**
 * One app on a filled surface with 28 dp corners, like the surrounding tool widgets.
 * The header keeps the icon beside the name; description and action use the whole card width.
 * Reserved text lines keep every card the same height without clipping large-font content.
 */
@Composable
fun ShowcaseAppCard(
    app: ShowcaseApp,
    labels: AppShowcaseLabels,
    onClick: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (app.iconUrl.isBlank()) {
                            Icon(Icons.Outlined.Apps, contentDescription = null, modifier = Modifier.size(32.dp))
                        } else {
                            AsyncImage(
                                model = app.iconUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
                            )
                        }
                    }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = app.category,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Text(
                text = app.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            val actionDescription = if (app.installed) labels.openActionDescription(app.name) else labels.storeActionDescription(app.name)
            FilledTonalButton(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = actionDescription
                        role = Role.Button
                    },
            ) {
                Icon(
                    if (app.installed) Icons.AutoMirrored.Outlined.OpenInNew else Icons.Outlined.Store,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Text(if (app.installed) labels.open else labels.store, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
