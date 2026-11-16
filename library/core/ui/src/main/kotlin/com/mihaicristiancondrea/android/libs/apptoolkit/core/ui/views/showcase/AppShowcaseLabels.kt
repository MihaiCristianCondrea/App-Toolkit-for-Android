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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase

import androidx.compose.runtime.Stable

/**
 * The words an [AppShowcase] uses. The host supplies them, so the section speaks the app's
 * language and names its own store; loading and retry default to the Toolkit's strings.
 *
 * @property storeActionDescription Spoken for a card's store action, given the app's name.
 * @property openActionDescription Spoken for an installed app's action, given its name.
 */
@Stable
class AppShowcaseLabels(
    val title: String,
    val viewAll: String,
    val viewAllDescription: String,
    val store: String,
    val open: String,
    val empty: String,
    val failed: String,
    val loading: String,
    val retry: String,
    val storeActionDescription: (appName: String) -> String,
    val openActionDescription: (appName: String) -> String,
)
