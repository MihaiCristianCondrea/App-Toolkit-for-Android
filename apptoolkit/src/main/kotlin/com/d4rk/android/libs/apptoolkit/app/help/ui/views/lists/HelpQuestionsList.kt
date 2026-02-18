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

package com.d4rk.android.libs.apptoolkit.app.help.ui.views.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.cards.QuestionCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HelpQuestionsList(
    questions: ImmutableList<FaqItem>,
    firebaseController: FirebaseController,
    ga4EventProvider: ((FaqItem) -> Ga4EventData)? = null,
) {
    val expandedStates: SnapshotStateMap<FaqId, Boolean> = remember { mutableStateMapOf() }
    val cardShape = RoundedCornerShape(
        topStart = SizeConstants.LargeIncreasedSize,
        topEnd = SizeConstants.LargeIncreasedSize,
        bottomStart = SizeConstants.ExtraSmallSize,
        bottomEnd = SizeConstants.ExtraSmallSize,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = cardShape
    ) {
        Column {
            questions.forEachIndexed { index: Int, question: FaqItem ->
                key(question.id) {
                    val isExpanded = expandedStates[question.id] == true
                    QuestionCard(
                        title = question.question,
                        summary = question.answer,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            firebaseController.logGa4Event(ga4EventProvider?.invoke(question))
                            expandedStates[question.id] = !isExpanded
                        },
                        modifier = Modifier.animateVisibility(index = index)
                    )
                }
            }
        }
    }
}
