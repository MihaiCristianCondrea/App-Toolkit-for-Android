package com.d4rk.android.libs.apptoolkit.app.display.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun SelectStartupScreenAlertDialog(onDismiss: () -> Unit, onStartupSelected: (String) -> Unit) {
    val context = LocalContext.current
    val dataStore = CommonDataStore.getInstance(context)
    val selectedPage = remember { mutableStateOf("") }
    val entries: List<String> = koinInject(qualifier = named("startup_entries"))
    val values: List<String> = koinInject(qualifier = named("startup_values"))
    val startupRoute by dataStore.getStartupPage(default = values.first()).collectWithLifecycleOnCompletion(initialValue = values.first()) { cause : Throwable? ->
        if (cause != null && cause !is CancellationException) {
            selectedPage.value = values.first()
        }
    }

    LaunchedEffect(startupRoute) {
        selectedPage.value = startupRoute
    }

    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onStartupSelected(selectedPage.value)
            onDismiss()
        },
        icon = Icons.Outlined.Home,
        showDismissButton = false,
        confirmButtonText = stringResource(id = R.string.done_button_content_description),
        title = stringResource(id = R.string.startup_page),
        content = {
            SelectStartupScreenAlertDialogContent(selectedPage, dataStore, entries, values, startupRoute)
        }
    )
}

@Composable
fun SelectStartupScreenAlertDialogContent(
    selectedPage: MutableState<String>,
    dataStore: CommonDataStore, // FIXME: Unstable parameter 'dataStore' prevents composable from being skippable
    startupEntries: List<String>, // FIXME: Parameter 'startupEntries' has runtime-determined stability
    startupValues: List<String>, // FIXME: Parameter 'startupValues' has runtime-determined stability
    startupRoute: String
) {

    Column {
        Text(text = stringResource(id = R.string.dialog_startup_subtitle))
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(count = startupEntries.size) { index ->
                    val currentValue = startupValues[index]
                    RadioButtonPreferenceItem(
                        text = startupEntries[index],
                        isChecked = selectedPage.value == currentValue,
                        onCheckedChange = {
                            selectedPage.value = currentValue
                        }
                    )
                }
            }
        }
        MediumVerticalSpacer()
        InfoMessageSection(message = stringResource(id = R.string.dialog_info_startup))
    }

    val latestStartupRoute by rememberUpdatedState(newValue = startupRoute)

    LaunchedEffect(Unit) {
        snapshotFlow { selectedPage.value }
            .distinctUntilChanged()
            .drop(count = 1)
            .onCompletion { cause : Throwable? ->
                if (cause != null && cause !is CancellationException) {
                    selectedPage.value = latestStartupRoute
                }
            }
            .collectLatest { route : String ->
                if (route.isNotBlank() && route != latestStartupRoute) {
                    dataStore.saveStartupPage(route)
                }
            }
    }
}