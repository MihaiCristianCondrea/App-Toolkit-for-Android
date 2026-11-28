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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

/**
 * Draws a confetti burst and falling confetti. The caller controls when the overlay
 * enters and leaves composition and whether to show it when system animations are disabled.
 *
 * @param modifier Modifier for the confetti canvas.
 * @param burstCount Number of particles emitted in the initial burst.
 * @param rainParticlesPerSecond Number of falling particles emitted per second.
 */
@Composable
fun CelebrationConfetti(
    modifier: Modifier = Modifier,
    burstCount: Int = 100,
    rainParticlesPerSecond: Int = 60,
) {
    KonfettiView(modifier = modifier, parties = remember(
        key1 = burstCount,
        key2 = rainParticlesPerSecond
    ) {
        listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = Spread.ROUND,
                position = Position.Relative(x = 0.5, y = 0.3),
                emitter = Emitter(duration = 200, timeUnit = TimeUnit.MILLISECONDS)
                    .max(amount = burstCount),
            ),
            Party(
                emitter = Emitter(duration = 3, timeUnit = TimeUnit.SECONDS)
                    .perSecond(amount = rainParticlesPerSecond),
                angle = Angle.BOTTOM,
                spread = Spread.SMALL,
                speed = 5f,
                maxSpeed = 15f,
                timeToLive = 3_000L,
                position = Position.Relative(x = 0.0, y = 0.0)
                    .between(value = Position.Relative(x = 1.0, y = 0.0)),
            ),
        )
    })
}
