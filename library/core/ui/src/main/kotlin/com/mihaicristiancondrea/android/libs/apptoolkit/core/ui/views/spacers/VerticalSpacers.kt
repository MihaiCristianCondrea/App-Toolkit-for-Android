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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing

/** Vertical spacer with an explicit [height], such as any token from [MaterialTheme.spacing]. */
@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraTiny` step (2dp by default). */
@Composable
fun ExtraTinyVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraTiny))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraSmall` step (4dp by default). */
@Composable
fun ExtraSmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraSmallIncreased` step (6dp by default). */
@Composable
fun ExtraSmallIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmallIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `small` step (8dp by default). */
@Composable
fun SmallVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `smallIncreased` step (10dp by default). */
@Composable
fun SmallIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `medium` step (12dp by default). */
@Composable
fun MediumVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `mediumIncreased` step (14dp by default). */
@Composable
fun MediumIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `large` step (16dp by default). */
@Composable
fun LargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `largeIncreased` step (20dp by default). */
@Composable
fun LargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.largeIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraLargeCompact` step (24dp by default). */
@Composable
fun ExtraLargeCompactVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLargeCompact))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraLarge` step (28dp by default). */
@Composable
fun ExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraLargeIncreased` step (32dp by default). */
@Composable
fun ExtraLargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLargeIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraExtraLargeCompact` step (40dp by default). */
@Composable
fun ExtraExtraLargeCompactVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraExtraLargeCompact))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraExtraLarge` step (48dp by default). */
@Composable
fun ExtraExtraLargeVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraExtraLarge))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraExtraLargeIncreased` step (56dp by default). */
@Composable
fun ExtraExtraLargeIncreasedVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraExtraLargeIncreased))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `huge` step (64dp by default). */
@Composable
fun HugeVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.huge))
}

/** Vertical spacer using [MaterialTheme.spacing]'s `extraHuge` step (72dp by default). */
@Composable
fun ExtraHugeVerticalSpacer() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraHuge))
}
