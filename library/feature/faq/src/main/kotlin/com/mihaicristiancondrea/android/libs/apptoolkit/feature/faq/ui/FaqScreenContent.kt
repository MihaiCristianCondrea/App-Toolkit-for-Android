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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.models.fab.ToolkitFab
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.ScreenStateHandler
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.ScaffoldFabs
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqId
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.data.models.FaqItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.states.FaqUiState
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.ads.FaqNativeAdCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.cards.ContactUsCard
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.cards.QuestionCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val INITIAL_VISIBLE_QUESTION_COUNT = 5

/**
 * Renders the help page for [state]: the questions, the contact card, the feedback button and its
 * sheet. Reports every tap through a callback and does nothing else with it.
 *
 * This is the stateless half of [FaqScreen]: it holds no ViewModel, injects nothing, logs nothing
 * and opens nothing, so it renders in a preview or a test with plain values. The state it keeps is
 * visual: which questions are expanded, whether all of them show, and whether the sheet is open.
 *
 * @param onRetry Loads the questions again after a failure or an empty result.
 * @param onFeedbackOpened The feedback button opened the sheet.
 * @param onRequestFeature The sheet's feature request row was tapped; the sheet closes itself.
 * @param onLeaveReview The sheet's review row was tapped; the sheet closes itself.
 * @param onQuestionToggled A question was expanded or collapsed, with its position in the list.
 * @param onShowMoreQuestions The rest of the questions were revealed.
 * @param onContactUs The contact card was tapped.
 * @param contentPadding Padding from the shell, applied inside the list and the state screens.
 * @param adUnitId The native ad slot's unit, or `null` when no ad should show.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FaqScreenContent(
    state: FaqUiState,
    onRetry: () -> Unit,
    onFeedbackOpened: () -> Unit,
    onRequestFeature: () -> Unit,
    onLeaveReview: () -> Unit,
    onQuestionToggled: (question: FaqItem, position: Int, expanded: Boolean) -> Unit,
    onShowMoreQuestions: () -> Unit,
    onContactUs: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    adUnitId: String? = null,
) {
    var showFeedbackSheet by rememberSaveable { mutableStateOf(value = false) }
    val feedbackSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)

    // The page frame draws it at the bottom end, above the system bar.
    ScaffoldFabs(
        listOf(
            ToolkitFab(
                icon = ToolkitIcon.Vector(Icons.Outlined.RateReview),
                label = stringResource(id = R.string.feedback),
                onClick = {
                    onFeedbackOpened()
                    showFeedbackSheet = true
                },
            ),
        ),
    )

    ScreenStateHandler(
        state = state.questions,
        modifier = modifier,
        contentPadding = contentPadding,
        onRetry = onRetry,
        // Nothing to show is still worth another try: the catalog may have been updated.
        onEmpty = { empty ->
            NoDataScreen(
                message = empty.message?.asString(),
                showRetry = true,
                onRetry = onRetry,
                paddingValues = contentPadding,
            )
        },
    ) { ready ->
        FaqList(
            questions = ready.value,
            contentPadding = contentPadding,
            adUnitId = adUnitId,
            onQuestionToggled = onQuestionToggled,
            onShowMoreQuestions = onShowMoreQuestions,
            onContactUs = onContactUs,
        )
    }

    if (showFeedbackSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFeedbackSheet = false },
            sheetState = feedbackSheetState,
        ) {
            Text(
                text = stringResource(id = R.string.help_feedback_sheet_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = SizeConstants.ExtraLargeCompactSize),
            )
            Spacer(modifier = Modifier.height(SizeConstants.SmallSize))

            GroupedActionList(
                modifier = Modifier.padding(horizontal = SizeConstants.MediumSize),
                actions = persistentListOf(
                    GroupedAction(
                        title = stringResource(id = R.string.help_feedback_sheet_feature_request_title),
                        description = stringResource(id = R.string.help_feedback_sheet_feature_request_description),
                        icon = Icons.Outlined.Lightbulb,
                        onClick = {
                            showFeedbackSheet = false
                            onRequestFeature()
                        },
                    ),
                    GroupedAction(
                        title = stringResource(id = R.string.help_feedback_sheet_review_title),
                        description = stringResource(id = R.string.help_feedback_sheet_review_description),
                        icon = Icons.Outlined.RateReview,
                        onClick = {
                            showFeedbackSheet = false
                            onLeaveReview()
                        },
                    ),
                ),
            )

            Spacer(modifier = Modifier.height(SizeConstants.ExtraLargeCompactSize))
        }
    }
}

@Composable
private fun FaqList(
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
        questions.take(INITIAL_VISIBLE_QUESTION_COUNT)
    }
    val hasHiddenQuestions = questions.size > INITIAL_VISIBLE_QUESTION_COUNT
    val popularGroupItemCount = visibleQuestions.size + if (hasHiddenQuestions) 1 else 0
    val supportGroupItemCount = 1 + if (adUnitId != null && isAdLoaded) 1 else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = SizeConstants.LargeSize,
            end = SizeConstants.LargeSize,
        ),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize),
    ) {
        item {
            Text(text = stringResource(id = R.string.popular_help_resources))
        }

        visibleQuestions.forEachIndexed { index: Int, question: FaqItem ->
            item(key = question.id.value) {
                var isExpanded by rememberSaveable(question.id.value) {
                    mutableStateOf(value = false)
                }
                QuestionCard(
                    title = question.question,
                    summary = question.answer,
                    isExpanded = isExpanded,
                    groupedPosition = groupedItemPosition(
                        index = index,
                        size = if (showAllQuestions) questions.size else popularGroupItemCount,
                    ),
                    onToggleExpand = {
                        val expanded = !isExpanded
                        onQuestionToggled(question, index, expanded)
                        isExpanded = expanded
                    },
                    modifier = Modifier
                        .animateItem(
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow,
                            )
                        )
                        .animateVisibility(index = index),
                )
            }
        }

        if (hasHiddenQuestions) {
            item(key = "show_more_questions") {
                AnimatedVisibility(
                    visible = !showAllQuestions,
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 500),
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 300),
                    ) + shrinkVertically(
                        animationSpec = tween(durationMillis = 500),
                    ),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow,
                                )
                            )
                            .groupedCorners(
                                position = groupedItemPosition(
                                    index = popularGroupItemCount - 1,
                                    size = popularGroupItemCount,
                                ),
                                outerRadius = SizeConstants.ExtraLargeIncreasedSize,
                            )
                            .animateVisibility(index = popularGroupItemCount - 1),
                        shape = RectangleShape,
                        onClick = {
                            onShowMoreQuestions()
                            showAllQuestions = true
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = SizeConstants.LargeSize,
                                    vertical = SizeConstants.MediumSize,
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
                                modifier = Modifier.padding(start = SizeConstants.SmallSize),
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(text = stringResource(id = R.string.need_more_help))
        }

        if (adUnitId != null) {
            item {
                FaqNativeAdCard(
                    adUnitId = adUnitId,
                    groupedPosition = groupedItemPosition(
                        index = 0,
                        size = supportGroupItemCount,
                    ),
                    modifier = Modifier.animateItem(),
                    onAdLoaded = { isAdLoaded = it },
                )
            }
        }

        item {
            ContactUsCard(
                groupedPosition = groupedItemPosition(
                    index = supportGroupItemCount - 1,
                    size = supportGroupItemCount,
                ),
                onClick = onContactUs,
            )
            repeat(3) { ExtraLargeVerticalSpacer() }
        }
    }
}

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
            onRetry = {},
            onFeedbackOpened = {},
            onRequestFeature = {},
            onLeaveReview = {},
            onQuestionToggled = { _, _, _ -> },
            onShowMoreQuestions = {},
            onContactUs = {},
        )
    }
}
