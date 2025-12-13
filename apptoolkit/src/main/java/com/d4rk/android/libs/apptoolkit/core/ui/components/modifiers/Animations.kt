package com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.domain.model.animations.button.ButtonState
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import kotlinx.coroutines.delay
import kotlin.math.min

/**
 * A modifier that adds a bounce effect to a composable when it's clicked.
 *
 * This modifier uses a scale animation to simulate a "bounce" effect when the composable
 * is pressed. The animation is only applied if bouncy buttons are enabled in the [CommonDataStore] and
 * animationEnabled is true.
 *
 * @param animationEnabled Whether the animation is enabled. Default is true.
 * @return A [Modifier] that applies the bounce effect on click.
 */
@Composable
fun Modifier.bounceClick(
    animationEnabled : Boolean = true ,
) : Modifier = composed {
    var buttonState : ButtonState by remember { mutableStateOf(value = ButtonState.Idle) }
    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = CommonDataStore.getInstance(context = context)
    val bouncyButtonsEnabled : Boolean by dataStore.bouncyButtons.collectAsStateWithLifecycle(initialValue = true)
    val scale : Float by animateFloatAsState(
        if (buttonState == ButtonState.Pressed && animationEnabled && bouncyButtonsEnabled) 0.96f else 1f , label = "Button Press Scale Animation"
    )

    if (bouncyButtonsEnabled) {
        return@composed this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(key1 = buttonState) {
                awaitPointerEventScope {
                    buttonState =
                        if (buttonState == ButtonState.Pressed) { // FIXME: Assigned value is never read
                            waitForUpOrCancellation()
                            ButtonState.Idle
                        } else {
                            awaitFirstDown(requireUnconsumed = false)
                            ButtonState.Pressed
                        }
                }
            }
    }
    else {
        return@composed this
    }
}

/**
 * Animates the visibility of a composable with a fade and vertical offset animation.
 *
 * The composable will fade and slide into place the first time it enters the
 * composition. The animation for each item can be staggered by providing an
 * [index]. After the initial animation runs, the composable remains visible even
 * if it leaves and re-enters the composition.
 *
 * @param index Used to stagger the start time of the animation for items in a
 * list or grid.
 * @param invisibleOffsetY The vertical offset in pixels applied before the
 * animation starts. Defaults to 50.
 * @param animationDuration Duration of the fade/offset animation in
 * milliseconds. Defaults to 300.
 * @param staggerDelay Amount of delay in milliseconds per [index] before the
 * animation starts. Defaults to 64.
 */
fun Modifier.animateVisibility(
    index : Int = 0 ,
    invisibleOffsetY : Int = 50 ,
    animationDuration : Int = 300 ,
    staggerDelay : Int = 64 ,
    maxStaggeredItems : Int = 20 ,
) = composed {
    var visible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!visible) {
            val delayMillis : Int = min(index , maxStaggeredItems) * staggerDelay
            delay(timeMillis = delayMillis.toLong())
            visible = true
        }
    }

    val alpha : State<Float> = animateFloatAsState(
        targetValue = if (visible) 1f else 0f ,
        animationSpec = tween(durationMillis = animationDuration) ,
        label = "Alpha"
    )

    val offsetState : State<Float> = animateFloatAsState(
        targetValue = if (visible) 0f else invisibleOffsetY.toFloat() ,
        animationSpec = tween(durationMillis = animationDuration) ,
        label = "OffsetY"
    )

    this
        .offset {
            IntOffset(x = 0, y = offsetState.value.toInt())
        }
        .graphicsLayer {
            this.alpha = alpha.value
        }
}