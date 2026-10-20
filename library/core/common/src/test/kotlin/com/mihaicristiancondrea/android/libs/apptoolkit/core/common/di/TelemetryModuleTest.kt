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
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TelemetryModuleTest {

    /**
     * Runs [block] against a fresh Koin and closes it, as `stopKoin` does between an app's tests:
     * a module declared as a `val` keeps its singles until the Koin holding them is closed.
     */
    private fun withKoin(vararg modules: Module, block: (Koin) -> Unit) {
        val application = koinApplication { modules(*modules) }
        try {
            block(application.koin)
        } finally {
            application.close()
        }
    }

    @Test
    fun `every contract is the one bound telemetry`() {
        val telemetry = FakeTelemetryRepository()
        withKoin(module { single<TelemetryRepository> { telemetry } }, telemetryModule) { koin ->
            assertSame(telemetry, koin.get<AnalyticsReporter>())
            assertSame(telemetry, koin.get<DiagnosticsReporter>())
            assertSame(telemetry, koin.get<TelemetryCollection>())
        }
    }

    @Test
    fun `a host that replaces the telemetry replaces every contract with it`() {
        val toolkit = FakeTelemetryRepository()
        val host = FakeTelemetryRepository()
        withKoin(
            module { single<TelemetryRepository> { toolkit } },
            telemetryModule,
            module { single<TelemetryRepository> { host } },
        ) { koin ->
            assertSame(host, koin.get<TelemetryRepository>())
            assertSame(host, koin.get<AnalyticsReporter>())
            assertSame(host, koin.get<DiagnosticsReporter>())
            assertSame(host, koin.get<TelemetryCollection>())
        }
    }
}
