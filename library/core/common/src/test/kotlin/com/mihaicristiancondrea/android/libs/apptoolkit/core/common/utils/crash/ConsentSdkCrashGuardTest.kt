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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.crash

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The guard is only allowed to swallow the UMP metrics-ping crash. Everything else must keep
 * reaching Crashlytics as a fatal, so the predicate is pinned down from both sides.
 */
class ConsentSdkCrashGuardTest {

    private val workerThread = Thread("pool-1-thread-3")
    private val mainThread = Thread("main")

    @Test
    fun `recognises the UMP metrics ping failure`() {
        val throwable = NoSuchElementException().withFrames(
            "java.util.Scanner" to "next",
            "com.google.android.gms.internal.consent_sdk.zzcd" to "zza",
            $$"java.util.concurrent.ThreadPoolExecutor$Worker" to "run",
        )

        assertTrue(
            ConsentSdkCrashGuard.isConsentSdkMetricsPingFailure(
                thread = workerThread,
                throwable = throwable,
            )
        )
    }

    @Test
    fun `ignores the same signature on the main thread`() {
        val throwable = NoSuchElementException().withFrames(
            "java.util.Scanner" to "next",
            "com.google.android.gms.internal.consent_sdk.zzcd" to "zza",
        )

        assertFalse(
            ConsentSdkCrashGuard.isConsentSdkMetricsPingFailure(
                thread = mainThread,
                throwable = throwable,
            )
        )
    }

    @Test
    fun `ignores a NoSuchElementException without a Scanner frame`() {
        val throwable = NoSuchElementException().withFrames(
            "com.google.android.gms.internal.consent_sdk.zzcd" to "zza",
            $$"java.util.concurrent.ThreadPoolExecutor$Worker" to "run",
        )

        assertFalse(
            ConsentSdkCrashGuard.isConsentSdkMetricsPingFailure(
                thread = workerThread,
                throwable = throwable,
            )
        )
    }

    @Test
    fun `ignores a Scanner failure outside the consent SDK`() {
        val throwable = NoSuchElementException().withFrames(
            "java.util.Scanner" to "next",
            "com.example.app.SomeParser" to "parse",
        )

        assertFalse(
            ConsentSdkCrashGuard.isConsentSdkMetricsPingFailure(
                thread = workerThread,
                throwable = throwable,
            )
        )
    }

    @Test
    fun `ignores other exception types raised inside the consent SDK`() {
        val throwable = IllegalStateException().withFrames(
            "java.util.Scanner" to "next",
            "com.google.android.gms.internal.consent_sdk.zzcd" to "zza",
        )

        assertFalse(
            ConsentSdkCrashGuard.isConsentSdkMetricsPingFailure(
                thread = workerThread,
                throwable = throwable,
            )
        )
    }

    private fun <T : Throwable> T.withFrames(vararg frames: Pair<String, String>): T = apply {
        stackTrace = frames
            .map { (className, methodName) ->
                StackTraceElement(className, methodName, null, -1)
            }
            .toTypedArray()
    }
}
