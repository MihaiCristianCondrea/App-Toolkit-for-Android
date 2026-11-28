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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animatedItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.PreferenceItemDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.constants.FaqConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.ads.FaqNativeAdCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.cards.ContactUsCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.cards.QuestionCard
import kotlinx.collections.immutable.ImmutableList

/**
 * Renders the question list and support rows, retaining each question's expansion state.
 *
 * @param onQuestionToggled Reports the question, its zero-based list position and its new state.
 * @param adUnitId Native ad unit, or `null` to omit the ad row.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun FaqList(
    questions: ImmutableList<FaqItem>,
    contentPadding: PaddingValues,
    adUnitId: String?,
    onQuestionToggled: (question: FaqItem, position: Int, expanded: Boolean) -> Unit,
    onShowMoreQuestions: () -> Unit,
    onContactUs: () -> Unit,
) {
    var showAllQuestions by rememberSaveable { mutableStateOf(value = false) }
    var isAdLoaded by remember { mutableStateOf(value = false) }
    val visibleQuestions = if (showAllQuestions) {
        questions
    } else {
        questions.take(n = FaqConstants.INITIAL_VISIBLE_QUESTION_COUNT)
    }
    val hasMoreToReveal = !showAllQuestions && (questions.size > FaqConstants.INITIAL_VISIBLE_QUESTION_COUNT)
    val popularGroupItemCount = visibleQuestions.size + (if (hasMoreToReveal) 1 else 0)
    val supportGroupItemCount = if ((adUnitId != null) && isAdLoaded) 2 else 1

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = MaterialTheme.spacing.large,
            end = MaterialTheme.spacing.large,
        ),
        verticalArrangement = Arrangement.spacedBy(space = ListItemDefaults.SegmentedGap),
    ) {
        animatedItem {
            Text(text = stringResource(id = R.string.popular_help_resources))
        }

        visibleQuestions.forEachIndexed { index: Int, question: FaqItem ->
            animatedItem(key = question.id.value) {
                var isExpanded by rememberSaveable(question.id.value) {
                    mutableStateOf(value = false)
                }
                QuestionCard(
                    title = question.question,
                    summary = question.answer,
                    isExpanded = isExpanded,
                    shape = PreferenceItemDefaults.segmentedShapes(
                        index = index,
                        count = popularGroupItemCount,
                        outerShape = MaterialTheme.shapes.extraLargeIncreased,
                    ).shape,
                    onToggleExpand = {
                        val expanded = !isExpanded
                        onQuestionToggled(question, index, expanded)
                        isExpanded = expanded
                    },
                )
            }
        }

        if (hasMoreToReveal) {
            animatedItem(key = "show_more_questions") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = PreferenceItemDefaults.segmentedShapes(
                        index = popularGroupItemCount - 1,
                        count = popularGroupItemCount,
                        outerShape = MaterialTheme.shapes.extraLargeIncreased,
                    ).shape,
                    onClick = {
                        onShowMoreQuestions()
                        showAllQuestions = true
                    },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = MaterialTheme.spacing.large,
                                vertical = MaterialTheme.spacing.medium,
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.show_more),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.padding(start = MaterialTheme.spacing.small),
                        )
                    }
                }
            }
        }

        animatedItem {
            Text(text = stringResource(id = R.string.need_more_help))
        }

        if (adUnitId != null) {
            item {
                FaqNativeAdCard(
                    adUnitId = adUnitId,
                    shape = PreferenceItemDefaults.segmentedShapes(
                        index = 0,
                        count = supportGroupItemCount,
                        outerShape = MaterialTheme.shapes.extraLargeIncreased,
                    ).shape,
                    modifier = Modifier.animateItem(),
                ) { loaded ->
                    isAdLoaded = loaded
                }
            }
        }

        animatedItem {
            ContactUsCard(
                shape = PreferenceItemDefaults.segmentedShapes(
                    index = supportGroupItemCount - 1,
                    count = supportGroupItemCount,
                    outerShape = MaterialTheme.shapes.extraLargeIncreased,
                ).shape,
                onClick = onContactUs,
            )
            Spacer(modifier = Modifier.height(height = 84.dp))
        }
    }
}
