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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.apptoolkit.startup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.StartupProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.OnboardingActivity
import javax.inject.Inject

/**
 * Sample startup policy used by the reusable startup feature.
 *
 * Notification permission is requested only where it is runtime-gated, and successful startup
 * always proceeds to the toolkit onboarding activity.
 */
class AppStartupProvider @Inject constructor() : StartupProvider {
    override val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    override fun getNextIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
}
