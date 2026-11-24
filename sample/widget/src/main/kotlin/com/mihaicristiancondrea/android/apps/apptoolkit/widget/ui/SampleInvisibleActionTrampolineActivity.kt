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

package com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.IntentCompat
import androidx.glance.appwidget.action.InvisibleActionTrampolineActivity
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.constants.WidgetLogTags
import com.mihaicristiancondrea.android.apps.apptoolkit.widget.ui.extensions.isMissingGlanceAction

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
            Log.w(WidgetLogTags.SAMPLE_WIDGET, "Ignoring widget trampoline launch with missing action data")
            finish()
        }
    }
}