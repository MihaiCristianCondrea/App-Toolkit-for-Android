package com.d4rk.android.libs.apptoolkit.app.review.domain.model

/**
 * Result of an in-app review request.
 */
sealed interface ReviewOutcome {
    /** The review flow was launched successfully. */
    data object Launched : ReviewOutcome

    /** The review flow was not launched because eligibility conditions were not met. */
    data object NotEligible : ReviewOutcome

    /** The review flow is unavailable on the current device/install. */
    data object Unavailable : ReviewOutcome

    /** The review flow was attempted but failed to launch. */
    data object Failed : ReviewOutcome
}
