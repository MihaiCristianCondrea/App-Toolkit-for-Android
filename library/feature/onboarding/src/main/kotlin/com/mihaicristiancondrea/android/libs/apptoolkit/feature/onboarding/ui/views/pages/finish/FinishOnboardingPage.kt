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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.finish

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.isSystemAnimationDisabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.effects.CelebrationConfetti
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeIncreasedVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.utils.FinalOnboardingKonfettiState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Final onboarding confirmation page with a completion icon, summary copy, and one-time konfetti.
 *
 * @param isSelected Whether this page is currently selected in the onboarding pager. Animations
 * and konfetti start only once selected, and konfetti is skipped when system animations are off.
 */
@Composable
fun FinishOnboardingPage(isSelected: Boolean = true) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val iconVisible = remember { mutableStateOf(value = false) }
    val showKonfetti = remember { mutableStateOf(value = false) }

    val transition = updateTransition(
        targetState = iconVisible.value,
        label = "FinalOnboardingIconTransition",
    )

    val iconSettleScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200, delayMillis = 400) },
        label = "Icon Settle Scale",
    ) { visible -> if (visible) 1f else 1.2f }

    val iconAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Icon Alpha",
    ) { visible -> if (visible) 1f else 0f }

    val iconTranslateY by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 400, delayMillis = 200) },
        label = "Icon TranslateY",
    ) { visible -> if (visible) 0f else 30f }

    LaunchedEffect(key1 = isSelected) {
        if (!isSelected) return@LaunchedEffect
        iconVisible.value = true

        if (
            !FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally &&
            !context.isSystemAnimationDisabled()
        ) {
            delay(duration = 300.milliseconds)
            showKonfetti.value = true
            FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally = true
            delay(duration = 4_000.milliseconds)
            showKonfetti.value = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(
                    horizontal = MaterialTheme.spacing.extraLargeCompact,
                    vertical = MaterialTheme.spacing.extraLargeIncreased,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = stringResource(id = R.string.onboarding_complete_icon_desc),
                modifier = Modifier
                    .size(size = 80.dp)
                    .graphicsLayer {
                        scaleX = iconSettleScale
                        scaleY = iconSettleScale
                        translationY = iconTranslateY
                        alpha = iconAlpha
                    },
                tint = MaterialTheme.colorScheme.primary,
            )

            ExtraLargeIncreasedVerticalSpacer()

            Text(
                text = stringResource(id = R.string.onboarding_final_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            LargeVerticalSpacer()

            Text(
                text = stringResource(id = R.string.onboarding_final_description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
            )
        }

        if (showKonfetti.value) {
            CelebrationConfetti(modifier = Modifier.fillMaxSize())
        }
    }
}

