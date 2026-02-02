package com.d4rk.android.libs.apptoolkit.app.help.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome

sealed interface HelpAction : ActionEvent {
    data class OpenOnlineHelp(val url: String) : HelpAction
    data class ReviewOutcomeReported(val outcome: ReviewOutcome) : HelpAction
}
