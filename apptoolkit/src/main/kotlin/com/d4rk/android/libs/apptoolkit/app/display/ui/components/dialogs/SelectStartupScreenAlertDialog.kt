package com.d4rk.android.libs.apptoolkit.app.display.ui.components.dialogs

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.dialogs.BasicAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.sections.InfoMessageSection
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.RadioButtonPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.effects.collectWithLifecycleOnCompletion
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onCompletion
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import kotlin.math.min

@Composable
fun SelectStartupScreenAlertDialog(
    onDismiss: () -> Unit,
    onStartupSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val dataStore = CommonDataStore.getInstance(context)

    val entriesRaw: List<String> = koinInject(qualifier = named("startup_entries"))
    val valuesRaw: List<String> = koinInject(qualifier = named("startup_values"))

    val entries: ImmutableList<String> = remember(entriesRaw) { entriesRaw.toImmutableList() }
    val values: ImmutableList<String> = remember(valuesRaw) { valuesRaw.toImmutableList() }

    val defaultRoute: String = values.firstOrNull().orEmpty()
    val startupRoute by dataStore
        .getStartupPage(default = defaultRoute)
        .collectWithLifecycleOnCompletion(initialValueProvider = { defaultRoute }) { _: Throwable? -> }

    var selectedPage by rememberSaveable { mutableStateOf(defaultRoute) }

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
                onSelectedPageChange = { selectedPage = it },
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
