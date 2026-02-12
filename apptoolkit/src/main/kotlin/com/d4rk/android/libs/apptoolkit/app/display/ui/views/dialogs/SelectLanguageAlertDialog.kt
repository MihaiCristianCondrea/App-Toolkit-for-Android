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

package com.d4rk.android.libs.apptoolkit.app.display.ui.views.dialogs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.ui.effects.collectDataStoreState
import com.d4rk.android.libs.apptoolkit.core.ui.effects.persistChanges
import com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.SELECT_LANGUAGE_DIALOG_LOG_TAG
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope


/**
 * A composable that displays an alert dialog for selecting the application language.
 *
 * This dialog retrieves the current language preference from the [CommonDataStore],
 * allows the user to pick a new language from a list of available options, and
 * automatically persists the selection.
 *
 * @param onDismiss Callback invoked when the dialog should be dismissed,
 * either by clicking outside, clicking the cancel button, or after confirming a selection.
 * @param onLanguageSelected Callback invoked when a language is confirmed,
 * providing the selected language string value as a parameter.
 */
@Composable
fun SelectLanguageAlertDialog(onDismiss: () -> Unit, onLanguageSelected: (String) -> Unit) {
    val dataStore: CommonDataStore = rememberCommonDataStore()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val selectedLanguage = remember { mutableStateOf(value = "") }

    val preferenceLanguageEntries =
        stringArrayResource(id = R.array.preference_language_entries).toList().toImmutableList()
    val preferenceLanguageValues =
        stringArrayResource(id = R.array.preference_language_values).toList().toImmutableList()
    val languageEntries: ImmutableList<String> = remember {
        preferenceLanguageEntries
    }
    val languageValues: ImmutableList<String> = remember {
        preferenceLanguageValues
    }

    val currentLanguageState = dataStore.getLanguage()
        .collectDataStoreState(
            initial = { "" },
            logTag = SELECT_LANGUAGE_DIALOG_LOG_TAG,
            onErrorReset = { mutableState ->
                mutableState.value = ""
                selectedLanguage.value = ""
            },
        )
    val currentLanguage by currentLanguageState

    LaunchedEffect(currentLanguage) {
        selectedLanguage.value = currentLanguage
    }

    val latestLanguage by rememberUpdatedState(newValue = currentLanguage)

    LaunchedEffect(selectedLanguage, coroutineScope) {
        selectedLanguage.persistChanges(
            scope = coroutineScope,
            currentValue = { latestLanguage },
            onPersist = { language: String ->
                if (language.isNotBlank()) {
                    dataStore.saveLanguage(language = language)
                }
            },
            onError = { throwable, latest ->
                Log.w(
                    SELECT_LANGUAGE_DIALOG_LOG_TAG,
                    "Failed to persist language selection.",
                    throwable
                )
                selectedLanguage.value = latest
            },
        )
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
    Column {
        Text(text = stringResource(id = R.string.dialog_language_subtitle))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        ) {
            LazyColumn {
                items(count = languageEntries.size) { index: Int ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButtonPreferenceItem(
                            modifier = Modifier.clip(shape = CircleShape),
                            text = languageEntries[index],
                            isChecked = selectedLanguage.value == languageValues[index],
                            onCheckedChange = {
                                selectedLanguage.value = languageValues[index]
                            }
                        )
                    }
                }
            }
        }
        MediumVerticalSpacer()
        InfoMessageSection(message = stringResource(id = R.string.dialog_info_language))
    }

}
