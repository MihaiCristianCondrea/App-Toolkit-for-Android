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

package com.d4rk.android.apps.apptoolkit.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.d4rk.android.apps.apptoolkit.core.utils.constants.logging.FAVORITES_CHANGED_LOG_TAG

class FavoritesChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val pkg = intent?.getStringExtra(EXTRA_PACKAGE_NAME)
        Log.d(FAVORITES_CHANGED_LOG_TAG, "Favorites changed: $pkg")
    }

    companion object {
        const val ACTION_FAVORITES_CHANGED =
            "com.d4rk.android.apps.apptoolkit.action.FAVORITES_CHANGED"
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
    }
}
