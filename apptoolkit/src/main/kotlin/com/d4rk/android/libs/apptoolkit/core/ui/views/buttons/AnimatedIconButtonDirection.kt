package com.d4rk.android.libs.apptoolkit.core.ui.views.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.core.ui.model.analytics.Ga4EventData
import com.d4rk.android.libs.apptoolkit.core.ui.views.analytics.logGa4Event

/**
 * An animated button that slides in and out horizontally with a fade effect.
 *
 * This composable provides an animated button that appears and disappears with a slide and fade animation.
 * The animation direction (from the left or right) and duration can be customized.
 *
 * @param modifier Modifier to be applied to the button.
 * @param visible Controls the visibility of the button. If true, the button will be visible, otherwise it will be hidden.
 * @param icon The icon to display within the button.
 * @param contentDescription The content description for the icon, used for accessibility.
 * @param onClick The callback that will be invoked when the button is clicked.
 * @param durationMillis The duration of the animation in milliseconds. Defaults to 500ms.
 * @param autoAnimate If true, the button will automatically animate in when `visible` is true.
 *                    If false, the animation will not be triggered automatically and will only occur when the visibility state changes. Defaults to true.
 * @param feedback The feedback configuration for sound and haptics.
 * @param fromRight If true, the button will slide in from the right and slide out to the right.
 *                  If false, the button will slide in from the left and slide out to the left. Defaults to false.
 * @param firebaseController Optional Firebase controller used to log GA4 events.
 * @param ga4Event Optional GA4 event data to log on click.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedIconButtonDirection(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    durationMillis: Int = 500,
    autoAnimate: Boolean = true,
    feedback: ButtonFeedback = ButtonFeedback(),
    fromRight: Boolean = false,
    firebaseController: FirebaseController? = null,
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
        enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)) + slideInHorizontally(
            initialOffsetX = { if (fromRight) it else -it },
            animationSpec = tween(durationMillis = durationMillis)
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = durationMillis)) + slideOutHorizontally(
            targetOffsetX = { if (fromRight) it else -it },
            animationSpec = tween(durationMillis = durationMillis)
        )
    ) {
        IconOnlyButton(
            modifier = modifier,
            onClick = onClick,
            enabled = true,
            iconContentDescription = contentDescription,
            vectorIcon = icon,
            feedback = feedback,
            firebaseController = firebaseController,
            ga4Event = ga4Event,
            style = IconOnlyButtonStyle.Standard,
        )
    }
}
