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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

@Composable
fun ExtraExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.LauncherIconSize))
}

@Composable
fun ExtraLargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraLargeIncreasedSize))
}

@Composable
fun ExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraLargeSize))
}

@Composable
fun LargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.LargeIncreasedSize))
}

@Composable
fun LargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.LargeSize))
}

@Composable
fun MediumVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.MediumSize))
}

@Composable
fun SmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.SmallSize))
}

@Composable
fun ExtraSmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraSmallSize))
}

@Composable
fun ExtraTinyVerticalSpacer() {
    Spacer(modifier = Modifier.height(height = SizeConstants.ExtraTinySize))
}

@Composable
fun NavigationBarsVerticalSpacer() {
    Spacer(modifier = Modifier.navigationBarsPadding())
}