package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.widget.Toast
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
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
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] isAppInstalled returns true when app exists")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        val appInfo = mockk<ApplicationInfo>()
        every { context.packageManager } returns pm
        every { pm.getApplicationInfo("pkg", 0) } returns appInfo

        val result = AppInfoHelper(TestDispatchers(dispatcher)).isAppInstalled(context, "pkg")

        assertEquals(true, result)
        println("🏁 [TEST DONE] isAppInstalled returns true when app exists")
    }

    @Test
    fun `isAppInstalled returns false when app missing`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        println("🚀 [TEST] isAppInstalled returns false when app missing")
        val context = mockk<Context>()
        val pm = mockk<PackageManager>()
        every { context.packageManager } returns pm
        every { pm.getApplicationInfo("pkg", 0) } throws PackageManager.NameNotFoundException()

        val result = AppInfoHelper(TestDispatchers(dispatcher)).isAppInstalled(context, "pkg")

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
        mockkStatic(Toast::class)
        try {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")

            assertEquals(false, result)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openApp shows toast and returns false when launch intent missing")
        } finally {
            unmockkStatic(Toast::class)
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
        mockkStatic(Toast::class)
        try {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast
            every { context.startActivity(intent) } throws RuntimeException("fail")

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openApp(context, "pkg")
            assertEquals(false, result)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openApp returns false on start failure")
        } finally {
            unmockkStatic(Toast::class)
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
        mockkStatic(Toast::class)
        try {
            val toast = mockk<Toast>(relaxed = true)
            every { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) } returns toast
            every { context.startActivity(intent) } throws RuntimeException("fail")

            val result = AppInfoHelper(TestDispatchers(dispatcher)).openAppResult(context, "pkg")
            assertTrue(result.isFailure)
            verify { Toast.makeText(context, "not installed", Toast.LENGTH_SHORT) }
            println("🏁 [TEST DONE] openAppResult exposes failure")
        } finally {
            unmockkStatic(Toast::class)
        }
    }
}
