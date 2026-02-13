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

package com.d4rk.android.libs.apptoolkit.app.main.ui.views.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.coroutines.dispatchers.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.ui.views.dialogs.BasicAlertDialog
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.extractChangesForVersion
import dev.jeziellago.compose.markdowntext.MarkdownText
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

@Composable
fun ChangelogDialog(
    changelogUrl: String,
    onDismiss: () -> Unit,
) {
    val dispatchers: DispatcherProvider = koinInject()
    val buildInfoProvider: BuildInfoProvider = koinInject()
    val noNewUpdatesText = stringResource(id = R.string.no_new_updates_message)
    val changelogText: MutableState<String?> = remember {
        mutableStateOf(null)
    }
    val isError = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val httpClient: HttpClient = koinInject()

    suspend fun loadChangelog() {
        withContext(dispatchers.io) {
            runCatching {
                val content: String = httpClient.get(changelogUrl).body()
                val section = content.extractChangesForVersion(buildInfoProvider.appVersion)
                changelogText.value = section.ifBlank { noNewUpdatesText }
            }.onFailure {
                isError.value = true
            }
        }
    }

    LaunchedEffect(Unit) {
        loadChangelog()
    }

    BasicAlertDialog(
        onDismiss = onDismiss,
        onConfirm = {
            if (isError.value) {
                isError.value = false
                changelogText.value = null
                scope.launch { loadChangelog() }
            } else {
                onDismiss()
            }
        },
        icon = Icons.Outlined.NewReleases,
        onCancel = onDismiss,
        showDismissButton = false,
        confirmButtonText = if (isError.value) stringResource(id = R.string.try_again) else stringResource(
            id = R.string.done_button_content_description
        ),
        title = stringResource(id = R.string.changelog_title),
        content = {
            val currentChangelogText = changelogText.value
            val currentIsError = isError.value

            when {
                currentChangelogText == null && !currentIsError -> Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator()
                    LargeHorizontalSpacer()
                    Text(text = stringResource(id = R.string.loading_changelog_message))
                }

                currentIsError -> Column(verticalArrangement = Arrangement.Center) {
                    Text(text = stringResource(id = R.string.error_loading_changelog_message))
                }

                else -> currentChangelogText?.let { markdownContent ->
                    MarkdownText(
                        modifier = Modifier.fillMaxWidth(), markdown = markdownContent
                    )
                }
            }
        })
}
