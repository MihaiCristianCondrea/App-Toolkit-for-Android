package com.d4rk.android.apps.apptoolkit.app.main.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome

sealed interface MainAction : ActionEvent {
    data class ReviewOutcomeReported(val outcome: ReviewOutcome) : MainAction
}
