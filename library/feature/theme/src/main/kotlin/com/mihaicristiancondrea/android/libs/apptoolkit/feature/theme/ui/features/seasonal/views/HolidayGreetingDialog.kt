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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.HolidaySeason
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R
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
 * keep the current theme, since nothing was confirmed.
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
        // The card sizes itself from the window, so the window spans the screen and a tap beside
        // the card reaches [HolidayGreetingContent] rather than the platform's outside check.
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        HolidayGreetingContent(season = season, onAnswer = answer)
    }
}

/**
 * The greeting inside its window: the card centred, and the space around it, which declines the
 * theme when tapped. Sized from the space it is given, so previews and tests render it without a
 * dialog window.
 */
@Composable
internal fun HolidayGreetingContent(
    season: HolidaySeason,
    onAnswer: (useHolidayTheme: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = remember(season) { season.greetingStyle() }
    val currentOnAnswer by rememberUpdatedState(onAnswer)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            // A tap beside the card dismisses it. A gesture rather than a click, so the space around
            // the card is not announced as a button; back dismisses it for TalkBack users.
            .pointerInput(Unit) { detectTapGestures { currentOnAnswer(false) } }
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center,
    ) {
        val cardWidth: Dp = min(maxWidth - WindowMargin, MaxCardWidth).coerceAtLeast(min(maxWidth, MinCardWidth))
        val maxCardHeight: Dp = (maxHeight - WindowMargin).coerceAtLeast(min(maxHeight, MinCardHeight))
        val preferredHeight: Dp = min(min(maxHeight * PreferredHeightFraction, MaxPreferredCardHeight), maxCardHeight)
        // A short window, such as a phone on its side: tighter spacing and less of the scene kept
        // clear, so both choices fit without scrolling.
        val compact: Boolean = maxCardHeight < CompactCardHeight
        val title = stringResource(style.title)
        Box(
            modifier = Modifier
                .width(cardWidth)
                .heightIn(max = maxCardHeight)
                .shadow(elevation = 8.dp, shape = CardShape)
                .clip(CardShape)
                // Below the illustration the card continues its bottom edge.
                .background(style.bottomEdge)
                // Taps on the card itself stay on it instead of reaching the space around it.
                .pointerInput(Unit) { detectTapGestures { } }
                .semantics { paneTitle = title }
                // Large fonts or a short window: the card keeps its full height and scrolls.
                .verticalScroll(rememberScrollState()),
        ) {
            GreetingLayout(
                style = style,
                preferredHeight = preferredHeight,
                minVisibleArt = if (compact) MIN_VISIBLE_ART_COMPACT else MIN_VISIBLE_ART,
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
                    // Decorative: the title and message say what the art shows.
                    contentDescription = null,
                    // The slot already has the art's aspect ratio; filling it exactly avoids the
                    // pixel of background Fit leaves beside the art when the height rounds down.
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
        val artHeight = (width / ILLUSTRATION_ASPECT_RATIO).roundToInt()
        val artPlaceable = art.first().measure(Constraints.fixed(width, artHeight))
        val sceneTop = (artHeight * SCENE_TOP_FRACTION).roundToInt()
        val sceneHeight = (artHeight * SCENE_HEIGHT_FRACTION).roundToInt()
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

/** The title and message on the top scrim, which fades into the illustration. */
@Composable
private fun GreetingHeader(style: HolidayGreetingStyle, title: String, compact: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Eased out over its lower part, so the scrim melts into the art without a line where
                // it ends.
                Brush.verticalGradient(
                    0f to style.topScrim,
                    0.5f to style.topScrim.copy(alpha = 0.92f),
                    0.7f to style.topScrim.copy(alpha = 0.6f),
                    0.85f to style.topScrim.copy(alpha = 0.25f),
                    1f to style.topScrim.copy(alpha = 0f),
                ),
            )
            .padding(
                start = 24.dp,
                top = if (compact) 20.dp else 28.dp,
                end = 24.dp,
                bottom = if (compact) 28.dp else 48.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
 * each other.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GreetingActions(
    style: HolidayGreetingStyle,
    compact: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    // The outlined button draws its border from the theme's outline colours.
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
                    .padding(start = 24.dp, top = if (compact) 28.dp else 40.dp, end = 24.dp, bottom = if (compact) 20.dp else 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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

/**
 * One season's greeting: its art, words and colours. The edges are the colours along the top and
 * bottom of the art, so the card continues it seamlessly; every text and button colour meets
 * WCAG AA against what it is drawn over.
 */
@Immutable
private data class HolidayGreetingStyle(
    @param:DrawableRes val illustration: Int,
    @param:StringRes val title: Int,
    @param:StringRes val message: Int,
    @param:StringRes val accept: Int,
    val topEdge: Color,
    val bottomEdge: Color,
    val topScrim: Color,
    val bottomScrim: Color,
    val titleColor: Color,
    val messageColor: Color,
    val primaryContainer: Color,
    val primaryContent: Color,
    val secondaryContent: Color,
    val secondaryOutline: Color,
)

private fun HolidaySeason.greetingStyle(): HolidayGreetingStyle = when (this) {
    // A frosty night: navy sky under white text, snow fading to frost white under icy-blue buttons.
    HolidaySeason.CHRISTMAS -> HolidayGreetingStyle(
        illustration = R.drawable.il_christmas_season,
        title = R.string.holiday_greeting_christmas_title,
        message = R.string.holiday_greeting_christmas_message,
        accept = R.string.holiday_greeting_christmas_accept,
        topEdge = Color(0xFF0A1F3A),
        bottomEdge = Color(0xFFF2F8FF),
        topScrim = Color(0xFF0A1F3A),
        bottomScrim = Color(0xFFF2F8FF),
        titleColor = Color.White,
        messageColor = Color(0xFFDCEEFF),
        primaryContainer = Color(0xFF1658B8),
        primaryContent = Color.White,
        secondaryContent = Color(0xFF0F3F80),
        secondaryOutline = Color(0xFF5C87C2),
    )

    // A moonlit night: deep purple above and below, a glowing orange button.
    HolidaySeason.HALLOWEEN -> HolidayGreetingStyle(
        illustration = R.drawable.il_halloween_season,
        title = R.string.holiday_greeting_halloween_title,
        message = R.string.holiday_greeting_halloween_message,
        accept = R.string.holiday_greeting_halloween_accept,
        topEdge = Color(0xFF1C1233),
        bottomEdge = Color(0xFF1E1233),
        topScrim = Color(0xFF1F1438),
        bottomScrim = Color(0xFF1E1233),
        titleColor = Color.White,
        messageColor = Color(0xFFE6DAF7),
        primaryContainer = Color(0xFFFF8C1A),
        primaryContent = Color(0xFF2B1000),
        secondaryContent = Color(0xFFFFC58A),
        secondaryOutline = Color(0xFFFF9E40),
    )
}

/** The illustrations' viewport, 360 by 480. */
private const val ILLUSTRATION_ASPECT_RATIO: Float = 360f / 480f

/** Where the scene, the art's middle band, starts: 140 units down, under the extra sky. */
private const val SCENE_TOP_FRACTION: Float = 140f / 480f

/** How tall the scene is: 200 units, with ground running on below it. */
private const val SCENE_HEIGHT_FRACTION: Float = 200f / 480f

/** How much of the scene's height stays clear of the scrims at least, when the text needs room. */
private const val MIN_VISIBLE_ART: Float = 0.55f

/** The same in a short window, where the choices matter more than the whole of the art. */
private const val MIN_VISIBLE_ART_COMPACT: Float = 0.3f

/** Cards that may be no taller than this lay out compactly. */
private val CompactCardHeight: Dp = 420.dp

private const val PreferredHeightFraction: Float = 0.65f
private val WindowMargin: Dp = 40.dp
private val MaxCardWidth: Dp = 400.dp
private val MinCardWidth: Dp = 280.dp
private val MaxPreferredCardHeight: Dp = 500.dp
private val MinCardHeight: Dp = 240.dp
private val CardShape = RoundedCornerShape(28.dp)

@Preview(name = "Christmas", widthDp = 411, heightDp = 891)
@Composable
private fun ChristmasGreetingPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Christmas, dark", widthDp = 411, heightDp = 891)
@Composable
private fun ChristmasGreetingDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween", widthDp = 411, heightDp = 891)
@Composable
private fun HalloweenGreetingPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Halloween, dark", widthDp = 411, heightDp = 891)
@Composable
private fun HalloweenGreetingDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Christmas, compact phone", widthDp = 320, heightDp = 568)
@Composable
private fun ChristmasGreetingCompactPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween, landscape", widthDp = 891, heightDp = 411)
@Composable
private fun HalloweenGreetingLandscapePreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}

@Preview(name = "Christmas, tablet", widthDp = 800, heightDp = 1280)
@Composable
private fun ChristmasGreetingTabletPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.CHRISTMAS, onAnswer = {})
    }
}

@Preview(name = "Halloween, large font", widthDp = 411, heightDp = 891, fontScale = 2f)
@Composable
private fun HalloweenGreetingLargeFontPreview() {
    MaterialTheme {
        HolidayGreetingContent(season = HolidaySeason.HALLOWEEN, onAnswer = {})
    }
}
