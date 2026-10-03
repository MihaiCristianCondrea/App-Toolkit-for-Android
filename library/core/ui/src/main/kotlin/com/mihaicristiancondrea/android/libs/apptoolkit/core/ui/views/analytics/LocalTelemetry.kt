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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.NoOpTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.TelemetryRepository
import org.koin.core.context.GlobalContext

/**
 * The [TelemetryRepository] composables report through: screen views, screen states, and the GA4 events a
 * component logs for its own taps.
 *
 * Components read it instead of taking it as a parameter, so a screen does not pass it to each one.
 * [ProvideTelemetry] sets it; `ShellHost` and every Toolkit activity already call that. Where
 * nothing provides it, as in a preview, it is [NoOpTelemetryRepository] and everything is dropped, so a
 * composition the host starts outside `ShellHost`, such as its own dialog window, calls
 * [ProvideTelemetry] to be reported.
 *
 * Static because it is set once at the root and never changes, so reading it costs nothing.
 */
val LocalTelemetry = staticCompositionLocalOf<TelemetryRepository> { NoOpTelemetryRepository }

/**
 * Provides [telemetryRepository] as [LocalTelemetry] to [content]. Call it once at the root of a
 * composition, around the theme.
 *
 * By default it keeps a [TelemetryRepository] an outer composition already provides, or else uses the app's
 * Koin binding. Without either, as in a preview or a test that binds none, it provides
 * [NoOpTelemetryRepository] instead of failing.
 */
@Composable
fun ProvideTelemetry(
    telemetryRepository: TelemetryRepository = rememberTelemetry(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalTelemetry provides telemetryRepository, content = content)
}

/**
 * The [TelemetryRepository] to provide at a composition's root: the one an outer composition provides, else
 * the app's Koin binding, else [NoOpTelemetryRepository]. For a root that already has a
 * `CompositionLocalProvider` and adds `LocalTelemetry provides rememberTelemetry()` to it.
 */
@Composable
fun rememberTelemetry(): TelemetryRepository {
    val provided: TelemetryRepository = LocalTelemetry.current
    return remember(provided) {
        if (provided !== NoOpTelemetryRepository) {
            provided
        } else {
            GlobalContext.getOrNull()?.getOrNull<TelemetryRepository>() ?: NoOpTelemetryRepository
        }
    }
}
