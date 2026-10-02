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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.TrackedStatus

/**
 * Where finishing onboarding stands, reported as the screen's `screen_state`: `success` while the
 * pages show, `loading` while completion is saved, and `error` once a save has failed.
 */
@Immutable
sealed interface OnboardingCompletion : TrackedStatus {

    /** The pages are showing and finishing has not been asked for. */
    data object Pending : OnboardingCompletion {
        override val trackingLabel: String get() = "success"
    }

    /** Completion is being saved. */
    data object Saving : OnboardingCompletion {
        override val trackingLabel: String get() = "loading"
    }

    /** The last save failed and the pages stay. Finishing again retries it. */
    data object Failed : OnboardingCompletion {
        override val trackingLabel: String get() = "error"
    }

    /** Completion is saved, so the screen enters the shell. */
    data object Saved : OnboardingCompletion {
        override val trackingLabel: String get() = "success"
    }
}
