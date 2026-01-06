package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.widget.Toast
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.packagemanager.isAppInstalled
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TestAppInfoHelper {

    /**
     * `try { block } finally { cleanup }` but expressed via Result-style chaining:
     * - preserves the original failure (test still fails as expected)
     * - always runs cleanup
     * - if cleanup fails too, it is added as a suppressed exception
     */
    private suspend inline fun <T> runCatchingFinally(
        crossinline block: suspend () -> T,
        crossinline finallyBlock: () -> Unit
    ): T {
        val result: Result<T> = runCatching { block() }

        val cleanupError: Throwable? = runCatching { finallyBlock() }.exceptionOrNull()

        if (cleanupError != null) {
            result.exceptionOrNull()?.let { primary ->
                primary.addSuppressed(cleanupError)
                throw primary
            }
            throw cleanupError
        }

        return result.getOrThrow()
    }

    private suspend inline fun <T> withToastStaticMock(
        crossinline block: suspend () -> T
    ): T {
        mockkStatic(Toast::class)
        return runCatchingFinally(
            block = { block() },
            finallyBlock = { unmockkStatic(Toast::class) }
        )
    }

    @Test
    fun `openApp adds new task flag when context not Activity`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openApp adds new task flag when context not Activity")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        val intent = mockk<Intent>(relaxed = true)
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns intent
        every { intent.resolveActivity(pm) } returns mockk<ComponentName>()
        justRun { context.startActivity(intent) }

        AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")

        verify { intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        println("🏁 [TEST DONE] openApp adds new task flag when context not Activity")
    }

    @Test
    fun `isAppInstalled returns true when app exists`() = runTest {
        println("🚀 [TEST] isAppInstalled returns true when app exists")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        every { context.packageManager } returns pm
        every { pm.getPackageInfo("pkg", 0) } returns PackageInfo()

        val result = context.isAppInstalled("pkg")

        assertEquals(true, result)
        println("🏁 [TEST DONE] isAppInstalled returns true when app exists")
    }

    @Test
    fun `isAppInstalled returns false when app missing`() = runTest {
        println("🚀 [TEST] isAppInstalled returns false when app missing")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        every { context.packageManager } returns pm
        every { pm.getPackageInfo("pkg", 0) } throws PackageManager.NameNotFoundException()

        val result = context.isAppInstalled("pkg")

        assertEquals(false, result)
        println("🏁 [TEST DONE] isAppInstalled returns false when app missing")
    }

    @Test
    fun `openApp does not add new task flag when context is Activity`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openApp does not add new task flag when context is Activity")
        val context = mockk<Activity>()
        val pm = mockk<PackageManager>()
        val intent = mockk<Intent>(relaxed = true)
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns intent
        every { intent.resolveActivity(pm) } returns mockk<ComponentName>()
        justRun { context.startActivity(intent) }

        AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")

        verify(exactly = 0) { intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        println("🏁 [TEST DONE] openApp does not add new task flag when context is Activity")
    }

    @Test
    fun `openApp shows toast and returns false when launch intent missing`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openApp shows toast and returns false when launch intent missing")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns null
        every { context.getString(any()) } returns "not installed"

        withToastStaticMock {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")

            assertEquals(false, result)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openApp shows toast and returns false when launch intent missing")
        }
    }

    @Test
    fun `openAppResult returns success when launch succeeds`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openAppResult returns success when launch succeeds")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        val intent = mockk<Intent>(relaxed = true)
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns intent
        every { intent.resolveActivity(pm) } returns mockk<ComponentName>()
        justRun { context.startActivity(intent) }

        val result = AppInfoHelper(TestDispatchers(dispatcher)).openAppResult(context, "pkg")

        assertEquals(Result.success(true), result)
        println("🏁 [TEST DONE] openAppResult returns success when launch succeeds")
    }

    @Test
    fun `openApp returns false on start failure`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openApp returns false on start failure")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        val intent = mockk<Intent>()
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns intent
        every { intent.resolveActivity(pm) } returns mockk<ComponentName>()
        every { context.getString(any()) } returns "not installed"

        withToastStaticMock {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast
            every { context.startActivity(intent) } throws RuntimeException("fail")

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")

            assertEquals(false, result)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openApp returns false on start failure")
        }
    }

    @Test
    fun `openAppResult exposes failure`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] openAppResult exposes failure")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        val intent = mockk<Intent>()
        every { context.packageManager } returns pm
        every { pm.getLaunchIntentForPackage("pkg") } returns intent
        every { intent.resolveActivity(pm) } returns mockk<ComponentName>()
        every { context.getString(any()) } returns "not installed"

        withToastStaticMock {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast
            every { context.startActivity(intent) } throws RuntimeException("fail")

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openAppResult(context, "pkg")

            assertTrue(result.isFailure)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openAppResult exposes failure")
        }
    }
}
