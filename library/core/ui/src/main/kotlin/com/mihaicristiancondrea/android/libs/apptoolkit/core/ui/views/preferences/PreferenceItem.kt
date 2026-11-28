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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.colorscheme.darken
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.componentEntrance

/**
 * Shapes and colors for Toolkit preference rows built on Material 3 [SegmentedListItem].
 */
object PreferenceItemDefaults {

    /**
     * Shapes for the row at [index] in a group of [count] rows, cut from the theme's shape tokens:
     * [outerShape] rounds the outside of the group and [innerShape] the seams between rows. Unlike
     * `ListItemDefaults.segmentedShapes`, which always rounds the outside with `shapes.large`, the
     * outer shape is chosen per group. A selected or checked row takes [outerShape] on every corner,
     * as Material's segmented lists do; the other interaction states keep the resting shape.
     *
     * Pass `.shape` of the result to a `Card` or `Surface` that sits in the same group.
     */
    @Composable
    fun segmentedShapes(
        index: Int,
        count: Int,
        outerShape: CornerBasedShape = MaterialTheme.shapes.largeIncreased,
        innerShape: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    ): ListItemShapes {
        val isFirst = index == 0
        val isLast = index >= count - 1
        val shape = remember(isFirst, isLast, outerShape, innerShape) {
            innerShape.copy(
                topStart = if (isFirst) outerShape.topStart else innerShape.topStart,
                topEnd = if (isFirst) outerShape.topEnd else innerShape.topEnd,
                bottomStart = if (isLast) outerShape.bottomStart else innerShape.bottomStart,
                bottomEnd = if (isLast) outerShape.bottomEnd else innerShape.bottomEnd,
            )
        }
        return ListItemDefaults.shapes(
            shape = shape,
            selectedShape = outerShape,
            pressedShape = shape,
            focusedShape = shape,
            hoveredShape = shape,
            draggedShape = shape,
        )
    }

    /**
     * Segmented list item colors whose resting surface matches Material 3 filled cards so grouped
     * rows separate cleanly from a plain [MaterialTheme.colorScheme] surface. Icons, summaries and
     * trailing content use [contentColor], and a selected or checked row keeps the resting colors;
     * its control shows the state.
     */
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor: Color = containerColor,
    ): ListItemColors = ListItemDefaults.segmentedColors(
        containerColor = containerColor,
        contentColor = contentColor,
        leadingContentColor = contentColor,
        trailingContentColor = contentColor,
        supportingContentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        selectedContainerColor = containerColor,
        selectedContentColor = contentColor,
        selectedLeadingContentColor = contentColor,
        selectedTrailingContentColor = contentColor,
        selectedSupportingContentColor = contentColor,
    )
}

/**
 * Stateless preference row backed by Material 3 [ListItem], with Toolkit click feedback and
 * analytics. Supply display-ready text, with [title] meaningful on its own and [summary] adding
 * context.
 *
 * @param icon Any [ToolkitIcon]; an overload takes an [ImageVector].
 * @param ga4EventProvider Resolves analytics at click time, taking precedence over [ga4Event]
 * when it returns an event.
 */
@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    icon: ToolkitIcon? = null,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    enabled: Boolean = true,
    shapes: ListItemShapes = ListItemDefaults.shapes(
        shape = MaterialTheme.shapes.large,
    ),
    colors: ListItemColors = inheritedContentColors(),
    onClick: (() -> Unit)? = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    val telemetry = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val leadingContent: (@Composable () -> Unit)? = icon?.let {
        {
            PreferenceLeadingIcon(
                icon = it,
                useIconContainer = useIconContainer,
                iconColor = iconColor,
                iconContainerColor = iconContainerColor,
            )
        }
    }
    val supportingContent: (@Composable () -> Unit)? =
        if (title != null && summary != null) {
            { PreferenceSummaryText(text = summary) }
        } else {
            null
        }
    val mainContent: @Composable () -> Unit = {
        when {
            title != null -> PreferenceTitleText(text = title)
            summary != null -> PreferenceSummaryText(text = summary)
        }
    }

    if (onClick != null) {
        ListItem(
            onClick = {
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                telemetry.logGa4Event(ga4EventProvider?.invoke() ?: ga4Event)
                onClick()
            },
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            shapes = shapes,
            colors = colors,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
            leadingContent = leadingContent,
            supportingContent = supportingContent,
            content = mainContent,
        )
    } else {
        ListItem(
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            shapes = shapes,
            colors = colors,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
            leadingContent = leadingContent,
            supportingContent = supportingContent,
            content = mainContent,
        )
    }
}

/** [PreferenceItem] with an [ImageVector] icon. */
@Composable
fun PreferenceItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    enabled: Boolean = true,
    shapes: ListItemShapes = ListItemDefaults.shapes(
        shape = MaterialTheme.shapes.large,
    ),
    colors: ListItemColors = inheritedContentColors(),
    onClick: (() -> Unit)? = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    PreferenceItem(
        modifier = modifier,
        icon = ToolkitIcon.Vector(imageVector = icon),
        title = title,
        summary = summary,
        useIconContainer = useIconContainer,
        iconColor = iconColor,
        iconContainerColor = iconContainerColor,
        enabled = enabled,
        shapes = shapes,
        colors = colors,
        onClick = onClick,
        ga4Event = ga4Event,
        ga4EventProvider = ga4EventProvider,
    )
}

