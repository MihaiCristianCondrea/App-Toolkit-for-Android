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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.di

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.AnalyticsObserver
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.ObservedTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.junit.jupiter.api.Test
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class FirebaseModuleTest {

    @Test
    fun `firebaseModule wraps FirebaseTelemetryRepository in ObservedTelemetryRepository and notifies observers`() {
        val observedEvents = mutableListOf<AnalyticsEvent>()
        val observedScreens = mutableListOf<Pair<String, String?>>()

        val app = koinApplication {
            modules(
                firebaseModule,
                module {
                    single {
                        object : AnalyticsObserver {
                            override fun onEvent(event: AnalyticsEvent) {
                                observedEvents += event
                            }

                            override fun onScreenView(screenName: String, screenClass: String?) {
                                observedScreens += screenName to screenClass
                            }
                        }
                    } bind AnalyticsObserver::class
                },
            )
        }

        try {
            val repository = app.koin.get<TelemetryRepository>()
            assertIs<ObservedTelemetryRepository>(repository)

            // FirebaseApp is not initialized on JVM, so FirebaseTelemetryRepository fails closed
            // while ObservedTelemetryRepository still forwards events to bound observers.
            val event = AnalyticsEvent(name = "custom_event")
            repository.logEvent(event)
            repository.logScreenView(screenName = "HomeScreen", screenClass = "MainActivity")

            assertEquals(listOf(event), observedEvents)
            assertEquals(listOf<Pair<String, String?>>("HomeScreen" to "MainActivity"), observedScreens)
        } finally {
            app.close()
        }
    }
}
