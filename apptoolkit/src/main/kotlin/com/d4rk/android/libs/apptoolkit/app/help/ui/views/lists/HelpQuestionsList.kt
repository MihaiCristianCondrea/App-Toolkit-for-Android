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
import androidx.compose.ui.draw.clip
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem
import com.d4rk.android.libs.apptoolkit.app.help.ui.views.cards.QuestionCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HelpQuestionsList(questions: ImmutableList<FaqItem>) {
    val expandedStates: SnapshotStateMap<FaqId, Boolean> = remember { mutableStateMapOf() }
    val cardShape = RoundedCornerShape(
        topStart = SizeConstants.LargeIncreasedSize,
        topEnd = SizeConstants.LargeIncreasedSize,
        bottomStart = SizeConstants.ExtraSmallSize,
        bottomEnd = SizeConstants.ExtraSmallSize,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape),
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
                            expandedStates[question.id] = !isExpanded
                        },
                        modifier = Modifier.animateVisibility(index = index)
                    )
                }
            }
        }
    }
}
