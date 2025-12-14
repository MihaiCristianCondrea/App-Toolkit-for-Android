package com.d4rk.android.libs.apptoolkit.app.support.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                SupportComposable()
            }
        }
    }
}