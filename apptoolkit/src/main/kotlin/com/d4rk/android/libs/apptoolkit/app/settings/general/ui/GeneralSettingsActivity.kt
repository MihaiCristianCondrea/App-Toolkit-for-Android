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

package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.ui.base.BaseActivity
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.GENERAL_SETTINGS_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.startActivitySafely
import kotlinx.coroutines.launch

class GeneralSettingsActivity : BaseActivity() {

    private var title: String = ""
    private var contentKey: String? = null

    companion object {
        private const val EXTRA_TITLE: String = "extra_title"
        private const val EXTRA_CONTENT: String = "extra_content"

        fun start(context: Context, title: String, contentKey: String) {
            val intent: Intent = Intent(context, GeneralSettingsActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_CONTENT, contentKey)
            }

            val launched = context.startActivitySafely(
                intent = intent,
                onFailure = { throwable ->
                    Log.e(
                        GENERAL_SETTINGS_LOG_TAG,
                        "Unable to resolve activity to handle intent",
                        throwable
                    )
                },
            )

            if (!launched) {
                Log.w(GENERAL_SETTINGS_LOG_TAG, "General settings activity launch failed")
            }
        }
    }

    override fun shouldSetContentOnCreate(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            CommonDataStore.getInstance(applicationContext).markSettingsInteracted()
        }

        title = intent.getStringExtra(EXTRA_TITLE)
            ?: getString(com.d4rk.android.libs.apptoolkit.R.string.settings)
        contentKey = intent.getStringExtra(EXTRA_CONTENT)

        setComposeContent()
    }

    @Composable
    override fun ScreenContent() {
        GeneralSettingsScreen(title = title, contentKey = contentKey) { finish() }
    }
}
