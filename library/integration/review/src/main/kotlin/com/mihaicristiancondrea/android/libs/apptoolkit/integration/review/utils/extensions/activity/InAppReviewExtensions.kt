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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.review.utils.extensions.activity

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.hasPlayStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isInstalledFromPlayStore
import kotlinx.coroutines.tasks.await

/**
 * Checks whether the Play Store in-app review flow can be safely requested.
 *
 * The helper ensures the app originates from the Play Store and that a lightweight review
 * request succeeds before reporting availability.
 */
suspend fun Activity.isInAppReviewAvailable(): Boolean {
    val context = applicationContext

    if (!context.hasPlayStore()) return false
    if (!context.isInstalledFromPlayStore()) return false

    val manager = ReviewManagerFactory.create(context)
    return runCatching {
        manager.requestReviewFlow().await()
        true
    }.getOrDefault(false)
}
