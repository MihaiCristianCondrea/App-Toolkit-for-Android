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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIconContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormatAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.MarkdownFormattingBar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.rememberMarkdownVisualTransformation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.markdown.toTextFieldValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.getGroupedShape

/** Visual treatment of a text field. */
enum class GeneralTextFieldStyle {

    /** The Material filled field, the platform default. */
    Filled,

    /** The Material outlined field. */
    Outlined,

    /**
     * A filled field without its indicator line, cut to the corners of the group it sits in. Use it
     * for a block of fields that reads as one card, such as a form; `position` says where in the
     * group this one is.
     */
    Grouped,
}

/** How much a field knows about the Markdown its text is written in. */
enum class GeneralTextFieldMarkdown {

    /** Plain text. The field's own `visualTransformation` is used as given. */
    None,

    /** Markdown syntax is highlighted as it is typed. The text itself is never rewritten. */
    Highlight,

    /** Highlighting, plus the formatting bar below the field. */
    Editor,
}

/**
 * The toolkit's text field: a Material field with the toolkit's grouped styling, icon slot and
 * Markdown authoring available as options rather than as separate components.
 *
 * It is the input counterpart of `GeneralButton`: one entry point, native defaults, and every
 * variation reachable through a parameter. The defaults render exactly the Material filled field.
 *
 * [errorText] both marks the field as errored and replaces [supportingText] beneath it, so the
 * message and the state cannot drift apart. Pass [isError] on its own to mark the state without a
 * message.
 *
 * [markdown] is what makes this field a Markdown editor: [GeneralTextFieldMarkdown.Highlight] styles
 * the syntax as it is typed, and [GeneralTextFieldMarkdown.Editor] adds the formatting bar under the
 * field. Both replace [visualTransformation], because the field draws the Markdown source itself.
 * The bar edits that source and moves the caret, which is why this overload keeps the selection of
 * its own: the caller owns the text, the field owns the caret. Where a caller needs the caret too,
 * the [TextFieldValue] overload hands both over.
 *
 * [ga4Event] is logged once each time the field gains focus, which is the moment a person starts
 * writing in it; a field is not a button, so there is no per-keystroke event.
 *
 * @param value Text the field shows.
 * @param onValueChange Receives every edit, from typing and from the formatting bar alike.
 * @param modifier The [Modifier] applied to the field, or to the field and its bar together.
 * @param style Visual treatment; see [GeneralTextFieldStyle].
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the text can be selected and copied but not edited.
 * @param label Floating label. A field inside a group usually leaves this null, because the label
 *   reserves height whether or not it is showing, and states itself through [placeholder] instead.
 * @param placeholder Shown while the field is empty.
 * @param supportingText Line below the field, replaced by [errorText] while there is an error.
 * @param errorText Message describing the error; non-null also marks the field as errored.
 * @param isError Whether the field is in its error state. Defaults to whether [errorText] is set.
 * @param leadingIcon Icon before the text. In a group without labels it is what names the field for
 *   screen readers, so give it a [leadingIconContentDescription].
 * @param leadingIconContentDescription Accessibility description of [leadingIcon].
 * @param trailingIcon Icon after the text. With [onTrailingIconClick] it becomes a button.
 * @param trailingIconContentDescription Accessibility description of [trailingIcon].
 * @param onTrailingIconClick Action of the trailing icon, such as clearing the field.
 * @param singleLine Whether the field refuses line breaks and scrolls horizontally.
 * @param minLines Rows the field is at least as tall as.
 * @param maxLines Rows the field grows to before it scrolls its own content.
 * @param keyboardOptions Keyboard type, capitalization and IME action.
 * @param keyboardActions What the IME action does.
 * @param visualTransformation Applied to plain fields only; [markdown] replaces it.
 * @param textStyle Typography of the text itself.
 * @param markdown Markdown support; see [GeneralTextFieldMarkdown].
 * @param onMarkdownFormat Reports which formatting action was used, for hosts that log them.
 * @param position Where this field sits in a [GeneralTextFieldStyle.Grouped] block.
 * @param groupedOuterRadius Corner radius at the outside of that block.
 * @param shape Overrides the resting shape of the field.
 * @param colors Overrides the colors of the field.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data, logged when the field gains focus.
 */
