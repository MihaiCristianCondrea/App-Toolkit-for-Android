package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.IntentCompat
import android.util.Log
import androidx.glance.appwidget.action.InvisibleActionTrampolineActivity

/**
 * Keeps Glance's private component address compatible with existing widget PendingIntents.
 * Glance 1.2.0 throws on a bare launch; only its two missing-envelope failures are ignored.
 * Valid actions and failures inside their destinations retain AndroidX's handling.
 */
class SampleInvisibleActionTrampolineActivity : InvisibleActionTrampolineActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val missingEnvelope = IntentCompat.getParcelableExtra(intent, "ACTION_INTENT", Intent::class.java) == null ||
            intent.getStringExtra("ACTION_TYPE") == null
        try {
            super.onCreate(savedInstanceState)
        } catch (failure: IllegalArgumentException) {
            if (!failure.isMissingGlanceAction(missingEnvelope)) throw failure
            Log.w("SampleWidget", "Ignoring widget trampoline launch with missing action data")
            finish()
        }
    }
}

internal fun IllegalArgumentException.isMissingGlanceAction(missingEnvelope: Boolean): Boolean =
    missingEnvelope &&
        (message == "List adapter activity trampoline invoked without specifying target intent." ||
            message == "List adapter activity trampoline invoked without trampoline type")