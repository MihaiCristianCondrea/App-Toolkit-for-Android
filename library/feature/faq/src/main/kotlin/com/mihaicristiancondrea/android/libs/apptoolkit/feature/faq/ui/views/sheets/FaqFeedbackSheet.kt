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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.ui.views.sheets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedAction
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.lists.GroupedActionList
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.faq.R
import kotlinx.collections.immutable.persistentListOf

/**
 * The feedback sheet: a feature request or a review. Picking either closes it through [onDismiss]
 * before running the choice.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FaqFeedbackSheet(
    onDismiss: () -> Unit,
    onRequestFeature: () -> Unit,
    onLeaveReview: () -> Unit,
) {
    val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
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
                        onDismiss()
                        onRequestFeature()
                    },
                ),
                GroupedAction(
                    title = stringResource(id = R.string.help_feedback_sheet_review_title),
                    description = stringResource(id = R.string.help_feedback_sheet_review_description),
                    icon = Icons.Outlined.RateReview,
                    onClick = {
                        onDismiss()
                        onLeaveReview()
                    },
                ),
            ),
        )

        Spacer(modifier = Modifier.height(SizeConstants.ExtraLargeCompactSize))
    }
}
