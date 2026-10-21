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
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
 * Its place in the row grows as it appears and narrows as it leaves, so the buttons beside it
 * glide over instead of jumping once it is gone. The motion follows `MaterialTheme.motionScheme`,
 * the app's motion tempo.
 *
 * @param durationMillis A fixed duration for the entrance and exit, in place of the motion
 * scheme's springs; null follows the motion tempo.
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
    durationMillis: Int? = null,
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

    val motion = MaterialTheme.motionScheme
    fun <T> spatial(): FiniteAnimationSpec<T> = durationMillis?.let { tween(durationMillis = it) } ?: motion.defaultSpatialSpec()
    fun <T> effects(): FiniteAnimationSpec<T> = durationMillis?.let { tween(durationMillis = it) } ?: motion.defaultEffectsSpec()
    val edge: Alignment.Horizontal = if (fromRight) Alignment.End else Alignment.Start

    AnimatedVisibility(
        visible = animatedVisibility.value && visible,
        enter = fadeIn(animationSpec = effects()) +
                slideInHorizontally(
                    initialOffsetX = { if (fromRight) it else -it },
                    animationSpec = spatial(),
                ) +
                scaleIn(initialScale = 0.8f, animationSpec = spatial()) +
                expandHorizontally(expandFrom = edge, animationSpec = spatial(), clip = false),
        exit = fadeOut(animationSpec = effects()) +
                slideOutHorizontally(
                    targetOffsetX = { if (fromRight) it else -it },
                    animationSpec = spatial(),
                ) +
                scaleOut(targetScale = 0.8f, animationSpec = spatial()) +
                shrinkHorizontally(shrinkTowards = edge, animationSpec = spatial(), clip = false),
    ) {
        AnimatedContent(
            targetState = icon to contentDescription,
            transitionSpec = {
                (fadeIn(motion.fastEffectsSpec()) + scaleIn(motion.fastSpatialSpec(), initialScale = 0.92f))
                    .togetherWith(fadeOut(motion.fastEffectsSpec()))
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
