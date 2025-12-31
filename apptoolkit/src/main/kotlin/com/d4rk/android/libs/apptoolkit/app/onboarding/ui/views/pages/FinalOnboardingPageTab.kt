package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.utils.helpers.FinalOnboardingKonfettiState
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraLargeIncreasedVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun FinalOnboardingPageTab() {

    val iconVisible = remember { mutableStateOf(false) }
    val showKonfetti = remember { mutableStateOf(false) }

    val parties = remember {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = Spread.ROUND,
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(amount = 100)
        )

        val partyRain = Party(
            emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(amount = 60),
            angle = Angle.BOTTOM,
            spread = Spread.SMALL,
            speed = 5f,
            maxSpeed = 15f,
            timeToLive = 3000L,
            position = Position.Relative(x = 0.0, y = 0.0)
                .between(value = Position.Relative(x = 1.0, y = 0.0))
        )

        listOf(party, partyRain)
    }

    val transition = updateTransition(
        targetState = iconVisible.value,
        label = "FinalOnboardingIconTransition"
    )

    val iconSettleScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200, delayMillis = 400) },
        label = "Icon Settle Scale"
    ) { visible -> if (visible) 1f else 1.2f }

    val iconAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Icon Alpha"
    ) { visible -> if (visible) 1f else 0f }

    val iconTranslateY by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 400, delayMillis = 200) },
        label = "Icon TranslateY"
    ) { visible -> if (visible) 0f else 30f }

    LaunchedEffect(Unit) {
        // Always show the icon with its enter animation
        iconVisible.value = true

        if (!FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally) {
            delay(300)
            showKonfetti.value = true
            FinalOnboardingKonfettiState.hasKonfettiBeenShownGlobally = true
            delay(4_000)
            showKonfetti.value = false // optional: removes KonfettiView from composition
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = SizeConstants.MediumSize * 2,
                    vertical = SizeConstants.ExtraLargeIncreasedSize
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = stringResource(id = R.string.onboarding_complete_icon_desc),
                modifier = Modifier
                    .size(size = SizeConstants.ExtraExtraLargeSize + SizeConstants.ExtraLargeIncreasedSize)
                    .graphicsLayer {
                        scaleX = iconSettleScale
                        scaleY = iconSettleScale
                        translationY = iconTranslateY
                        alpha = iconAlpha
                    },
                tint = MaterialTheme.colorScheme.primary
            )

            ExtraLargeIncreasedVerticalSpacer()

            Text(
                text = stringResource(id = R.string.onboarding_final_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            LargeVerticalSpacer()

            Text(
                text = stringResource(id = R.string.onboarding_final_description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = SizeConstants.LargeSize)
            )
        }

        if (showKonfetti.value) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = parties
            )
        }
    }
}
