package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.d4rk.android.libs.apptoolkit.core.utils.constants.links.AppLinks
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.safeStartActivity
import com.d4rk.android.libs.apptoolkit.test.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestIntentsHelper {

    @Before
    fun setUp() {
        mockkStatic("com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.ContextExtensionsKt")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `openUrl starts ACTION_VIEW intent`() {
        println("🚀 [TEST] openUrl starts ACTION_VIEW intent")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openUrl(context = context, url = "https://example.com")

        val intent = intents.single()
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals("https://example.com", intent.data.toString())
        assertTrue(addNewTaskFlags.single())
        println("🏁 [TEST DONE] openUrl starts ACTION_VIEW intent")
    }

    @Test
    fun `openActivity starts activity with new task flag`() {
        println("🚀 [TEST] openActivity starts activity with new task flag")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openActivity(context, String::class.java)

        val intent = intents.single()
        assertEquals(String::class.java.name, intent.component?.className)
        assertTrue(addNewTaskFlags.single())
        println("🏁 [TEST DONE] openActivity starts activity with new task flag")
    }

    @Test
    fun `openUrl returns false on failure`() {
        println("🚀 [TEST] openUrl returns false on failure")
        val context = mockk<Context>()
        mockSafeStartActivity(context, results = listOf(false))

        val result = IntentsHelper.openUrl(context = context, url = "https://example.com")
        assertEquals(false, result)
        println("🏁 [TEST DONE] openUrl returns false on failure")
    }

    @Test
    fun `openActivity returns false on failure`() {
        println("🚀 [TEST] openActivity returns false on failure")
        val context = mockk<Context>()
        mockSafeStartActivity(context, results = listOf(false))

        val result = IntentsHelper.openActivity(context, String::class.java)
        assertEquals(false, result)
        println("🏁 [TEST DONE] openActivity returns false on failure")
    }

    @Test
    fun `openAppNotificationSettings builds correct intent`() {
        println("🚀 [TEST] openAppNotificationSettings builds correct intent")
        val context = mockk<Context>()
        every { context.packageName } returns "pkg"
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openAppNotificationSettings(context)

        val intent = intents.single()
        assertEquals(true, addNewTaskFlags.single())
        assertTrue(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assertEquals(Settings.ACTION_APP_NOTIFICATION_SETTINGS, intent.action)
            assertEquals("pkg", intent.getStringExtra(Settings.EXTRA_APP_PACKAGE))
        } else {
            assertEquals("android.settings.APPLICATION_DETAILS_SETTINGS", intent.action)
            assertEquals(Uri.fromParts("package", "pkg", null), intent.data)
        }
        println("🏁 [TEST DONE] openAppNotificationSettings builds correct intent")
    }

    @Test
    fun `openPlayStoreForApp uses market when resolvable`() {
        println("🚀 [TEST] openPlayStoreForApp uses market when resolvable")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        val result = IntentsHelper.openPlayStoreForApp(context, "com.test")

        val intent = intents.single()
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals("${AppLinks.MARKET_APP_PAGE}com.test", intent.data.toString())
        assertFalse(addNewTaskFlags.single())
        assertTrue(result)
        println("🏁 [TEST DONE] openPlayStoreForApp uses market when resolvable")
    }

    @Test
    fun `openPlayStoreForApp falls back to web when market missing`() {
        println("🚀 [TEST] openPlayStoreForApp falls back to web when market missing")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(
            context = context,
            results = listOf(false, true)
        )

        IntentsHelper.openPlayStoreForApp(context, "com.test")

        assertEquals(2, intents.size)
        val marketIntent = intents.first()
        val webIntent = intents.last()

        assertEquals(Intent.ACTION_VIEW, marketIntent.action)
        assertEquals("${AppLinks.MARKET_APP_PAGE}com.test", marketIntent.data.toString())
        assertFalse(addNewTaskFlags.first())

        assertEquals(Intent.ACTION_VIEW, webIntent.action)
        assertEquals("${AppLinks.PLAY_STORE_APP}com.test", webIntent.data.toString())
        assertTrue(addNewTaskFlags.last())
        println("🏁 [TEST DONE] openPlayStoreForApp falls back to web when market missing")
    }

    @Test
    fun `shareApp builds chooser intent`() {
        println("🚀 [TEST] shareApp builds chooser intent")
        val context = mockk<Context>()
        val res = mockk<Resources>()
        every { context.packageName } returns "pkg"
        every { context.resources } returns res
        every { res.getText(R.string.send_email_using) } returns "send"
        every {
            context.getString(
                R.string.summary_share_message,
                "${AppLinks.PLAY_STORE_APP}pkg"
            )
        } returns "msg"
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.shareApp(
            context = context,
            shareMessageFormat = R.string.summary_share_message
        )

        val chooser = intents.single()
        assertTrue(addNewTaskFlags.single())
        assertEquals(Intent.ACTION_CHOOSER, chooser.action)
        val sendIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            chooser.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            chooser.getParcelableExtra(Intent.EXTRA_INTENT)
        }
        assertEquals(Intent.ACTION_SEND, sendIntent?.action)
        assertEquals("msg", sendIntent?.getStringExtra(Intent.EXTRA_TEXT))
        assertEquals("text/plain", sendIntent?.type)
        println("🏁 [TEST DONE] shareApp builds chooser intent")
    }

    @Test
    fun `shareApp uses provided package name`() {
        println("\uD83D\uDE80 [TEST] shareApp uses provided package name")
        val context = mockk<Context>()
        val res = mockk<Resources>()
        every { context.packageName } returns "pkg"
        every { context.resources } returns res
        every { res.getText(R.string.send_email_using) } returns "send"
        every {
            context.getString(
                R.string.summary_share_message,
                "${AppLinks.PLAY_STORE_APP}other"
            )
        } returns "msg"
        val (intents, _) = mockSafeStartActivity(context)

        IntentsHelper.shareApp(context, R.string.summary_share_message, packageName = "other")

        val chooser = intents.single()
        val sendIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            chooser.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            chooser.getParcelableExtra(Intent.EXTRA_INTENT)
        }
        assertEquals("msg", sendIntent?.getStringExtra(Intent.EXTRA_TEXT))
        println("\uD83C\uDFC1 [TEST DONE] shareApp uses provided package name")
    }

    @Test
    fun `sendEmailToDeveloper builds mailto chooser`() {
        println("🚀 [TEST] sendEmailToDeveloper builds mailto chooser")
        val context = mockk<Context>()
        every { context.getString(R.string.feedback_for, "App") } returns "subject"
        every { context.getString(R.string.dear_developer) } returns "body"
        every { context.getString(R.string.send_email_using) } returns "send"
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.sendEmailToDeveloper(context, R.string.app_name)

        val chooser = intents.single()
        assertTrue(addNewTaskFlags.single())
        assertEquals(Intent.ACTION_CHOOSER, chooser.action)
        val inner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            chooser.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            chooser.getParcelableExtra(Intent.EXTRA_INTENT)
        }
        assertEquals(Intent.ACTION_SENDTO, inner?.action)
        assertTrue(inner?.data.toString().startsWith("mailto:"))
        println("🏁 [TEST DONE] sendEmailToDeveloper builds mailto chooser")
    }

    @Test
    fun `openAppNotificationSettings returns false on failure`() {
        println("🚀 [TEST] openAppNotificationSettings returns false on failure")
        val context = mockk<Context>()
        every { context.packageName } returns "pkg"
        mockSafeStartActivity(context, results = listOf(false))

        val result = IntentsHelper.openAppNotificationSettings(context)
        assertEquals(false, result)
        println("🏁 [TEST DONE] openAppNotificationSettings returns false on failure")
    }

    @Test
    fun `openPlayStoreForApp returns false on failure`() {
        println("🚀 [TEST] openPlayStoreForApp returns false on failure")
        val context = mockk<Context>()
        val (intents, _) = mockSafeStartActivity(context, results = listOf(false, false))

        val result = IntentsHelper.openPlayStoreForApp(context = context, packageName = "com.test")
        assertEquals(2, intents.size)
        assertEquals(false, result)
        println("🏁 [TEST DONE] openPlayStoreForApp returns false on failure")
    }

    @Test
    fun `shareApp returns false on failure`() {
        println("🚀 [TEST] shareApp returns false on failure")
        val context = mockk<Context>()
        val res = mockk<Resources>()
        every { context.packageName } returns "pkg"
        every { context.resources } returns res
        every { res.getText(R.string.send_email_using) } returns "send"
        every {
            context.getString(
                R.string.summary_share_message,
                "${AppLinks.PLAY_STORE_APP}pkg"
            )
        } returns "msg"
        mockSafeStartActivity(context, results = listOf(false))

        val result = IntentsHelper.shareApp(context, R.string.summary_share_message)
        assertEquals(false, result)
        println("🏁 [TEST DONE] shareApp returns false on failure")
    }

    @Test
    fun `sendEmailToDeveloper returns false on failure`() {
        println("🚀 [TEST] sendEmailToDeveloper returns false on failure")
        val context = mockk<Context>()
        every { context.getString(R.string.feedback_for, "App") } returns "subject"
        every { context.getString(R.string.dear_developer) } returns "body"
        every { context.getString(R.string.send_email_using) } returns "send"
        mockSafeStartActivity(context, results = listOf(false))

        val result = IntentsHelper.sendEmailToDeveloper(context, R.string.app_name)
        assertEquals(false, result)
        println("🏁 [TEST DONE] sendEmailToDeveloper returns false on failure")
    }

    @Test
    fun `openPlayStoreForApp with empty package name`() {
        println("🚀 [TEST] openPlayStoreForApp with empty package name")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openPlayStoreForApp(context, "")

        val intent = intents.single()
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(AppLinks.MARKET_APP_PAGE, intent.data.toString())
        assertFalse(addNewTaskFlags.single())
        println("🏁 [TEST DONE] openPlayStoreForApp with empty package name")
    }

    @Test
    fun `openPlayStoreForApp null package throws`() {
        println("🚀 [TEST] openPlayStoreForApp null package throws")
        val context = mockk<Context>()
        val method = IntentsHelper::class.java.getDeclaredMethod(
            "openPlayStoreForApp",
            Context::class.java,
            String::class.java
        )

        assertFailsWith<NullPointerException> {
            method.invoke(IntentsHelper, context, null)
        }
        println("🏁 [TEST DONE] openPlayStoreForApp null package throws")
    }

    @Test
    fun `openUrl handles malformed url`() {
        println("🚀 [TEST] openUrl handles malformed url")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openUrl(context, "htp::://bad url")

        val intent = intents.single()
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals("htp::://bad url", intent.data.toString())
        assertTrue(addNewTaskFlags.single())
        println("🏁 [TEST DONE] openUrl handles malformed url")
    }

    @Test
    fun `openPlayStoreForApp handles unusual package name`() {
        println("🚀 [TEST] openPlayStoreForApp handles unusual package name")
        val context = mockk<Context>()
        val (intents, _) = mockSafeStartActivity(context)

        val pkg = "com.example.app-1_2"
        IntentsHelper.openPlayStoreForApp(context, pkg)

        val intent = intents.single()
        assertEquals("${AppLinks.MARKET_APP_PAGE}$pkg", intent.data.toString())
        println("🏁 [TEST DONE] openPlayStoreForApp handles unusual package name")
    }

    @Test
    fun `shareApp uses provided chooser title`() {
        println("🚀 [TEST] shareApp uses provided chooser title")
        val context = mockk<Context>()
        val res = mockk<Resources>()
        every { context.packageName } returns "pkg"
        every { context.resources } returns res
        every { res.getText(R.string.send_email_using) } returns "Share via \u2728"
        every {
            context.getString(
                R.string.summary_share_message,
                "${AppLinks.PLAY_STORE_APP}pkg"
            )
        } returns "msg"
        val (intents, _) = mockSafeStartActivity(context)

        IntentsHelper.shareApp(context, R.string.summary_share_message)

        val chooser = intents.single()
        assertEquals("Share via \u2728", chooser.getCharSequenceExtra(Intent.EXTRA_TITLE))
        println("🏁 [TEST DONE] shareApp uses provided chooser title")
    }

    @Test
    fun `sendEmailToDeveloper uses provided chooser title`() {
        println("🚀 [TEST] sendEmailToDeveloper uses provided chooser title")
        val context = mockk<Context>()
        every { context.getString(R.string.feedback_for, "App") } returns "subject"
        every { context.getString(R.string.dear_developer) } returns "body"
        every { context.getString(R.string.send_email_using) } returns "Email via \uD83D\uDE80"
        val (intents, _) = mockSafeStartActivity(context)

        IntentsHelper.sendEmailToDeveloper(context, R.string.app_name)

        val chooser = intents.single()
        assertEquals("Email via \uD83D\uDE80", chooser.getCharSequenceExtra(Intent.EXTRA_TITLE))
        println("🏁 [TEST DONE] sendEmailToDeveloper uses provided chooser title")
    }

    @Test
    fun `openAppNotificationSettings uses legacy intent pre O`() {
        println("🚀 [TEST] openAppNotificationSettings uses legacy intent pre O")
        val context = mockk<Context>()
        every { context.packageName } returns "pkg"
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        mockkStatic(Build.VERSION::class)
        every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.N

        IntentsHelper.openAppNotificationSettings(context)

        val intent = intents.single()
        assertTrue(addNewTaskFlags.single())
        assertEquals("android.settings.APPLICATION_DETAILS_SETTINGS", intent.action)
        assertEquals(Uri.fromParts("package", "pkg", null), intent.data)
        println("🏁 [TEST DONE] openAppNotificationSettings uses legacy intent pre O")
    }

    @Test
    fun `openDisplaySettings uses display intent when resolvable`() {
        println("🚀 [TEST] openDisplaySettings uses display intent when resolvable")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(context)

        IntentsHelper.openDisplaySettings(context)

        val intent = intents.single()
        assertEquals(Settings.ACTION_DISPLAY_SETTINGS, intent.action)
        assertFalse(addNewTaskFlags.single())
        println("🏁 [TEST DONE] openDisplaySettings uses display intent when resolvable")
    }

    @Test
    fun `openDisplaySettings falls back to general settings`() {
        println("🚀 [TEST] openDisplaySettings falls back to general settings")
        val context = mockk<Context>()
        val (intents, addNewTaskFlags) = mockSafeStartActivity(
            context = context,
            results = listOf(false, true)
        )

        IntentsHelper.openDisplaySettings(context)

        assertEquals(
            listOf(Settings.ACTION_DISPLAY_SETTINGS, Settings.ACTION_SETTINGS),
            intents.map { it.action })
        assertEquals(listOf(false, false), addNewTaskFlags)
        println("🏁 [TEST DONE] openDisplaySettings falls back to general settings")
    }

    private fun mockSafeStartActivity(
        context: Context,
        results: List<Boolean> = listOf(true),
    ): Pair<MutableList<Intent>, MutableList<Boolean>> {
        val intents = mutableListOf<Intent>()
        val addNewTaskFlags = mutableListOf<Boolean>()
        var index = 0
        every {
            context.safeStartActivity(
                intent = any(),
                addNewTaskFlag = any(),
                onFailure = any(),
            )
        } answers {
            intents.add(firstArg())
            addNewTaskFlags.add(secondArg())
            results.getOrElse(index++) { results.last() }
        }
        return intents to addNewTaskFlags
    }
}
