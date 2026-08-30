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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di.AppToolkitDiConstants
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.DialogContentSizing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dialogs.dialogContentHeight
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.sections.InfoMessageSection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import kotlin.math.min


/**
 * A composable function that displays an [androidx.compose.material3.AlertDialog] allowing the user
 * to select the startup screen for the application.
 *
 * This dialog fetches available screen names and their corresponding route values via Koin
 * dependency injection. The caller owns persistence of the confirmed selection.
 *
 * @param onDismiss A callback invoked when the dialog is dismissed or the "Done" button is clicked.
 * @param onStartupSelected A callback invoked when a startup screen is selected, providing
 * the selected route string as a parameter.
 */
@Composable
fun SelectStartupScreenAlertDialog(
    currentRoute: String,
    onDismiss: () -> Unit,
    onStartupSelected: (String) -> Unit,
    /**
     * Defaults to wrapping the option list. This dialog usually offers a handful of startup screens
     * and previously reserved 60% of the screen height for them regardless, leaving most of the
     * dialog empty. Pass [DialogContentSizing.FractionOfScreen] to restore the fixed height.
     */
    sizing: DialogContentSizing = DialogContentSizing.WrapContent,
) {
    val entriesRaw: List<String> = koinInject(qualifier = named(AppToolkitDiConstants.STARTUP_ENTRIES))
    val valuesRaw: List<String> = koinInject(qualifier = named(AppToolkitDiConstants.STARTUP_VALUES))

    val entries: ImmutableList<String> = remember(entriesRaw) { entriesRaw.toImmutableList() }
    val values: ImmutableList<String> = remember(valuesRaw) { valuesRaw.toImmutableList() }

    val initialRoute = currentRoute.ifBlank { values.firstOrNull().orEmpty() }
    val selectedPageState = rememberSaveable(initialRoute) { mutableStateOf(initialRoute) }
    var selectedPage by selectedPageState

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
                sizing = sizing,
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
    sizing: DialogContentSizing = DialogContentSizing.WrapContent,
) {
    val count = min(startupEntries.size, startupValues.size)

    Column(
        modifier = Modifier.dialogContentHeight(sizing = sizing)
    ) {
        Text(text = stringResource(id = R.string.dialog_startup_subtitle))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(space = SizeConstants.ExtraTinySize)
            ) {
                items(count) { index ->
                    val currentValue = startupValues[index]
                    RadioButtonPreferenceItem(
                        modifier = Modifier.groupedPreferenceItem(
                            position = groupedItemPosition(
                                index = index,
                                size = count
                            ),
                            outerRadius = SizeConstants.LargeMediumSize,
                            horizontalPadding = SizeConstants.ZeroSize
                        ),
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
