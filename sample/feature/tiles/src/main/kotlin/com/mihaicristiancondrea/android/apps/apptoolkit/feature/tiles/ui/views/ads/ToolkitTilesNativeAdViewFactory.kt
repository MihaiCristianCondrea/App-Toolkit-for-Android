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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads

import android.content.Context
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.DefaultNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewHolder
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.roundedOutline

/** Keeps the shared Compact arrangement with the Quick Tools row's spacing and badge size. */
class ToolkitTilesNativeAdViewFactory : NativeAdViewFactory {
    private val defaultFactory = DefaultNativeAdViewFactory()

    override fun createViewHolder(context: Context, presentation: NativeAdPresentation): NativeAdViewHolder {
        val holder = defaultFactory.createViewHolder(context, presentation)
        if (presentation != NativeAdPresentation.Compact) return holder

        holder.content.setPadding(context.dp(16), context.dp(12), context.dp(16), context.dp(12))
        holder.iconFrame?.apply {
            layoutParams.width = context.dp(44)
            layoutParams.height = context.dp(44)
            outlineProvider = roundedOutline(radiusPx = context.dp(22))
        }
        holder.headline.maxLines = 2
        return holder
    }
}
