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

package com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.ui.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.mihaicristiancondrea.android.apps.apptoolkit.app.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.app.about.ui.AboutScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/** The About surface for this app: the toolkit screen plus the hidden version-tap unlock. */
@Composable
fun AboutSettingsContent(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val showcaseUnlockRepository: ShowcaseUnlockRepository = koinInject()
    val firebaseController: FirebaseController = koinInject()
    val coroutineScope = rememberCoroutineScope()

    AboutScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        onVersionTap = { tapCount ->
            coroutineScope.launch {
                runCatching {
                    showcaseUnlockRepository.unlockAfterVersionTaps(tapCount = tapCount)
                }.onFailure { throwable ->
                    firebaseController.recordNonFatal(
                        throwable = throwable,
                        attributes = mapOf("operation" to "unlock_components_showcase"),
                    )
                }
            }
        },
    )
}
