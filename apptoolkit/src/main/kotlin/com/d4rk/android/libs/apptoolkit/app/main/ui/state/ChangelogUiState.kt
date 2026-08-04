/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.libs.apptoolkit.app.main.ui.state

import androidx.compose.runtime.Immutable

/** Immutable content rendered by the changelog dialog. */
@Immutable
data class ChangelogUiState(
    val markdown: String = "",
)
