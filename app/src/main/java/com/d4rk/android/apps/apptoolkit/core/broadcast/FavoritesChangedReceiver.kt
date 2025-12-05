package com.d4rk.android.apps.apptoolkit.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.d4rk.android.apps.apptoolkit.app.logging.FAVORITES_CHANGED_LOG_TAG

class FavoritesChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val pkg = intent?.getStringExtra(EXTRA_PACKAGE_NAME)
        Log.d(FAVORITES_CHANGED_LOG_TAG, "Favorites changed: $pkg")
    }

    companion object {
        const val ACTION_FAVORITES_CHANGED = "com.d4rk.android.apps.apptoolkit.action.FAVORITES_CHANGED"
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
    }
}
