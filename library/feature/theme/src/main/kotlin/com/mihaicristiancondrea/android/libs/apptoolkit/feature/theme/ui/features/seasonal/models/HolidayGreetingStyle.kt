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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.features.seasonal.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * One season's greeting: its art, words and colours. The edges are the colours along the top and
 * bottom of the art, so the card continues it seamlessly; every text and button colour meets
 * WCAG AA against what it is drawn over.
 */
@Immutable
internal data class HolidayGreetingStyle(
    @param:DrawableRes val illustration: Int,
    @param:StringRes val title: Int,
    @param:StringRes val message: Int,
    @param:StringRes val accept: Int,
    val topEdge: Color,
    val bottomEdge: Color,
    val topScrim: Color,
    val bottomScrim: Color,
    val titleColor: Color,
    val messageColor: Color,
    val primaryContainer: Color,
    val primaryContent: Color,
    val secondaryContent: Color,
    val secondaryOutline: Color,
)
