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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

/**
 * The five Material 3 Expressive button size classes.
 *
 * Each entry stands for one expressive container height. Everything else a button needs at that
 * size — shape, content padding, icon spacing, and label typography — is derived from that height
 * through the `ButtonDefaults.*For(buttonHeight)` helpers, so the toolkit never restates expressive
 * metrics of its own.
 *
 * The icon glyph is the deliberate exception. `GeneralButton` draws it at a fixed size by default
 * rather than scaling it with the container, because an icon that grows with the button reads as
 * oversized beside a label. Passing `iconSize = null` to `GeneralButton` opts into the expressive
 * icon size for the chosen entry instead.
 *
 * [Small] is the toolkit default because it is `ButtonDefaults.MinHeight`, which is the height
 * `GeneralButton` rendered at before this size class existed.
 */
enum class ButtonMeasurements {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge,
}

/**
 * Expressive container height for this size class.
 *
 * This is the value the `ButtonDefaults.*For` helpers key off, so a caller aligning a sibling
 * composable with a toolkit button should measure against it.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val ButtonMeasurements.containerHeight: Dp
    get() = when (this) {
        ButtonMeasurements.ExtraSmall -> ButtonDefaults.ExtraSmallContainerHeight
        ButtonMeasurements.Small -> ButtonDefaults.MinHeight
        ButtonMeasurements.Medium -> ButtonDefaults.MediumContainerHeight
        ButtonMeasurements.Large -> ButtonDefaults.LargeContainerHeight
        ButtonMeasurements.ExtraLarge -> ButtonDefaults.ExtraLargeContainerHeight
    }

/** Resting and pressed shapes for a labelled button at this size. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ButtonMeasurements.buttonShapes(): ButtonShapes =
    ButtonDefaults.shapesFor(buttonHeight = containerHeight)

/** Content padding for a labelled button at this size. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun ButtonMeasurements.contentPadding(
    hasLeadingIcon: Boolean,
    hasTrailingIcon: Boolean,
): PaddingValues = ButtonDefaults.contentPaddingFor(containerHeight, hasLeadingIcon, hasTrailingIcon)

/** Label typography for a labelled button at this size. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ButtonMeasurements.labelTextStyle(): TextStyle =
    ButtonDefaults.textStyleFor(buttonHeight = containerHeight)

/** Icon size beside a label at this size. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal val ButtonMeasurements.labelIconSize: Dp
    get() = ButtonDefaults.iconSizeFor(containerHeight)

/** Gap between the icon and the label at this size. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal val ButtonMeasurements.labelIconSpacing: Dp
    get() = ButtonDefaults.iconSpacingFor(containerHeight)

/**
 * Container size for an icon-only button at this size class.
 *
 * Icon-only buttons follow the expressive icon-button metrics rather than the labelled-button
 * ones, so they resolve against `IconButtonDefaults` instead of deriving from [containerHeight].
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal val ButtonMeasurements.iconButtonContainerSize: DpSize
    get() = when (this) {
        ButtonMeasurements.ExtraSmall -> IconButtonDefaults.extraSmallContainerSize()
        ButtonMeasurements.Small -> IconButtonDefaults.smallContainerSize()
        ButtonMeasurements.Medium -> IconButtonDefaults.mediumContainerSize()
        ButtonMeasurements.Large -> IconButtonDefaults.largeContainerSize()
        ButtonMeasurements.ExtraLarge -> IconButtonDefaults.extraLargeContainerSize()
    }

/** Icon size for an icon-only button at this size class. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal val ButtonMeasurements.iconButtonIconSize: Dp
    get() = when (this) {
        ButtonMeasurements.ExtraSmall -> IconButtonDefaults.extraSmallIconSize
        ButtonMeasurements.Small -> IconButtonDefaults.smallIconSize
        ButtonMeasurements.Medium -> IconButtonDefaults.mediumIconSize
        ButtonMeasurements.Large -> IconButtonDefaults.largeIconSize
        ButtonMeasurements.ExtraLarge -> IconButtonDefaults.extraLargeIconSize
    }

/** Resting and pressed shapes for an icon-only button at this size class. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ButtonMeasurements.iconButtonShapes(): IconButtonShapes = when (this) {
    ButtonMeasurements.ExtraSmall -> IconButtonDefaults.shapes(
        shape = IconButtonDefaults.extraSmallRoundShape,
        pressedShape = IconButtonDefaults.extraSmallPressedShape,
    )

    ButtonMeasurements.Small -> IconButtonDefaults.shapes(
        shape = IconButtonDefaults.smallRoundShape,
        pressedShape = IconButtonDefaults.smallPressedShape,
    )

    ButtonMeasurements.Medium -> IconButtonDefaults.shapes(
        shape = IconButtonDefaults.mediumRoundShape,
        pressedShape = IconButtonDefaults.mediumPressedShape,
    )

    ButtonMeasurements.Large -> IconButtonDefaults.shapes(
        shape = IconButtonDefaults.largeRoundShape,
        pressedShape = IconButtonDefaults.largePressedShape,
    )

    ButtonMeasurements.ExtraLarge -> IconButtonDefaults.shapes(
        shape = IconButtonDefaults.extraLargeRoundShape,
        pressedShape = IconButtonDefaults.extraLargePressedShape,
    )
}
