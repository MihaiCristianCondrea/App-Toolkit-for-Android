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

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** The page without its ViewModel: no ad, so nothing here needs Koin. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class FaqScreenContentTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    private val questions = (1..7).map { FaqItem(id = FaqId("q$it"), question = "Question $it", answer = "Answer $it") }
    private val calls = mutableListOf<String>()
    private var showFeedbackSheet by mutableStateOf(false)

    private fun string(id: Int): String = compose.activity.getString(id)

    private fun setContent() {
        compose.setContent {
            MaterialTheme {
                FaqScreenContent(
                    state = FaqUiState(questions = Loadable.Ready(questions.toImmutableList())),
                    showFeedbackSheet = showFeedbackSheet,
                    onFeedbackSheetDismissed = {
                        calls += "dismissed"
                        showFeedbackSheet = false
                    },
                    onRetry = {},
                    onRequestFeature = { calls += "feature" },
                    onLeaveReview = { calls += "review" },
                    onQuestionToggled = { question, position, expanded -> calls += "${question.id.value}@$position=$expanded" },
                    onShowMoreQuestions = { calls += "more" },
                    onContactUs = {},
                )
            }
        }
    }

    private fun scrollTo(text: String) {
        compose.onNode(hasScrollAction()).performScrollToNode(hasText(text))
    }

    @Test
    fun `show more reveals the rest of the questions once and then leaves`() {
        setContent()
        scrollTo("Question 5")
        compose.onNodeWithText("Question 6").assertDoesNotExist()

        scrollTo(string(R.string.show_more))
        compose.onNodeWithText(string(R.string.show_more)).performClick()
        compose.waitForIdle()

        scrollTo("Question 7")
        compose.onNodeWithText("Question 7").assertExists()
        compose.onNodeWithText(string(R.string.show_more)).assertDoesNotExist()
        assertEquals(listOf("more"), calls)
    }

    @Test
    fun `a question reports each toggle with its position`() {
        setContent()
        compose.onNodeWithText("Question 2").performClick()
        compose.onNodeWithText("Question 2").performClick()

        assertEquals(listOf("q2@1=true", "q2@1=false"), calls)
    }

    @Test
    fun `a row of the feedback sheet closes it before running its choice`() {
        setContent()
        compose.onNodeWithText(string(R.string.help_feedback_sheet_title)).assertDoesNotExist()

        showFeedbackSheet = true
        compose.onNodeWithText(string(R.string.help_feedback_sheet_feature_request_title)).performClick()
        compose.waitForIdle()

        assertEquals(listOf("dismissed", "feature"), calls)
        compose.onNodeWithText(string(R.string.help_feedback_sheet_title)).assertDoesNotExist()
    }
}
