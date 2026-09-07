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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.analytics

import androidx.compose.runtime.Stable
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.domain.models.AppInfo
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppActionLauncher
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController

/**
 * Reports the two app-details launches that leave the app, then forwards to [delegate].
 *
 * The app-details sheet is presentation-only and receives a launcher rather than a Firebase
 * controller, so decorating the launcher is what lets "open app" and "view on Play Store" emit the
 * same `app_card_interaction` event the list surfaces already emit. One instance belongs to one
 * selected app, which is how the event gets its package and name without the sheet passing them.
 *
 * Every other launcher action delegates unchanged.
 */
@Stable
class AnalyticsAppActionLauncher(
    private val delegate: AppActionLauncher,
    private val firebaseController: FirebaseController,
    private val appInfo: AppInfo,
    private val source: String,
) : AppActionLauncher by delegate {

    override fun openApp(packageName: String): Boolean {
        firebaseController.logAppInteraction(
            source = source,
            appInfo = appInfo,
            interaction = AppInteractionType.OpenInstalledApp,
        )
        return delegate.openApp(packageName)
    }

    override fun openPlayStore(packageName: String): Boolean {
        firebaseController.logAppInteraction(
            source = source,
            appInfo = appInfo,
            interaction = AppInteractionType.OpenInPlayStore,
        )
        return delegate.openPlayStore(packageName)
    }
}
