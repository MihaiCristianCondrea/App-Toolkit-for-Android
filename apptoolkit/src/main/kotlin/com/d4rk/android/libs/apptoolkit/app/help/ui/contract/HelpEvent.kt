/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.libs.apptoolkit.app.help.ui.contract

import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

sealed interface HelpEvent : UiEvent {
    data object LoadFaq : HelpEvent
    data object DismissSnackbar : HelpEvent
    data object OpenFeatureRequestForm : HelpEvent
    data object OpenContactPage : HelpEvent
    data class RequestReview(val host: ReviewHost) : HelpEvent
}
