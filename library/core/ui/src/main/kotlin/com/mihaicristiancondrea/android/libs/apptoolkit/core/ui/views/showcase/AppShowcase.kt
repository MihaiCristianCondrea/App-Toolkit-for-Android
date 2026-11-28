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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil3.compose.AsyncImage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItems
import kotlinx.collections.immutable.ImmutableList

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
 * @param onCardsVisibilityChanged Whether at least half the cards row is inside its viewport.
 * Hosts apply lifecycle and exposure-duration policy; the showcase sends no telemetry itself.
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
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.none),
    onCardsVisibilityChanged: (Boolean) -> Unit = {},
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
        AppShowcaseHeading(labels, icon, onViewAll, Modifier.padding(contentPadding))
        when (state) {
            AppShowcaseState.Loading -> ShowcaseMessage(Modifier.padding(contentPadding)) {
                CircularWavyProgressIndicator()
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
            is AppShowcaseState.Ready -> ShowcaseRow(state.apps, labels, onAppClick, onAppAction, contentPadding, onCardsVisibilityChanged)
        }
    }
}

@Composable
private fun AppShowcaseHeading(labels: AppShowcaseLabels, icon: ImageVector, onViewAll: () -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier.animateVisibility().fillMaxWidth().padding(start = MaterialTheme.spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(size = MaterialTheme.size.small))
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
                modifier = Modifier
                    .padding(start = ButtonDefaults.IconSpacing)
                    .size(ButtonDefaults.IconSize),
            )
        }
    }
}

@Composable
private fun ShowcaseMessage(modifier: Modifier, content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth().heightIn(min = 96.dp).semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        content = content,
    )
}

@Composable
private fun ShowcaseRow(
    apps: ImmutableList<ShowcaseApp>,
    labels: AppShowcaseLabels,
    onAppClick: (ShowcaseApp) -> Unit,
    onAppAction: (ShowcaseApp) -> Unit,
    contentPadding: PaddingValues,
    onCardsVisibilityChanged: (Boolean) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val listState = rememberLazyListState()
        LazyRow(
            modifier = Modifier.onVisibilityChanged(minFractionVisible = 0.5f, callback = onCardsVisibilityChanged),
            state = listState,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            flingBehavior = rememberSnapFlingBehavior(listState, SnapPosition.Start),
        ) {
            animatedItems(apps, key = { it.key }) { app ->
                ShowcaseAppCard(app, labels, { onAppClick(app) }, { onAppAction(app) }, Modifier.width(showcaseCardWidth(maxWidth, MaterialTheme.spacing.medium)))
            }
        }
    }
}

/**
 * A card's width for a row [available] wide: on a phone, most of the row so the next card peeks
 * in; wider, as many whole cards of at least [MinCardWidth] as fit.
 */
internal fun showcaseCardWidth(available: Dp, gap: Dp): Dp { // FIXME: Probably wrong here
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
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(
            Modifier.padding(
                horizontal = MaterialTheme.spacing.large,
                vertical = MaterialTheme.spacing.largeIncreased,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(size = MaterialTheme.size.huge),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (app.iconUrl.isBlank()) {
                            Icon(Icons.Outlined.Apps, contentDescription = null, modifier = Modifier.size(size = MaterialTheme.size.large))
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
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
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
            GeneralButton(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(),
                style = GeneralButtonStyle.Tonal,
                label = if (app.installed) labels.open else labels.store,
                icon = ToolkitIcon.Vector(if (app.installed) Icons.AutoMirrored.Outlined.OpenInNew else Icons.Outlined.Store),
                contentDescription = actionDescription,
            )
        }
    }
}
