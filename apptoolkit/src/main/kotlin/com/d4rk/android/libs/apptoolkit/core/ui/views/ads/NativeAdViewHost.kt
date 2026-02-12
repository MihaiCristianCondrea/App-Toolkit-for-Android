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

package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * A small helper to inflate and remember a [NativeAdView] inside Compose.
 *
 * @param modifier Modifiers applied to the hosting view.
 * @param layoutResId The XML layout resource used to inflate the [NativeAdView].
 * @param onNativeAdViewReady Called after the view is created or updated so the caller can store it.
 * @param onUpdate Called on every recomposition to apply the latest UI updates to the view.
 */
@Composable
@UiComposable
internal fun NativeAdViewHost(
    modifier: Modifier = Modifier,
    layoutResId: Int,
    onNativeAdViewReady: (NativeAdView) -> Unit,
    onUpdate: (NativeAdView) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            (LayoutInflater.from(context).inflate(layoutResId, null) as NativeAdView)
                .also(onNativeAdViewReady)
        },
        update = { view ->
            onNativeAdViewReady(view)
            onUpdate(view)
        }
    )
}