@Composable
fun GeneralTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: GeneralTextFieldStyle = GeneralTextFieldStyle.Filled,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: ToolkitIcon? = null,
    leadingIconContentDescription: String? = null,
    trailingIcon: ToolkitIcon? = null,
    trailingIconContentDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current,
    markdown: GeneralTextFieldMarkdown = GeneralTextFieldMarkdown.None,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit = {},
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    groupedOuterRadius: Dp = SizeConstants.LargeMediumSize,
    shape: Shape? = null,
    colors: TextFieldColors? = null,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    // The formatting bar has to place the caret, so an editor keeps a TextFieldValue of its own and
    // reports only the text back. Everything else stays on the plain String field.
    if (markdown == GeneralTextFieldMarkdown.Editor) {
        var fieldValue: TextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                value = TextFieldValue(text = value, selection = TextRange(index = value.length)),
            )
        }

        LaunchedEffect(value) {
            if (value != fieldValue.text) {
                fieldValue = fieldValue.copy(
                    text = value,
                    selection = TextRange(index = value.length),
                )
            }
        }

        GeneralTextField(
            value = fieldValue,
            onValueChange = { updated ->
                val textChanged: Boolean = updated.text != fieldValue.text
                fieldValue = updated
                if (textChanged) onValueChange(updated.text)
            },
            modifier = modifier,
            style = style,
            enabled = enabled,
            readOnly = readOnly,
            label = label,
            placeholder = placeholder,
            supportingText = supportingText,
            errorText = errorText,
            isError = isError,
            leadingIcon = leadingIcon,
            leadingIconContentDescription = leadingIconContentDescription,
            trailingIcon = trailingIcon,
            trailingIconContentDescription = trailingIconContentDescription,
            onTrailingIconClick = onTrailingIconClick,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = textStyle,
            markdown = markdown,
            onMarkdownFormat = onMarkdownFormat,
            position = position,
            groupedOuterRadius = groupedOuterRadius,
            shape = shape,
            colors = colors,
            firebaseController = firebaseController,
            ga4Event = ga4Event,
        )
        return
    }

    val skin: GeneralTextFieldSkin = rememberGeneralTextFieldSkin(
        style = style,
        position = position,
        groupedOuterRadius = groupedOuterRadius,
        hasFormattingBar = false,
        shapeOverride = shape,
    )
    val slots = GeneralTextFieldSlots(
        label = label,
        placeholder = placeholder,
        supportingText = errorText ?: supportingText,
        leadingIcon = leadingIcon,
        leadingIconContentDescription = leadingIconContentDescription,
        trailingIcon = trailingIcon,
        trailingIconContentDescription = trailingIconContentDescription,
        onTrailingIconClick = onTrailingIconClick,
    )
    val fieldColors: TextFieldColors = colors ?: generalTextFieldColors(style = style)
    val fieldModifier: Modifier = Modifier
        .fillMaxWidth()
        .logFocusGain(firebaseController = firebaseController, ga4Event = ga4Event)
    val transformation: VisualTransformation = rememberFieldTransformation(
        markdown = markdown,
        visualTransformation = visualTransformation,
    )

    if (style == GeneralTextFieldStyle.Outlined) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.then(fieldModifier),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = slots.label(),
            placeholder = slots.placeholder(),
            leadingIcon = slots.leadingIcon(),
            trailingIcon = slots.trailingIcon(),
            supportingText = slots.supportingText(),
            isError = isError,
            visualTransformation = transformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            shape = skin.fieldShape,
            colors = fieldColors,
        )
        return
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(fieldModifier),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = slots.label(),
        placeholder = slots.placeholder(),
        leadingIcon = slots.leadingIcon(),
        trailingIcon = slots.trailingIcon(),
        supportingText = slots.supportingText(),
        isError = isError,
        visualTransformation = transformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        shape = skin.fieldShape,
        colors = fieldColors,
    )
}

/**
 * [TextFieldValue] variant, for callers that own the caret as well as the text.
 *
 * A Markdown editor needs exactly this, because its formatting actions place the caret between the
 * markers they insert. Take this overload when the caret is part of the state a screen restores, and
 * the [String] one otherwise.
 *
 * Every parameter behaves as it does on the [String] overload.
 */
