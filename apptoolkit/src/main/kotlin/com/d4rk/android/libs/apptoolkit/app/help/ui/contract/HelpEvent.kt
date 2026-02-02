package com.d4rk.android.libs.apptoolkit.app.help.ui.contract

import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface HelpEvent : UiEvent {
    data object LoadFaq : HelpEvent
    data object DismissSnackbar : HelpEvent
    data class RequestReview(val host: ReviewHost) : HelpEvent
}
