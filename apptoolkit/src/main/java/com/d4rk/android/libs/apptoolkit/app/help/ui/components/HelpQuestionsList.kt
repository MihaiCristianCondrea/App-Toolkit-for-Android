package com.d4rk.android.libs.apptoolkit.app.help.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.UiHelpQuestion
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.animateVisibility

@Composable
fun HelpQuestionsList(questions : List<UiHelpQuestion>) {
    val expandedStates : SnapshotStateMap<Int , Boolean> = remember { mutableStateMapOf() }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            questions.forEachIndexed { index : Int , question : UiHelpQuestion ->
                key(question.id) {
                    val isExpanded = expandedStates[question.id] == true
                    QuestionCard(title = question.question , summary = question.answer , isExpanded = isExpanded , onToggleExpand = {
                        expandedStates[question.id] = ! isExpanded
                    } , modifier = Modifier.animateVisibility(index = index))
                }
            }
        }
    }
}