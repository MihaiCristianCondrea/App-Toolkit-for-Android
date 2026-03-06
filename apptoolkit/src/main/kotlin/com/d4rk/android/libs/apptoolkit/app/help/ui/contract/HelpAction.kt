/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.libs.apptoolkit.app.help.ui.contract

import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewOutcome
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

sealed interface HelpAction : ActionEvent {
    data class OpenUrl(val url: String) : HelpAction
    data object OpenPlayStoreReview : HelpAction
    data class ReviewOutcomeReported(val outcome: ReviewOutcome) : HelpAction
}
