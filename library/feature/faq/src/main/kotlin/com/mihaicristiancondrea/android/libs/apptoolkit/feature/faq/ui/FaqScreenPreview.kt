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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun FaqScreenContentPreview() {
    MaterialTheme {
        FaqScreenContent(
            state = FaqUiState(
                questions = Loadable.Ready(
                    persistentListOf(
                        FaqItem(
                            id = FaqId("preview-1"),
                            question = "How do I change the theme?",
                            answer = "Open Settings, then Theme, and pick a palette.",
                        ),
                        FaqItem(
                            id = FaqId("preview-2"),
                            question = "Does the app work offline?",
                            answer = "Yes. Everything but the remote questions works offline.",
                        ),
                    )
                ),
            ),
            showFeedbackSheet = false,
            onFeedbackSheetDismissed = {},
            onRetry = {},
            onRequestFeature = {},
            onLeaveReview = {},
            onQuestionToggled = { _, _, _ -> },
            onShowMoreQuestions = {},
            onContactUs = {},
        )
    }
}
