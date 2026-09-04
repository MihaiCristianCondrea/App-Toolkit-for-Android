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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.navigation.domain.models

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.window.AppWindowWidthSizeClass
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.StableNavKey

/**
 * Context shared by all navigation entry builders in the app.
 *
 * Lives in `:sample:core:navigation` rather than with the shell that creates it: a feature module
 * needs this type to declare its entry builder, and depending on the shell for it would make every
 * feature depend on every other feature the shell wires together.
 */
@Stable
data class AppNavigationEntryContext(
    val paddingValues: PaddingValues,
    val windowWidthSizeClass: AppWindowWidthSizeClass,
    val onRandomAppHandlerChanged: (StableNavKey, RandomAppHandler?) -> Unit,
) {
    fun registerRandomAppHandlerFor(route: StableNavKey): (RandomAppHandler?) -> Unit = { handler ->
        onRandomAppHandlerChanged(route, handler)
    }
}
