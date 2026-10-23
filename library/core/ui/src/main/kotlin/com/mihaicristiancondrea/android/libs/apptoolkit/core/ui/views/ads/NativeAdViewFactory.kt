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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Interface for creating the view hierarchy for different native ad presentations.
 *
 * Use this to provide custom ad view structures from the host app while keeping the
 * ad lifecycle and binding logic in the toolkit.
 */
interface NativeAdViewFactory {
    fun createViewHolder(context: Context, presentation: NativeAdPresentation): NativeAdViewHolder
}

val LocalNativeAdViewFactory = staticCompositionLocalOf<NativeAdViewFactory> {
    DefaultNativeAdViewFactory()
}
