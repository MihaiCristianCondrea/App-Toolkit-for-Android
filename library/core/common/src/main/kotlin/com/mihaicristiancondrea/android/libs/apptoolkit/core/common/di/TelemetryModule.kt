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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.AnalyticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.DiagnosticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryCollection
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Binds [AnalyticsReporter], [DiagnosticsReporter] and [TelemetryCollection] to the app's one
 * [TelemetryRepository].
 *
 * Each resolves the [TelemetryRepository] binding when first asked for, so a host that replaces
 * that binding, or loads it in another module, replaces all three with it, and the SDKs are never
 * set up twice. `appToolkitModules` includes it; a host that assembles its own module list adds
 * it beside the module that binds [TelemetryRepository].
 */
val telemetryModule: Module = module {
    single<AnalyticsReporter> { get<TelemetryRepository>() }
    single<DiagnosticsReporter> { get<TelemetryRepository>() }
    single<TelemetryCollection> { get<TelemetryRepository>() }
}
