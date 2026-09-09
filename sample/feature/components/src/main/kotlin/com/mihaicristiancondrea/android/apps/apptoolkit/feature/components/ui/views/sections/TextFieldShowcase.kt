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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseHeader
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSection
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.components.ui.views.ShowcaseSurface
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextField
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldMarkdown
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.fields.GeneralTextFieldStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer

/** Rows the Markdown preview grows to before it scrolls its own content. */
private const val MARKDOWN_MIN_LINES: Int = 4
private const val MARKDOWN_MAX_LINES: Int = 10

/**
 * Every `GeneralTextField` variant, one per card: the three styles, the error state, and the
 * Markdown editor.
 *
 * The text these fields hold is scratch state of the showcase itself, so it stays here rather than
 * in the screen: nothing else reads it, and a preview that forgets what was typed in it on rotation
 * would look broken.
 */
@Composable
fun TextFieldShowcase(
    firebaseController: FirebaseController,
    onLogEvent: (String, String?) -> Ga4EventData,
) {
    var title: String by rememberSaveable { mutableStateOf(value = "") }
    var email: String by rememberSaveable { mutableStateOf(value = "") }
    var name: String by rememberSaveable { mutableStateOf(value = "") }
    var note: String by rememberSaveable { mutableStateOf(value = "") }
    var markdown: String by rememberSaveable { mutableStateOf(value = "") }

    ShowcaseHeader(
        title = stringResource(id = R.string.components_section_text_fields),
        icon = Icons.Outlined.TextFields,
    )
    ShowcaseSection {
        ShowcaseSurface(position = GroupedItemPosition.FIRST) {
            CardTitle(text = stringResource(id = R.string.components_text_field_filled))
            MediumVerticalSpacer()
            GeneralTextField(
                value = title,
                onValueChange = { title = it },
                label = stringResource(id = R.string.components_text_field_title_label),
                placeholder = stringResource(id = R.string.components_text_field_title_placeholder),
                supportingText = stringResource(id = R.string.components_text_field_support),
                leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Title),
                leadingIconContentDescription = stringResource(
                    id = R.string.components_text_field_title_label,
                ),
                trailingIcon = if (title.isEmpty()) {
                    null
                } else {
                    ToolkitIcon.Vector(imageVector = Icons.Rounded.Close)
                },
                trailingIconContentDescription = stringResource(
                    id = R.string.components_text_field_clear,
                ),
                onTrailingIconClick = { title = "" },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                firebaseController = firebaseController,
                ga4Event = onLogEvent("text_field", "filled"),
            )
        }

        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            CardTitle(text = stringResource(id = R.string.components_text_field_outlined))
            MediumVerticalSpacer()
            // The error state carries its message, so the two cannot drift apart.
            val invalidEmail: Boolean = email.isNotBlank() && !email.contains(char = '@')
            GeneralTextField(
                value = email,
                onValueChange = { email = it },
                style = GeneralTextFieldStyle.Outlined,
                label = stringResource(id = R.string.components_text_field_email_label),
                placeholder = stringResource(
                    id = R.string.components_text_field_email_placeholder,
                ),
                errorText = if (invalidEmail) {
                    stringResource(id = R.string.components_text_field_email_error)
                } else {
                    null
                },
                leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Email),
                leadingIconContentDescription = stringResource(
                    id = R.string.components_text_field_email_label,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                firebaseController = firebaseController,
                ga4Event = onLogEvent("text_field", "outlined"),
            )
        }

        ShowcaseSurface(position = GroupedItemPosition.MIDDLE) {
            CardTitle(text = stringResource(id = R.string.components_text_field_grouped))
            MediumVerticalSpacer()
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize),
            ) {
                GeneralTextField(
                    value = name,
                    onValueChange = { name = it },
                    style = GeneralTextFieldStyle.Grouped,
                    position = GroupedItemPosition.FIRST,
                    placeholder = stringResource(
                        id = R.string.components_text_field_name_placeholder,
                    ),
                    leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Person),
                    leadingIconContentDescription = stringResource(
                        id = R.string.components_text_field_name_placeholder,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                    firebaseController = firebaseController,
                    ga4Event = onLogEvent("text_field", "grouped"),
                )
                GeneralTextField(
                    value = note,
                    onValueChange = { note = it },
                    style = GeneralTextFieldStyle.Grouped,
                    position = GroupedItemPosition.LAST,
                    placeholder = stringResource(
                        id = R.string.components_text_field_note_placeholder,
                    ),
                    leadingIcon = ToolkitIcon.Vector(imageVector = Icons.AutoMirrored.Outlined.Notes),
                    leadingIconContentDescription = stringResource(
                        id = R.string.components_text_field_note_placeholder,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done,
                    ),
                )
            }
        }

        ShowcaseSurface(position = GroupedItemPosition.LAST) {
            CardTitle(text = stringResource(id = R.string.components_text_field_markdown))
            MediumVerticalSpacer()
            GeneralTextField(
                value = markdown,
                onValueChange = { markdown = it },
                style = GeneralTextFieldStyle.Grouped,
                placeholder = stringResource(
                    id = R.string.components_text_field_markdown_placeholder,
                ),
                leadingIcon = ToolkitIcon.Vector(imageVector = Icons.Outlined.Description),
                leadingIconContentDescription = stringResource(
                    id = R.string.components_text_field_markdown,
                ),
                minLines = MARKDOWN_MIN_LINES,
                maxLines = MARKDOWN_MAX_LINES,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default,
                ),
                markdown = GeneralTextFieldMarkdown.Editor,
                firebaseController = firebaseController,
                ga4Event = onLogEvent("text_field", "markdown"),
            )
        }
    }
}

@Composable
private fun CardTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}