@Composable
fun GeneralTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    style: GeneralTextFieldStyle = GeneralTextFieldStyle.Filled,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: ToolkitIcon? = null,
    leadingIconContentDescription: String? = null,
    trailingIcon: ToolkitIcon? = null,
    trailingIconContentDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current,
    markdown: GeneralTextFieldMarkdown = GeneralTextFieldMarkdown.None,
    onMarkdownFormat: (MarkdownFormatAction) -> Unit = {},
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    groupedOuterRadius: Dp = SizeConstants.LargeMediumSize,
    shape: Shape? = null,
    colors: TextFieldColors? = null,
    firebaseController: FirebaseController? = null,
    ga4Event: Ga4EventData? = null,
) {
    val hasFormattingBar: Boolean = markdown == GeneralTextFieldMarkdown.Editor
    val skin: GeneralTextFieldSkin = rememberGeneralTextFieldSkin(
        style = style,
        position = position,
        groupedOuterRadius = groupedOuterRadius,
        hasFormattingBar = hasFormattingBar,
        shapeOverride = shape,
    )
    val slots = GeneralTextFieldSlots(
        label = label,
        placeholder = placeholder,
        supportingText = errorText ?: supportingText,
        leadingIcon = leadingIcon,
        leadingIconContentDescription = leadingIconContentDescription,
        trailingIcon = trailingIcon,
        trailingIconContentDescription = trailingIconContentDescription,
        onTrailingIconClick = onTrailingIconClick,
    )
    val fieldColors: TextFieldColors = colors ?: generalTextFieldColors(style = style)
    val fieldModifier: Modifier = Modifier
        .fillMaxWidth()
        .logFocusGain(firebaseController = firebaseController, ga4Event = ga4Event)
    val transformation: VisualTransformation = rememberFieldTransformation(
        markdown = markdown,
        visualTransformation = visualTransformation,
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
    ) {
        if (style == GeneralTextFieldStyle.Outlined) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = fieldModifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                label = slots.label(),
                placeholder = slots.placeholder(),
                leadingIcon = slots.leadingIcon(),
                trailingIcon = slots.trailingIcon(),
                supportingText = slots.supportingText(),
                isError = isError,
                visualTransformation = transformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                shape = skin.fieldShape,
                colors = fieldColors,
            )
        } else {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = fieldModifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                label = slots.label(),
                placeholder = slots.placeholder(),
                leadingIcon = slots.leadingIcon(),
                trailingIcon = slots.trailingIcon(),
                supportingText = slots.supportingText(),
                isError = isError,
                visualTransformation = transformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                shape = skin.fieldShape,
                colors = fieldColors,
            )
        }

        if (hasFormattingBar && enabled && !readOnly) {
            MarkdownFormattingBar(
                value = value,
                onEdit = { action, edit ->
                    onValueChange(edit.toTextFieldValue())
                    onMarkdownFormat(action)
                },
                shape = skin.formattingBarShape,
                containerColor = skin.formattingBarColor,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Shapes and colors the chosen style gives the field and the bar under it. */
@Immutable
private data class GeneralTextFieldSkin(
    val fieldShape: Shape,
    val formattingBarShape: Shape,
    val formattingBarColor: Color,
)

/**
 * Cuts the field, and the formatting bar under it, to the style they are drawn in.
 *
 * A grouped field that carries a bar hands the lower half of its position to it, so the pair still
 * reads as the single block the position describes.
 */
@Composable
private fun rememberGeneralTextFieldSkin(
    style: GeneralTextFieldStyle,
    position: GroupedItemPosition,
    groupedOuterRadius: Dp,
    hasFormattingBar: Boolean,
    shapeOverride: Shape?,
): GeneralTextFieldSkin {
    val filledShape: Shape = TextFieldDefaults.shape
    val outlinedShape: Shape = OutlinedTextFieldDefaults.shape
    val filledBarColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest

    return remember(style, position, groupedOuterRadius, hasFormattingBar, shapeOverride,
        filledShape, outlinedShape, filledBarColor) {
        when (style) {
            GeneralTextFieldStyle.Grouped -> {
                val fieldPosition: GroupedItemPosition = if (!hasFormattingBar) position else when (position) {
                    GroupedItemPosition.SINGLE -> GroupedItemPosition.FIRST
                    GroupedItemPosition.LAST -> GroupedItemPosition.MIDDLE
                    else -> position
                }
                val barPosition: GroupedItemPosition = when (position) {
                    GroupedItemPosition.SINGLE, GroupedItemPosition.LAST -> GroupedItemPosition.LAST
                    else -> GroupedItemPosition.MIDDLE
                }
                GeneralTextFieldSkin(
                    fieldShape = shapeOverride ?: getGroupedShape(
                        position = fieldPosition,
                        outerRadius = groupedOuterRadius,
                    ),
                    formattingBarShape = getGroupedShape(
                        position = barPosition,
                        outerRadius = groupedOuterRadius,
                    ),
                    formattingBarColor = filledBarColor,
                )
            }

            GeneralTextFieldStyle.Outlined -> GeneralTextFieldSkin(
                fieldShape = shapeOverride ?: outlinedShape,
                // An outlined field is closed by its own border, so the bar is left unfilled rather
                // than boxed a second time under it.
                formattingBarShape = RectangleShape,
                formattingBarColor = Color.Transparent,
            )

            GeneralTextFieldStyle.Filled -> GeneralTextFieldSkin(
                fieldShape = shapeOverride ?: filledShape,
                // The filled field rounds its top corners only, so the bar rounds the bottom ones
                // and the two close the same box.
                formattingBarShape = RoundedCornerShape(
                    bottomStart = SizeConstants.ExtraSmallSize,
                    bottomEnd = SizeConstants.ExtraSmallSize,
                ),
                formattingBarColor = filledBarColor,
            )
        }
    }
}

/** The indicator line is dropped for a grouped field: it would cut the block into strips. */
@Composable
private fun generalTextFieldColors(style: GeneralTextFieldStyle): TextFieldColors = when (style) {
    GeneralTextFieldStyle.Outlined -> OutlinedTextFieldDefaults.colors()
    GeneralTextFieldStyle.Filled -> TextFieldDefaults.colors()
    GeneralTextFieldStyle.Grouped -> TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    )
}

/** Markdown owns the drawing of the source it highlights, so it replaces the caller's transformation. */
@Composable
private fun rememberFieldTransformation(
    markdown: GeneralTextFieldMarkdown,
    visualTransformation: VisualTransformation,
): VisualTransformation {
    if (markdown == GeneralTextFieldMarkdown.None) return visualTransformation
    return rememberMarkdownVisualTransformation()
}

/** Logs [ga4Event] the moment the field takes focus, and not again until focus comes back. */
@Composable
private fun Modifier.logFocusGain(
    firebaseController: FirebaseController?,
    ga4Event: Ga4EventData?,
): Modifier {
    var focused: Boolean by remember { mutableStateOf(value = false) }
    return onFocusChanged { state ->
        if (state.isFocused && !focused) firebaseController.logGa4Event(ga4Event = ga4Event)
        focused = state.isFocused
    }
}

/**
 * The optional Material slots, built from the plain values a caller passes.
 *
 * They are resolved together so both overloads describe a field the same way, and so a null value
 * means the slot is absent rather than empty.
 */
private class GeneralTextFieldSlots(
    val label: String?,
    val placeholder: String?,
    val supportingText: String?,
    val leadingIcon: ToolkitIcon?,
    val leadingIconContentDescription: String?,
    val trailingIcon: ToolkitIcon?,
    val trailingIconContentDescription: String?,
    val onTrailingIconClick: (() -> Unit)?,
) {

    fun label(): @Composable (() -> Unit)? {
        val text: String = label ?: return null
        return { Text(text = text) }
    }

    fun placeholder(): @Composable (() -> Unit)? {
        val text: String = placeholder ?: return null
        return { Text(text = text) }
    }

    fun supportingText(): @Composable (() -> Unit)? {
        val text: String = supportingText ?: return null
        return { Text(text = text) }
    }

    fun leadingIcon(): @Composable (() -> Unit)? {
        val icon: ToolkitIcon = leadingIcon ?: return null
        return {
            ToolkitIconContent(
                icon = icon,
                contentDescription = leadingIconContentDescription,
            )
        }
    }

    fun trailingIcon(): @Composable (() -> Unit)? {
        val icon: ToolkitIcon = trailingIcon ?: return null
        val onClick: (() -> Unit)? = onTrailingIconClick
        if (onClick == null) {
            return {
                ToolkitIconContent(
                    icon = icon,
                    contentDescription = trailingIconContentDescription,
                )
            }
        }
        return {
            GeneralButton(
                onClick = onClick,
                style = GeneralButtonStyle.Text,
                icon = icon,
                contentDescription = trailingIconContentDescription,
                iconSize = SizeConstants.ButtonIconSize,
            )
        }
    }
}
