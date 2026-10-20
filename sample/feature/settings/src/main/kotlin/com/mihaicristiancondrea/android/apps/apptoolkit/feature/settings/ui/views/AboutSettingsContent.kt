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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.settings.data.repositories.ShowcaseUnlockRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logUnlockAchievement
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.result.runSuspendCatching
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.about.ui.AboutScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/** The achievement id the showcase unlock reports. Dashboards group by it, so it never changes. */
internal const val SHOWCASE_ACHIEVEMENT_ID: String = "showcase_unlocked"

/** The About surface for this app: the toolkit screen plus the hidden version-tap unlock. */
@Composable
fun AboutSettingsContent() {
    val showcaseUnlockRepository: ShowcaseUnlockRepository = koinInject()
    val telemetry = LocalTelemetry.current
    val coroutineScope = rememberCoroutineScope()

    AboutScreen(
        onVersionTap = { tapCount ->
            // Leaving the screen cancels this scope, and the unlock with it.
            coroutineScope.launch { unlockShowcase(showcaseUnlockRepository, tapCount, telemetry) }
        },
    )
}

/**
 * Asks [repository] to unlock the showcase for [tapCount], then reports what happened: the
 * `unlock_achievement` event only once a new unlock is saved, and a failed save as a non-fatal.
 * Cancellation propagates and reports neither.
 */
internal suspend fun unlockShowcase(
    repository: ShowcaseUnlockRepository,
    tapCount: Int,
    telemetry: TelemetryRepository,
) {
    runSuspendCatching { repository.unlockAfterVersionTaps(tapCount = tapCount) }
        .onSuccess { unlocked -> if (unlocked) telemetry.logUnlockAchievement(SHOWCASE_ACHIEVEMENT_ID) }
        .onFailure { throwable ->
            telemetry.recordNonFatal(
                throwable = throwable,
                attributes = mapOf("operation" to "unlock_components_showcase"),
            )
        }
}
