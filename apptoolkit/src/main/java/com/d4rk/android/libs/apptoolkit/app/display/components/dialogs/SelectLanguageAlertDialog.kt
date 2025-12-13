package com.d4rk.android.libs.apptoolkit.app.display.components.dialogs

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs.BasicAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.effects.collectWithLifecycleOnCompletion
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onCompletion

@Composable
fun SelectLanguageAlertDialog(onDismiss : () -> Unit , onLanguageSelected : (String) -> Unit) {
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val selectedLanguage = remember { mutableStateOf(value = "") }
    val languageEntries : List<String> = stringArrayResource(id = R.array.preference_language_entries).toList()
    val languageValues : List<String> = stringArrayResource(id = R.array.preference_language_values).toList()

    val currentLanguage by dataStore.getLanguage().collectWithLifecycleOnCompletion(initialValue = "") { cause : Throwable? ->
        if (cause != null && cause !is CancellationException) {
            selectedLanguage.value = ""
        }
    }

    LaunchedEffect(currentLanguage) {
        selectedLanguage.value = currentLanguage
    }

    val latestLanguage by rememberUpdatedState(newValue = currentLanguage)

    LaunchedEffect(Unit) {
        snapshotFlow { selectedLanguage.value }
            .distinctUntilChanged()
            .drop(count = 1)
            .onCompletion { cause : Throwable? ->
                if (cause != null && cause !is CancellationException) {
                    selectedLanguage.value = latestLanguage
                }
            }
            .collectLatest { language : String ->
                if (language.isNotBlank()) {
                    dataStore.saveLanguage(language = language)
                }
            }
    }

    BasicAlertDialog(onDismiss = onDismiss , onConfirm = {
        onLanguageSelected(selectedLanguage.value)
        onDismiss()
    } , onCancel = {
        onDismiss()
    } , icon = Icons.Outlined.Language , title = stringResource(id = R.string.select_language_title) , content = {
        SelectLanguageAlertDialogContent(
            selectedLanguage = selectedLanguage , languageEntries = languageEntries , languageValues = languageValues
        )
    })
}

@Composable
fun SelectLanguageAlertDialogContent(
    selectedLanguage: MutableState<String>,
    languageEntries: List<String>,
    languageValues: List<String>
) { // FIXME: Parameter 'languageEntries' has runtime-determined stability && Parameter 'languageValues' has runtime-determined stability

    Column {
        Text(text = stringResource(id = R.string.dialog_language_subtitle))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        ) {
            LazyColumn {
                items(count = languageEntries.size) { index : Int ->
                    Row(Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Start) {
                        RadioButtonPreferenceItem(
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