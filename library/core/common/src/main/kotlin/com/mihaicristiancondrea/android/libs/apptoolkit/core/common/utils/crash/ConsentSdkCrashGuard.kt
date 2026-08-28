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

import android.os.Looper
import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.logging.CONSENT_LOG_TAG

/**
 * Keeps a known crash inside the User Messaging Platform SDK from killing the host process.
 *
 * ### What the SDK does
 *
 * `user-messaging-platform:4.0.0` pings a metrics reporting URL after a failed consent request. When
 * the response is not `200`/`204` it reads the error body with:
 *
 * ```java
 * new Scanner(connection.getErrorStream()).useDelimiter("\\A").next();
 * ```
 *
 * The stream is null-checked, the *body* is not, so an empty error body makes `Scanner.next()` throw
 * [NoSuchElementException]. The ping runs on the SDK's own `ThreadPoolExecutor`, so no `catch` in
 * `UmpConsentRemoteDataSource`, `DefaultConsentRepository`, or `RequestConsentUseCase` can ever see it:
 * an uncaught throwable on a plain executor thread reaches the default uncaught-exception handler
 * and takes the process down. 4.0.0 is the newest published version, so there is no upgrade that
 * fixes the read.
 *
 * ### What this guard does
 *
 * It wraps the default uncaught-exception handler and swallows *only* that exact failure, recording
 * it as a non-fatal instead. The ping is pure telemetry for Google: dropping it has no functional
 * effect on the consent flow, which has already reported its own error to the caller by this point.
 *
 * The predicate is deliberately narrow, every condition must hold:
 * - the throwable is a [NoSuchElementException];
 * - the stack trace contains a `java.util.Scanner` frame;
 * - the stack trace contains a `com.google.android.gms.internal.consent_sdk` frame;
 * - the crashing thread is not the main thread.
 *
 * Anything else is passed straight to the previous handler, unchanged.
 *
 * The real fix for the underlying failure is upstream (Google), and the fix for the *cause* is on
 * our side: pointing the consent request at the host app's own AdMob app id (see
 * `AdMobAppIdProvider`) and never running two consent round trips at once (see
 * `DefaultConsentRepository`). This guard covers the remainder and should be removed once a
 * `user-messaging-platform` release fixes the read, verified by decompiling, not by release notes.
 */
object ConsentSdkCrashGuard {

    private const val SCANNER_CLASS_NAME: String = "java.util.Scanner"
    private const val CONSENT_SDK_PACKAGE: String = "com.google.android.gms.internal.consent_sdk"

    @Volatile
    private var installed: Boolean = false

    /**
     * Installs the guard on top of the current default uncaught-exception handler.
     *
     * Install this *after* Crashlytics has registered its own handler so that this guard wraps it:
     * anything the guard does not recognise is delegated to Crashlytics exactly as before, and the
     * one recognised failure never reaches it as a fatal.
     *
     * Repeated calls are ignored, so it is safe to call from an [android.app.Application] subclass
     * that a consumer also initializes manually.
     *
     * @param reportNonFatal invoked with the swallowed throwable and diagnostic attributes. It is
     * called on the crashing thread and must not throw.
     */
    @Synchronized
    fun install(reportNonFatal: (Throwable, Map<String, String>) -> Unit) {
        if (installed) return
        installed = true

        val previousHandler: Thread.UncaughtExceptionHandler? =
            Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (!isConsentSdkMetricsPingFailure(thread = thread, throwable = throwable)) {
                previousHandler?.uncaughtException(thread, throwable)
                return@setDefaultUncaughtExceptionHandler
            }

            Log.w(
                CONSENT_LOG_TAG,
                "Swallowed a UMP metrics-ping crash on thread '${thread.name}'.",
                throwable,
            )
            runCatching {
                reportNonFatal(
                    throwable,
                    mapOf(
                        "guard" to "ConsentSdkCrashGuard",
                        "thread" to thread.name,
                    ),
                )
            }
        }
    }

    /**
     * Whether [throwable] is the UMP metrics-ping failure, and nothing else.
     */
    fun isConsentSdkMetricsPingFailure(thread: Thread, throwable: Throwable): Boolean {
        if (throwable !is NoSuchElementException) return false
        if (isMainThread(thread = thread)) return false

        val frames: Array<StackTraceElement> = throwable.stackTrace
        val hasScannerFrame: Boolean = frames.any { it.className == SCANNER_CLASS_NAME }
        val hasConsentSdkFrame: Boolean = frames.any {
            it.className.startsWith(CONSENT_SDK_PACKAGE)
        }
        return hasScannerFrame && hasConsentSdkFrame
    }

    /**
     * Resolves the main thread through [Looper] when one is available, and falls back to the
     * thread name so the predicate stays usable in plain JVM unit tests.
     */
    private fun isMainThread(thread: Thread): Boolean =
        runCatching { Looper.getMainLooper()?.thread === thread }
            .getOrElse { thread.name == "main" }
}
