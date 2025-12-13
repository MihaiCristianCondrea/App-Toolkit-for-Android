package com.d4rk.android.libs.apptoolkit.app.licenses

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                LicensesScreen(
                    onBackClicked = {
                        this.finish()
                    }
                )
            }
        }
    }
}