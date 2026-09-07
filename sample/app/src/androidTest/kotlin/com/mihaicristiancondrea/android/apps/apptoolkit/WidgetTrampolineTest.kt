package com.mihaicristiancondrea.android.apps.apptoolkit

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class WidgetTrampolineTest {
    @Test
    fun originalComponentIgnoresMissingEnvelopeAndStillDeliversValidCallback() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val component = ComponentName(context.packageName,
            "androidx.glance.appwidget.action.InvisibleActionTrampolineActivity")
        val info = context.packageManager.getActivityInfo(component, 0)
        assertFalse(info.exported)
        assertTrue(info.targetActivity.endsWith("SampleInvisibleActionTrampolineActivity"))

        fun launch(intent: Intent) {
            context.startActivity(intent.setComponent(component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            instrumentation.waitForIdleSync()
        }
        launch(Intent())
        launch(Intent().putExtra("ACTION_INTENT", Intent("unused")))

        val received = CountDownLatch(1)
        val action = context.packageName + ".TEST_WIDGET_CALLBACK"
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.getStringExtra("payload") == "preserved") received.countDown()
            }
        }
        ContextCompat.registerReceiver(context, receiver, IntentFilter(action), ContextCompat.RECEIVER_NOT_EXPORTED)
        try {
            launch(Intent()
                .putExtra("ACTION_TYPE", "CALLBACK")
                .putExtra("ACTION_INTENT", Intent(action).setPackage(context.packageName)
                    .putExtra("payload", "preserved")))
            assertTrue("Glance callback must still dispatch through its original component", received.await(10, TimeUnit.SECONDS))
        } finally {
            context.unregisterReceiver(receiver)
        }
    }
}
