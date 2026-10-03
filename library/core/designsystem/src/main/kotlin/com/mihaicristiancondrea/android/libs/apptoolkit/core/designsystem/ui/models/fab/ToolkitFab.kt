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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon

/**
 * A floating action button, described rather than drawn: the Toolkit's scaffolds turn a list of
 * these into a column of buttons at the bottom end of the screen, with the Toolkit's click sound,
 * haptics, press bounce and animated icons.
 *
 * ```
 * listOf(
 *     ToolkitFab(ToolkitIcon.Vector(Icons.Outlined.Search), onClick = ::search, size = FabSize.Small, color = FabColor.Secondary),
 *     ToolkitFab(ToolkitIcon.Vector(Icons.Outlined.Add), onClick = ::add, label = "New item", expanded = !scrolled),
 * )
 * ```
 *
 * @param icon The button's icon. An animated vector or Lottie icon plays on each click.
 * @param onClick What the button does.
 * @param label Makes the button an extended one, with this text beside the icon.
 * @param contentDescription What a screen reader says; defaults to [label]. Give one for a button
 * without a label.
 * @param size Material 3's floating action button sizes, for both plain and extended buttons.
 * @param color Which container colour of the theme the button takes.
 * @param expanded For an extended button: false folds it to its icon, as when a list scrolls.
 * @param visible False scales the button out; it keeps its place in the list for when it returns.
 * @param id Tells buttons apart as the list changes, so each animates in and out on its own.
 * Defaults to the label or, without one, the content description.
 */
@Immutable
class ToolkitFab(
    val icon: ToolkitIcon,
    val onClick: () -> Unit,
    val label: String? = null,
    val contentDescription: String? = label,
    val size: FabSize = FabSize.Regular,
    val color: FabColor = FabColor.Primary,
    val expanded: Boolean = true,
    val visible: Boolean = true,
    val id: Any = label ?: contentDescription ?: icon,
)

/** The sizes Material 3 gives a floating action button, from the smallest to the largest. */
enum class FabSize { Small, Regular, Medium, Large }

/**
 * The theme colour a floating action button's container takes: the primary, secondary or
 * tertiary container, or a raised surface for a button that should not draw the eye.
 */
enum class FabColor { Primary, Secondary, Tertiary, Surface }
