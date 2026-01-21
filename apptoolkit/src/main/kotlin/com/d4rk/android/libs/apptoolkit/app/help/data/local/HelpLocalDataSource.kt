package com.d4rk.android.libs.apptoolkit.app.help.data.local

import android.content.Context
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqId
import com.d4rk.android.libs.apptoolkit.app.help.domain.model.FaqItem

/**
 * Local data source responsible for retrieving FAQ items from the application's resources.
 *
 * This class maps predefined string resources to [FaqItem] domain models, providing
 * a static list of questions and answers available offline.
 *
 * @property context The [Context] used to resolve string resources.
 */
class HelpLocalDataSource(private val context: Context) {
    fun loadLocalQuestions(): List<FaqItem> {
        val faq = listOf(
            R.string.question_1 to R.string.summary_preference_faq_1,
            R.string.question_2 to R.string.summary_preference_faq_2,
            R.string.question_3 to R.string.summary_preference_faq_3,
            R.string.question_4 to R.string.summary_preference_faq_4,
            R.string.question_5 to R.string.summary_preference_faq_5,
            R.string.question_6 to R.string.summary_preference_faq_6,
            R.string.question_7 to R.string.summary_preference_faq_7,
            R.string.question_8 to R.string.summary_preference_faq_8,
            R.string.question_9 to R.string.summary_preference_faq_9
        ).map { (questionRes, answerRes) ->
            FaqItem(
                id = FaqId("local:$questionRes"),
                question = context.getString(questionRes),
                answer = context.getString(answerRes),
            )
        }
        return faq
    }
}