/**
 * Segmented preference row backed by Material 3 [SegmentedListItem], with Toolkit click feedback
 * and analytics. Pass [PreferenceItemDefaults.segmentedShapes] for grouped lists.
 */
@Composable
fun SettingsPreferenceItem(
    modifier: Modifier = Modifier,
    icon: ToolkitIcon? = null,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    enabled: Boolean = true,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
    colors: ListItemColors = PreferenceItemDefaults.colors(),
    onClick: (() -> Unit)? = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    val telemetry = LocalTelemetry.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val leadingContent: (@Composable () -> Unit)? = icon?.let {
        {
            PreferenceLeadingIcon(
                icon = it,
                useIconContainer = useIconContainer,
                iconColor = iconColor,
                iconContainerColor = iconContainerColor,
            )
        }
    }
    val supportingContent: (@Composable () -> Unit)? =
        if (title != null && summary != null) {
            { PreferenceSummaryText(text = summary) }
        } else {
            null
        }
    val mainContent: @Composable () -> Unit = {
        when {
            title != null -> PreferenceTitleText(text = title)
            summary != null -> PreferenceSummaryText(text = summary)
        }
    }

    if (onClick != null) {
        SegmentedListItem(
            onClick = {
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                telemetry.logGa4Event(ga4EventProvider?.invoke() ?: ga4Event)
                onClick()
            },
            shapes = shapes,
            modifier = modifier
                .componentEntrance()
                .fillMaxWidth(),
            enabled = enabled,
            colors = colors,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
            leadingContent = leadingContent,
            supportingContent = supportingContent,
            content = mainContent,
        )
    } else {
        SegmentedListItem(
            shapes = shapes,
            modifier = modifier
                .componentEntrance()
                .fillMaxWidth(),
            enabled = enabled,
            colors = colors,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(all = MaterialTheme.spacing.large),
            leadingContent = leadingContent,
            supportingContent = supportingContent,
            content = mainContent,
        )
    }
}

/** [SettingsPreferenceItem] with an [ImageVector] icon. */
@Composable
fun SettingsPreferenceItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    title: String? = null,
    summary: String? = null,
    useIconContainer: Boolean = false,
    iconColor: Color? = null,
    iconContainerColor: Color? = null,
    enabled: Boolean = true,
    shapes: ListItemShapes = PreferenceItemDefaults.segmentedShapes(index = 0, count = 1),
    colors: ListItemColors = PreferenceItemDefaults.colors(),
    onClick: (() -> Unit)? = {},
    ga4Event: Ga4EventData? = null,
    ga4EventProvider: (() -> Ga4EventData?)? = null,
) {
    SettingsPreferenceItem(
        modifier = modifier,
        icon = ToolkitIcon.Vector(imageVector = icon),
        title = title,
        summary = summary,
        useIconContainer = useIconContainer,
        iconColor = iconColor,
        iconContainerColor = iconContainerColor,
        enabled = enabled,
        shapes = shapes,
        colors = colors,
        onClick = onClick,
        ga4Event = ga4Event,
        ga4EventProvider = ga4EventProvider,
    )
}

@Composable
internal fun PreferenceTitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
internal fun PreferenceSummaryText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
    )
}

/** Leading icon of a row with a trailing switch or checkbox. */
@Composable
internal fun ControlRowLeadingIcon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier.padding(end = MaterialTheme.spacing.largeIncreased),
    )
}

/** Transparent row colors that follow the parent's [LocalContentColor], as a plain row did. */
@Composable
private fun inheritedContentColors(): ListItemColors {
    val contentColor = LocalContentColor.current
    return ListItemDefaults.colors(
        containerColor = Color.Transparent,
        contentColor = contentColor,
        leadingContentColor = contentColor,
        trailingContentColor = contentColor,
        supportingContentColor = contentColor,
        disabledContainerColor = Color.Transparent,
    )
}

/**
 * Leading icon of a preference row. The end padding widens ListItem's 12 dp leading gap to the
 * 16 dp the rows have always had between icon and text.
 */
@Composable
private fun PreferenceLeadingIcon(
    icon: ToolkitIcon,
    useIconContainer: Boolean,
    iconColor: Color?,
    iconContainerColor: Color?,
) {
    val gapModifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall)
    if (useIconContainer) {
        val containerColor = iconContainerColor ?: MaterialTheme.colorScheme.secondaryContainer
        val tint = if (iconColor == null && iconContainerColor == null) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            iconColor ?: containerColor.darken(factor = 0.6f)
        }

        Surface(
            modifier = gapModifier.size(size = MaterialTheme.size.extraLarge),
            shape = CircleShape,
            color = containerColor,
        ) {
            Box(contentAlignment = Alignment.Center) {
                ToolkitIconContent(
                    icon = icon,
                    contentDescription = null,
                    modifier = Modifier.size(size = MaterialTheme.size.medium),
                    tint = tint,
                )
            }
        }
    } else {
        ToolkitIconContent(
            icon = icon,
            contentDescription = null,
            modifier = gapModifier.size(size = MaterialTheme.size.medium),
        )
    }
}
