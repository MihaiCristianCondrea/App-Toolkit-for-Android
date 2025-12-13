package com.d4rk.android.libs.apptoolkit.app.help.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.HelpScreenConfig
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpActivity : AppCompatActivity() {
    private val config : HelpScreenConfig by inject()
    private val viewModel: HelpViewModel by viewModel()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                HelpScreen(config = config, viewModel = viewModel)
            }
        }
    }
}