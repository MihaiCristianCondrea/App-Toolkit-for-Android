/*
 * Copyright (C) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.d4rk.android.libs.apptoolkit.app.main.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/** User and lifecycle requests supported by the changelog dialog. */
sealed interface ChangelogEvent : UiEvent {
    data object Load : ChangelogEvent
    data object Retry : ChangelogEvent
}
