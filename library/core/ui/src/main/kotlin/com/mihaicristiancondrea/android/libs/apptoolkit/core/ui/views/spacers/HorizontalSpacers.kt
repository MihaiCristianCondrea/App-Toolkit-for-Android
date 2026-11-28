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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing

/** Horizontal spacer with an explicit [width], such as any token from [MaterialTheme.spacing]. */
@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraTiny` step (2dp by default). */
@Composable
fun ExtraTinyHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraTiny))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraSmall` step (4dp by default). */
@Composable
fun ExtraSmallHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraSmallIncreased` step (6dp by default). */
@Composable
fun ExtraSmallIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmallIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `small` step (8dp by default). */
@Composable
fun SmallHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `smallIncreased` step (10dp by default). */
@Composable
fun SmallIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `medium` step (12dp by default). */
@Composable
fun MediumHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `mediumIncreased` step (14dp by default). */
@Composable
fun MediumIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.mediumIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `large` step (16dp by default). */
@Composable
fun LargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `largeIncreased` step (20dp by default). */
@Composable
fun LargeIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.largeIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraLargeCompact` step (24dp by default). */
@Composable
fun ExtraLargeCompactHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLargeCompact))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraLarge` step (28dp by default). */
@Composable
fun ExtraLargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLarge))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraLargeIncreased` step (32dp by default). */
@Composable
fun ExtraLargeIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraLargeIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraExtraLargeCompact` step (40dp by default). */
@Composable
fun ExtraExtraLargeCompactHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraExtraLargeCompact))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraExtraLarge` step (48dp by default). */
@Composable
fun ExtraExtraLargeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraExtraLarge))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraExtraLargeIncreased` step (56dp by default). */
@Composable
fun ExtraExtraLargeIncreasedHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraExtraLargeIncreased))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `huge` step (64dp by default). */
@Composable
fun HugeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.huge))
}

/** Horizontal spacer using [MaterialTheme.spacing]'s `extraHuge` step (72dp by default). */
@Composable
fun ExtraHugeHorizontalSpacer() {
    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraHuge))
}
