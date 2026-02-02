package com.d4rk.android.libs.apptoolkit.app.display.ui.views.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.ui.effects.collectDataStoreState
import com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onCompletion
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.min

private const val SELECT_STARTUP_LOG_TAG =
    "SelectStartupDialog" // TODO: Move to log tags file in library

/**
 * A composable function that displays an [androidx.compose.material3.AlertDialog] allowing the user
 * to select the startup screen for the application.
 *
 * This dialog fetches available screen names and their corresponding route values via Koin
 * dependency injection, manages the state of the current selection within the [CommonDataStore],
 * and persists changes automatically when a new option is selected.
 *
 * @param onDismiss A callback invoked when the dialog is dismissed or the "Done" button is clicked.
 * @param onStartupSelected A callback invoked when a startup screen is selected, providing
 * the selected route string as a parameter.
 */
@Composable
fun SelectStartupScreenAlertDialog(
    onDismiss: () -> Unit,
    onStartupSelected: (String) -> Unit
) {
    val dataStore: CommonDataStore = rememberCommonDataStore()

    val entriesRaw: List<String> = koinInject(qualifier = named("startup_entries"))
    val valuesRaw: List<String> = koinInject(qualifier = named("startup_values"))

    val entries: ImmutableList<String> = remember(entriesRaw) { entriesRaw.toImmutableList() }
    val values: ImmutableList<String> = remember(valuesRaw) { valuesRaw.toImmutableList() }

    val defaultRoute: String = values.firstOrNull().orEmpty()
    val startupRouteState = dataStore
        .getStartupPage(default = defaultRoute)
        .collectDataStoreState(
            initial = { defaultRoute },
            logTag = SELECT_STARTUP_LOG_TAG,
            onErrorReset = { mutableState -> mutableState.value = defaultRoute },
        )
    val startupRoute by startupRouteState

    val selectedPageState = rememberSaveable { mutableStateOf(defaultRoute) }
    var selectedPage by selectedPageState

    LaunchedEffect(startupRoute) {
        selectedPage = startupRoute
    }

    val latestStartupRoute by rememberUpdatedState(startupRoute)
    LaunchedEffect(dataStore) {
        snapshotFlow { selectedPage }
            .distinctUntilChanged()
            .drop(1)
            .onCompletion { cause: Throwable? ->
                if (cause != null && cause !is CancellationException) {
                    selectedPage = latestStartupRoute
                }
            }
            .collectLatest { route: String ->
                if (route.isNotBlank() && route != latestStartupRoute) {
                    dataStore.saveStartupPage(route)
                }
            }
    }

    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onStartupSelected(selectedPage)
            onDismiss()
        },
        icon = Icons.Outlined.Home,
        showDismissButton = false,
        confirmButtonText = stringResource(id = R.string.done_button_content_description),
        title = stringResource(id = R.string.startup_page),
        content = {
            SelectStartupScreenAlertDialogContent(
                selectedPage = selectedPage,
                onSelectedPageChange = { selected -> selectedPage = selected },
                startupEntries = entries,
                startupValues = values,
            )
        }
    )
}

@Composable
fun SelectStartupScreenAlertDialogContent(
    selectedPage: String,
    onSelectedPageChange: (String) -> Unit,
    startupEntries: ImmutableList<String>,
    startupValues: ImmutableList<String>,
) {
    val count = min(startupEntries.size, startupValues.size)

    Column {
        Text(text = stringResource(id = R.string.dialog_startup_subtitle))
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(count) { index ->
                    val currentValue = startupValues[index]
                    RadioButtonPreferenceItem(
                        modifier = Modifier.clip(shape = CircleShape),
                        text = startupEntries[index],
                        isChecked = selectedPage == currentValue,
                        onCheckedChange = { onSelectedPageChange(currentValue) }
                    )
                }
            }
        }
        MediumVerticalSpacer()
        InfoMessageSection(message = stringResource(id = R.string.dialog_info_startup))
    }
}
