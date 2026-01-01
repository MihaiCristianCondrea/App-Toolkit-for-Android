package com.d4rk.android.libs.apptoolkit.app.settings.general.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import com.d4rk.android.libs.apptoolkit.core.logging.GENERAL_SETTINGS_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.startActivitySafely
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import kotlinx.coroutines.launch

class GeneralSettingsActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            CommonDataStore.getInstance(applicationContext).markSettingsInteracted()
        }

        val title: String = intent.getStringExtra(EXTRA_TITLE)
            ?: getString(com.d4rk.android.libs.apptoolkit.R.string.settings)
        val contentKey: String? = intent.getStringExtra(EXTRA_CONTENT)

        setContent {
            AppTheme {
                GeneralSettingsScreen(title = title, contentKey = contentKey) { finish() }
            }
        }
    }
}
