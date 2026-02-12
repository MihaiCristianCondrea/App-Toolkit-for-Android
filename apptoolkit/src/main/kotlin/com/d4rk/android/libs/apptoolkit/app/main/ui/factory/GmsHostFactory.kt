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

package com.d4rk.android.libs.apptoolkit.app.main.ui.factory

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.d4rk.android.libs.apptoolkit.app.consent.domain.model.ConsentHost
import com.d4rk.android.libs.apptoolkit.app.main.domain.model.InAppUpdateHost
import com.d4rk.android.libs.apptoolkit.app.review.domain.model.ReviewHost

/**
 * Builds GMS host abstractions for in-app review and update flows.
 */
class GmsHostFactory {
    fun createConsentHost(activity: Activity): ConsentHost {
        return object : ConsentHost {
            override val activity: Activity = activity
        }
    }

    fun createReviewHost(activity: Activity): ReviewHost {
        return object : ReviewHost {
            override val activity: Activity = activity
        }
    }

    fun createUpdateHost(
        activity: Activity,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
    ): InAppUpdateHost {
        return object : InAppUpdateHost {
            override val activity: Activity = activity
            override val updateResultLauncher: ActivityResultLauncher<IntentSenderRequest> = launcher
        }
    }
}