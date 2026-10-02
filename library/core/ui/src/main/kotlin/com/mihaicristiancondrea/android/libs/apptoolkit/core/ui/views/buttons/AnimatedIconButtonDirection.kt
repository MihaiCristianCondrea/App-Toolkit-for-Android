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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.analytics.Ga4EventData

/**
 * Icon action button with slide, fade, and scale visibility transitions. Icon replacements
 * crossfade in place without replaying the entrance. Click feedback, playback, and analytics
 * follow [GeneralButton].
 *
 * @param autoAnimate Reveals a hidden button when [visible] becomes `true`. Disabling this
 * leaves a hidden button hidden.
 * @param fromRight Selects the physical right edge; `false` selects the left edge.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedIconButtonDirection(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    enabled: Boolean = true,
    icon: ToolkitIcon,
    contentDescription: String? = null,
    onClick: () -> Unit,
    durationMillis: Int = 500,
    autoAnimate: Boolean = true,
    feedback: ButtonFeedback = ButtonFeedback(),
    fromRight: Boolean = false,
    iconSize: Dp = SizeConstants.TwentyFourSize,
    ga4Event: Ga4EventData? = null,
) {
    val animatedVisibility: MutableState<Boolean> =
        rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(visible) {
        if (autoAnimate && visible) {
            animatedVisibility.value = true
        } else if (!visible) {
            animatedVisibility.value = false
        }
    }

    AnimatedVisibility(
        visible = animatedVisibility.value && visible,
        enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)) +
                slideInHorizontally(
                    initialOffsetX = { if (fromRight) it else -it },
                    animationSpec = tween(durationMillis = durationMillis)
                ) +
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = durationMillis)
                ),
        exit = fadeOut(animationSpec = tween(durationMillis = durationMillis)) +
                slideOutHorizontally(
                    targetOffsetX = { if (fromRight) it else -it },
                    animationSpec = tween(durationMillis = durationMillis)
                ) +
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(durationMillis = durationMillis)
                )
    ) {
        AnimatedContent(
            targetState = icon to contentDescription,
            transitionSpec = {
                (fadeIn(tween(220, delayMillis = 90)) + scaleIn(tween(220, delayMillis = 90), initialScale = 0.92f))
                    .togetherWith(fadeOut(tween(90)))
            },
            label = "AnimatedIconButtonDirectionIcon",
        ) { (targetIcon, targetDescription) ->
            GeneralButton(
                modifier = modifier,
                onClick = onClick,
                enabled = enabled,
                contentDescription = targetDescription,
                icon = targetIcon,
                iconSize = iconSize,
                feedback = feedback,
                ga4Event = ga4Event,
                style = GeneralButtonStyle.Text,
            )
        }
    }
}
