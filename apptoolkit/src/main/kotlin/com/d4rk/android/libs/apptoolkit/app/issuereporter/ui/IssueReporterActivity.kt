package com.d4rk.android.libs.apptoolkit.app.issuereporter.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.d4rk.android.libs.apptoolkit.app.theme.style.AppTheme

class IssueReporterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                IssueReporterScreen(
                    onBackClicked = {
                        this.finish()
                    }
                )
            }
        }
    }
}
