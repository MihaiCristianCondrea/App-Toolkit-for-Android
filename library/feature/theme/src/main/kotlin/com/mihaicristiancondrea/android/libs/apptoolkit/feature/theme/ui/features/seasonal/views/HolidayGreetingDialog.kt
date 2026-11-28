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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.elevation.elevation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.constants.HolidayGreetingLayoutConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.mappers.toHolidayGreetingStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.models.HolidayGreetingStyle
import kotlin.math.roundToInt

/**
 * Greets a holiday and offers its theme for the length of the holiday.
 *
 * A floating card with the season's illustration as its whole background: the title and message
 * over a scrim at the top, the illustration in the middle, and the two choices over a scrim at the
 * bottom. Its colours are the season's own, whatever theme the app wears, so the art and the text
 * over it look the same, and stay readable, in light and dark mode.
 *
 * The filled button takes the holiday theme; the outlined one, back, and a tap outside the card
 * keep the current theme, since nothing was confirmed. The window spans the screen, because the card
 * sizes itself from it and a tap beside the card has to reach [HolidayGreetingContent] rather than
 * the platform's outside check.
 *
 * @param onAnswer Called once with whether the holiday theme should be applied.
 */
@Composable
fun HolidayGreetingDialog(
    season: HolidaySeason,
    onAnswer: (useHolidayTheme: Boolean) -> Unit,
) {
    val answer = rememberSingleAnswer(onAnswer)
    Dialog(
        onDismissRequest = { answer(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        HolidayGreetingContent(season = season, onAnswer = answer)
    }
}

/**
 * The greeting inside its window: the card centred, and the space around it, which declines the
 * theme when tapped. Sized from the space it is given, so previews and tests render it without a
 * dialog window.
 *
 * The space around the card takes a tap gesture rather than a click, so TalkBack does not announce
 * it as a button; back dismisses the dialog instead. Taps on the card stay on it. In a short window,
 * such as a phone on its side, spacing tightens and less of the scene is kept clear so both choices
 * fit; with large fonts the card keeps its full height and scrolls. Below the illustration the card
 * continues the art's bottom edge colour.
 */
@Composable
internal fun HolidayGreetingContent(
    season: HolidaySeason,
    onAnswer: (useHolidayTheme: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = remember(season) { season.toHolidayGreetingStyle() }
    val currentOnAnswer by rememberUpdatedState(onAnswer)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { currentOnAnswer(false) } }
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center,
    ) {
        val windowMargin: Dp = MaterialTheme.spacing.extraExtraLargeCompact
        val cardWidth: Dp = min(maxWidth - windowMargin, HolidayGreetingLayoutConstants.MAX_CARD_WIDTH)
            .coerceAtLeast(min(maxWidth, HolidayGreetingLayoutConstants.MIN_CARD_WIDTH))
        val maxCardHeight: Dp = (maxHeight - windowMargin)
            .coerceAtLeast(min(maxHeight, HolidayGreetingLayoutConstants.MIN_CARD_HEIGHT))
        val preferredHeight: Dp = min(
            min(
                maxHeight * HolidayGreetingLayoutConstants.PREFERRED_HEIGHT_FRACTION,
                HolidayGreetingLayoutConstants.MAX_PREFERRED_CARD_HEIGHT,
            ),
            maxCardHeight,
        )
        val compact: Boolean = maxCardHeight < HolidayGreetingLayoutConstants.COMPACT_CARD_HEIGHT
        val title = stringResource(style.title)
        val cardShape = MaterialTheme.shapes.extraLarge
        Box(
            modifier = Modifier
                .width(cardWidth)
                .heightIn(max = maxCardHeight)
                .shadow(elevation = MaterialTheme.elevation.level4, shape = cardShape)
                .clip(cardShape)
                .background(style.bottomEdge)
                .pointerInput(Unit) { detectTapGestures { } }
                .semantics { paneTitle = title }
                .verticalScroll(rememberScrollState()),
        ) {
            GreetingLayout(
                style = style,
                preferredHeight = preferredHeight,
                minVisibleArt = if (compact) {
                    HolidayGreetingLayoutConstants.MIN_VISIBLE_ART_COMPACT
                } else {
                    HolidayGreetingLayoutConstants.MIN_VISIBLE_ART
                },
                header = { GreetingHeader(style = style, title = title, compact = compact) },
                actions = {
                    GreetingActions(
                        style = style,
                        compact = compact,
                        onAccept = { currentOnAnswer(true) },
                        onDecline = { currentOnAnswer(false) },
                    )
                },
            )
        }
    }
}

/**
 * Lays the card out: [header] at the top and [actions] at the bottom, over the illustration,
 * which keeps its whole aspect ratio at the card's width. The art is a portrait scene whose sky
 * and ground run on above and below its middle band, the scene itself; that band is centred
 * between the text and the buttons, and the sky and ground continue under them. Where the card is
 * taller still, it continues the art's top and bottom edges. The card is at least
 * [preferredHeight], and grows when the text and buttons need more, always leaving most of the
 * scene in view.
 *
 * The art is decorative, since the title and message say what it shows. It fills its slot with
 * `ContentScale.FillBounds`: the slot already has the art's aspect ratio, and `Fit` would leave a
 * pixel of background beside it when the height rounds down.
 */
@Composable
private fun GreetingLayout(
    style: HolidayGreetingStyle,
    preferredHeight: Dp,
    minVisibleArt: Float,
    header: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val preferredHeightPx = with(density) { preferredHeight.roundToPx() }
    Layout(
        contents = listOf(
            { Box(Modifier.background(style.topEdge)) },
            {
                Image(
                    painter = painterResource(style.illustration),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            },
            header,
            actions,
        ),
    ) { (topFill, art, head, act), constraints ->
        val width = constraints.maxWidth
        val fullWidth = Constraints(minWidth = width, maxWidth = width)
        val headPlaceable = head.first().measure(fullWidth)
        val actPlaceable = act.first().measure(fullWidth)
        val artHeight = (width / HolidayGreetingLayoutConstants.ILLUSTRATION_ASPECT_RATIO).roundToInt()
        val artPlaceable = art.first().measure(Constraints.fixed(width, artHeight))
        val sceneTop = (artHeight * HolidayGreetingLayoutConstants.SCENE_TOP_FRACTION).roundToInt()
        val sceneHeight = (artHeight * HolidayGreetingLayoutConstants.SCENE_HEIGHT_FRACTION).roundToInt()
        val minArtGap = (sceneHeight * minVisibleArt).roundToInt()
        val height = maxOf(
            preferredHeightPx,
            headPlaceable.height + actPlaceable.height + minArtGap,
            constraints.minHeight,
        )
        val gap = height - headPlaceable.height - actPlaceable.height
        val artTop = headPlaceable.height + (gap - sceneHeight) / 2 - sceneTop
        val topFillPlaceable = topFill.first().measure(Constraints.fixed(width, (artTop + 1).coerceAtLeast(0)))
        layout(width, height) {
            topFillPlaceable.place(0, 0)
            artPlaceable.place(0, artTop)
            headPlaceable.place(0, 0)
            actPlaceable.place(0, height - actPlaceable.height)
        }
    }
}

/**
 * The title and message on the top scrim. The scrim eases out over its lower part, so it melts into
 * the illustration without a visible edge.
 */
@Composable
private fun GreetingHeader(style: HolidayGreetingStyle, title: String, compact: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to style.topScrim,
                    0.5f to style.topScrim.copy(alpha = 0.92f),
                    0.7f to style.topScrim.copy(alpha = 0.6f),
                    0.85f to style.topScrim.copy(alpha = 0.25f),
                    1f to style.topScrim.copy(alpha = 0f),
                ),
            )
            .padding(
                start = MaterialTheme.spacing.extraLargeCompact,
                top = if (compact) MaterialTheme.spacing.largeIncreased else MaterialTheme.spacing.extraLarge,
                end = MaterialTheme.spacing.extraLargeCompact,
                bottom = if (compact) MaterialTheme.spacing.extraLarge else MaterialTheme.spacing.extraExtraLarge,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = style.titleColor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(style.message),
            color = style.messageColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * The two choices on the bottom scrim, as a dialog's buttons: end-aligned, the main one last,
 * moving to a line of their own only when long translations or large fonts leave no room beside
 * each other. The theme's outline colours are replaced with the season's, because the outlined
 * button draws its border from them.
 */
@Composable
private fun GreetingActions(
    style: HolidayGreetingStyle,
    compact: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            outline = style.secondaryOutline,
            outlineVariant = style.secondaryOutline,
        ),
    ) {
        CompositionLocalProvider(LocalContentColor provides style.secondaryContent) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0f to style.bottomScrim.copy(alpha = 0f),
                            0.3f to style.bottomScrim.copy(alpha = 0.55f),
                            0.6f to style.bottomScrim.copy(alpha = 0.9f),
                            1f to style.bottomScrim,
                        ),
                    )
                    .padding(
                        start = MaterialTheme.spacing.extraLargeCompact,
                        top = if (compact) MaterialTheme.spacing.extraLarge else MaterialTheme.spacing.extraExtraLargeCompact,
                        end = MaterialTheme.spacing.extraLargeCompact,
                        bottom = if (compact) MaterialTheme.spacing.largeIncreased else MaterialTheme.spacing.extraLargeCompact,
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.End),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            ) {
                GeneralButton(
                    onClick = onDecline,
                    style = GeneralButtonStyle.Outlined,
                    label = stringResource(R.string.holiday_greeting_keep_theme),
                    contentColor = style.secondaryContent,
                )
                GeneralButton(
                    onClick = onAccept,
                    style = GeneralButtonStyle.Filled,
                    label = stringResource(style.accept),
                    containerColor = style.primaryContainer,
                    contentColor = style.primaryContent,
                )
            }
        }
    }
}

/**
 * Wraps [onAnswer] so only the first answer counts: a tap that lands while the dialog closes, or
 * back pressed right after a button, would otherwise answer twice.
 */
@Composable
private fun rememberSingleAnswer(onAnswer: (Boolean) -> Unit): (Boolean) -> Unit {
    val currentOnAnswer by rememberUpdatedState(onAnswer)
    return remember {
        var answered = false
        { useHolidayTheme: Boolean ->
            if (!answered) {
                answered = true
                currentOnAnswer(useHolidayTheme)
            }
        }
    }
}
