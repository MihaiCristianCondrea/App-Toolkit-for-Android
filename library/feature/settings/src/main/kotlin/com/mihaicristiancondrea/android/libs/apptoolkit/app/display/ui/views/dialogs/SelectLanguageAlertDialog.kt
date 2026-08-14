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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.display.ui.views.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


/**
 * A composable that displays an alert dialog for selecting the application language.
 *
 * The caller owns persistence; dismissing the dialog does not save a provisional selection.
 *
 * @param onDismiss Callback invoked when the dialog should be dismissed,
 * either by clicking outside, clicking the cancel button, or after confirming a selection.
 * @param onLanguageSelected Callback invoked when a language is confirmed,
 * providing the selected language string value as a parameter.
 */
@Composable
fun SelectLanguageAlertDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    val selectedLanguage = rememberSaveable(currentLanguage) {
        mutableStateOf(value = currentLanguage)
    }

    val preferenceLanguageEntries = stringArrayResource(id = R.array.preference_language_entries)
    val preferenceLanguageValues = stringArrayResource(id = R.array.preference_language_values)
    val languageEntries: ImmutableList<String> = remember(preferenceLanguageEntries) {
        preferenceLanguageEntries.toList().toImmutableList()
    }
    val languageValues: ImmutableList<String> = remember(preferenceLanguageValues) {
        preferenceLanguageValues.toList().toImmutableList()
    }

    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onLanguageSelected(selectedLanguage.value)
            onDismiss()
        },
        onCancel = {
            onDismiss()
        },
        icon = Icons.Outlined.Language,
        title = stringResource(id = R.string.select_language_title),
        content = {
            SelectLanguageAlertDialogContent(
                selectedLanguage = selectedLanguage,
                languageEntries = languageEntries,
                languageValues = languageValues
            )
        })
}

@Composable
fun SelectLanguageAlertDialogContent(
    selectedLanguage: MutableState<String>,
    languageEntries: ImmutableList<String>,
    languageValues: ImmutableList<String>
) {
    Column(
        modifier = Modifier.fillMaxHeight(fraction = 0.6f)
    ) {
        Text(text = stringResource(id = R.string.dialog_language_subtitle))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize)
            ) {
                items(count = languageEntries.size) { index: Int ->
                    RadioButtonPreferenceItem(
                        modifier = Modifier.groupedPreferenceItem(
                            position = groupedItemPosition(
                                index = index,
                                size = languageEntries.size
                            ),
                            outerRadius = SizeConstants.LargeMediumSize,
                            horizontalPadding = SizeConstants.ZeroSize
                        ),
                        text = languageEntries[index],
                        isChecked = selectedLanguage.value == languageValues[index],
                        onCheckedChange = {
                            selectedLanguage.value = languageValues[index]
                        }
                    )
                }
            }
        }
        MediumVerticalSpacer()
        InfoMessageSection(message = stringResource(id = R.string.dialog_info_language))
    }
}
