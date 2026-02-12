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

package com.d4rk.android.libs.apptoolkit.core.ui.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.d4rk.android.libs.apptoolkit.app.theme.ui.style.AppTheme

/**
 * Activity that provides shared Compose setup for screens.
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Compose content to be rendered by this activity.
     */
    @Composable
    protected abstract fun ScreenContent()

    /**
     * Controls whether this activity should set its Compose content during [onCreate].
     */
    protected open fun shouldSetContentOnCreate(): Boolean = true

    /**
     * Sets the activity content using the shared [AppTheme] wrapper.
     */
    protected fun setComposeContent(content: @Composable () -> Unit = { ScreenContent() }) {
        setContent {
            AppTheme {
                content()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (shouldSetContentOnCreate()) {
            setComposeContent()
        }
    }
}
