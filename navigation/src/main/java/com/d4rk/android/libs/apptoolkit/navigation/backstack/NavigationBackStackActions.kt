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

package com.d4rk.android.libs.apptoolkit.navigation.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.d4rk.android.libs.apptoolkit.navigation.models.NavigationDestination
import com.d4rk.android.libs.apptoolkit.navigation.models.isTopLevel

fun <T : NavigationDestination> SnapshotStateList<T>.navigateTopLevel(
    destination: T,
) {
    check(destination.isTopLevel) {
        "Only top-level destinations can be used with navigateTopLevel()."
    }

    while (lastOrNull()?.isTopLevel == false) {
        removeLastOrNull()
    }

    if (lastOrNull() != destination) {
        add(destination)
    }
}

fun <T : NavigationDestination> SnapshotStateList<T>.navigateSingleTop(
    destination: T,
) {
    if (lastOrNull() != destination) {
        add(destination)
    }
}

fun <T> SnapshotStateList<T>.navigateBack(onFinish: () -> Unit) {
    if (size > 1) {
        removeLastOrNull()
    } else {
        onFinish()
    }
}
