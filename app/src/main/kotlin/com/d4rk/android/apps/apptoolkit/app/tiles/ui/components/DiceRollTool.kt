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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun DiceRollTool() {
    DiceRollerApp()
}

@Composable
fun DiceRollerApp() {
    val rollDice: () -> Int = remember {
        { Random.nextInt(from = 1, until = 7) }
    }

    DiceWithButtonAndProceduralDice(
        rollDice = rollDice
    )
}

@Composable
fun DiceWithButtonAndProceduralDice(
    modifier: Modifier = Modifier,
    rollDice: () -> Int,
) {
    var displayedResult: Int by remember { mutableIntStateOf(1) }
    var rollRequest: Int by remember { mutableIntStateOf(0) }
    var rolling: Boolean by remember { mutableStateOf(false) }

    val rotationX = remember { Animatable(SettledRotationX) }
    val rotationY = remember { Animatable(SettledRotationY) }
    val rotationZ = remember { Animatable(SettledRotationZ) }
    val scale = remember { Animatable(1f) }
    val translationY = remember { Animatable(0f) }

    LaunchedEffect(rollRequest) {
        if (rollRequest == 0) return@LaunchedEffect

        rolling = true

        val finalResult: Int = rollDice().coerceIn(
            minimumValue = MinDiceValue,
            maximumValue = MaxDiceValue,
        )

        coroutineScope {
            launch {
                repeat(times = 14) { index: Int ->
                    displayedResult = Random.nextInt(
                        from = MinDiceValue,
                        until = MaxDiceValue + 1,
                    )

                    delay(timeMillis = 42L + index * 3L)
                }

                displayedResult = finalResult
            }

            launch {
                rotationX.animateTo(
                    targetValue = nextSettledAngle(
                        currentValue = rotationX.value,
                        extraTurns = 3,
                        settledDegrees = SettledRotationX,
                    ),
                    animationSpec = tween(
                        durationMillis = RollDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }

            launch {
                rotationY.animateTo(
                    targetValue = nextSettledAngle(
                        currentValue = rotationY.value,
                        extraTurns = 4,
                        settledDegrees = SettledRotationY,
                    ),
                    animationSpec = tween(
                        durationMillis = RollDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }

            launch {
                rotationZ.animateTo(
                    targetValue = nextSettledAngle(
                        currentValue = rotationZ.value,
                        extraTurns = 5,
                        settledDegrees = SettledRotationZ,
                    ),
                    animationSpec = tween(
                        durationMillis = RollDurationMillis,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }

            launch {
                scale.animateTo(
                    targetValue = 0.88f,
                    animationSpec = tween(durationMillis = 140),
                )

                scale.animateTo(
                    targetValue = 1.06f,
                    animationSpec = tween(
                        durationMillis = 560,
                        easing = FastOutSlowInEasing,
                    ),
                )

                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 180),
                )
            }

            launch {
                translationY.animateTo(
                    targetValue = -88f,
                    animationSpec = tween(
                        durationMillis = 280,
                        easing = FastOutSlowInEasing,
                    ),
                )

                translationY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 540,
                        easing = FastOutSlowInEasing,
                    ),
                )

                translationY.animateTo(
                    targetValue = -7f,
                    animationSpec = tween(durationMillis = 70),
                )

                translationY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 90),
                )
            }
        }

        displayedResult = finalResult
        rolling = false
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialProceduralDice3D(
            value = displayedResult,
            rotationX = rotationX.value,
            rotationY = rotationY.value,
            rotationZ = rotationZ.value,
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.translationY = translationY.value
                },
        )

        Spacer(modifier = Modifier.height(SizeConstants.ExtraLargeSize))

        Button(
            onClick = {
                if (!rolling) {
                    rollRequest++
                }
            },
            enabled = !rolling,
        ) {
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.size(SizeConstants.SmallSize))

            Text(text = stringResource(id = R.string.tool_dice_roll_action))
        }

        Spacer(modifier = Modifier.height(SizeConstants.MediumSize))

        Text(
            text = if (rolling) {
                stringResource(id = R.string.tool_dice_roll_waiting)
            } else {
                displayedResult.toString()
            },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun MaterialProceduralDice3D(
    value: Int,
    rotationX: Float,
    rotationY: Float,
    rotationZ: Float,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    val colors = ProceduralDiceColors(
        frontFaceColor = colorScheme.tertiaryContainer,
        sideFaceColor = colorScheme.secondaryContainer,
        topFaceColor = colorScheme.primaryContainer,
        edgeColor = colorScheme.outline,
        pipColor = colorScheme.onPrimaryContainer,
    )

    // Using `remember` to hoist Paints out of DrawScope
    val fillPaint = remember {
        Paint().apply {
            style = PaintingStyle.Fill
            isAntiAlias = true
        }
    }

    val strokePaint = remember {
        Paint().apply {
            style = PaintingStyle.Stroke
            strokeJoin = StrokeJoin.Round
            strokeCap = StrokeCap.Round
            isAntiAlias = true
        }
    }

    Canvas(modifier = modifier) {
        drawProceduralDiceCube(
            value = value,
            rotationX = rotationX,
            rotationY = rotationY,
            rotationZ = rotationZ,
            colors = colors,
            fillPaint = fillPaint,
            strokePaint = strokePaint,
        )
    }
}

private fun DrawScope.drawProceduralDiceCube(
    value: Int,
    rotationX: Float,
    rotationY: Float,
    rotationZ: Float,
    colors: ProceduralDiceColors,
    fillPaint: Paint,
    strokePaint: Paint,
) {
    val safeValue: Int = value.coerceIn(
        minimumValue = MinDiceValue,
        maximumValue = MaxDiceValue,
    )

    val center = Offset(
        x = size.width / 2f,
        y = size.height / 2f,
    )

    val cubeHalfSize: Float = min(
        a = size.width,
        b = size.height,
    ) * 0.34f

    val rotationXRadians: Double = rotationX.toRadians()
    val rotationYRadians: Double = rotationY.toRadians()
    val rotationZRadians: Double = rotationZ.toRadians()

    val faceNumbers: Map<DiceCubeFace, Int> = createFaceNumbers(
        resultValue = safeValue,
    )

    val renderedFaces: List<RenderedDiceFace> = DiceFaceDefinitions
        .map { faceDefinition: DiceFaceDefinition ->
            val rotatedVertices: List<Vec3> = faceDefinition.vertices.map { vertex: Vec3 ->
                vertex.rotate(
                    rotationX = rotationXRadians,
                    rotationY = rotationYRadians,
                    rotationZ = rotationZRadians,
                )
            }

            val rotatedNormal: Vec3 = faceDefinition.normal.rotate(
                rotationX = rotationXRadians,
                rotationY = rotationYRadians,
                rotationZ = rotationZRadians,
            )

            RenderedDiceFace(
                face = faceDefinition.face,
                corners = rotatedVertices.map { vertex: Vec3 ->
                    vertex.project(
                        center = center,
                        cubeHalfSize = cubeHalfSize,
                    )
                },
                facingCamera = rotatedNormal.z,
                depth = rotatedVertices.sumOf { vertex: Vec3 ->
                    vertex.z.toDouble()
                }.toFloat() / rotatedVertices.size,
                number = faceNumbers.getValue(faceDefinition.face),
            )
        }
        .filter { face: RenderedDiceFace ->
            face.facingCamera > 0f
        }
        .sortedBy { face: RenderedDiceFace ->
            face.depth
        }

    val edgeWidth: Float = cubeHalfSize * 0.08f // Adjusted thickness relative to curves
    val cornerRadius: Float = cubeHalfSize * 0.48f // Applied to corner path effect for curved edges
    val cornerEffect = PathEffect.cornerPathEffect(cornerRadius)

    fillPaint.pathEffect = cornerEffect
    strokePaint.pathEffect = cornerEffect
    strokePaint.strokeWidth = edgeWidth

    renderedFaces.forEach { face: RenderedDiceFace ->
        val path = Path().apply {
            moveTo(
                x = face.corners[0].x,
                y = face.corners[0].y,
            )

            for (index in 1 until face.corners.size) {
                lineTo(
                    x = face.corners[index].x,
                    y = face.corners[index].y,
                )
            }

            close()
        }

        drawIntoCanvas { canvas ->
            fillPaint.color = colors.colorFor(face.face)
            canvas.drawPath(path, fillPaint)

            strokePaint.color = colors.edgeColor
            canvas.drawPath(path, strokePaint)
        }

        drawProjectedPips(
            corners = face.corners,
            value = face.number,
            pipColor = colors.pipColor,
        )
    }
}

private fun DrawScope.drawProjectedPips(
    corners: List<Offset>,
    value: Int,
    pipColor: Color,
) {
    pipCentersFor(value = value).forEach { center: Offset ->
        drawProjectedPip(
            corners = corners,
            center = center,
            radius = PipRadius,
            color = pipColor,
        )
    }
}

private fun DrawScope.drawProjectedPip(
    corners: List<Offset>,
    center: Offset,
    radius: Float,
    color: Color,
) {
    val path = Path()

    repeat(times = PipSegmentCount) { index: Int ->
        val angle: Double = (index.toDouble() / PipSegmentCount.toDouble()) * FullCircleRadians

        val pipPoint = bilinear(
            corners = corners,
            u = center.x + cos(angle).toFloat() * radius,
            v = center.y + sin(angle).toFloat() * radius,
        )

        if (index == 0) {
            path.moveTo(
                x = pipPoint.x,
                y = pipPoint.y,
            )
        } else {
            path.lineTo(
                x = pipPoint.x,
                y = pipPoint.y,
            )
        }
    }

    path.close()

    drawPath(
        path = path,
        color = color,
    )
}

private fun pipCentersFor(value: Int): List<Offset> {
    val topLeft = Offset(x = 0.29f, y = 0.29f)
    val topRight = Offset(x = 0.71f, y = 0.29f)
    val center = Offset(x = 0.5f, y = 0.5f)
    val middleLeft = Offset(x = 0.29f, y = 0.5f)
    val middleRight = Offset(x = 0.71f, y = 0.5f)
    val bottomLeft = Offset(x = 0.29f, y = 0.71f)
    val bottomRight = Offset(x = 0.71f, y = 0.71f)

    return when (value) {
        1 -> listOf(center)

        2 -> listOf(
            topLeft,
            bottomRight,
        )

        3 -> listOf(
            topLeft,
            center,
            bottomRight,
        )

        4 -> listOf(
            topLeft,
            topRight,
            bottomLeft,
            bottomRight,
        )

        5 -> listOf(
            topLeft,
            topRight,
            center,
            bottomLeft,
            bottomRight,
        )

        else -> listOf(
            topLeft,
            topRight,
            middleLeft,
            middleRight,
            bottomLeft,
            bottomRight,
        )
    }
}

private fun createFaceNumbers(resultValue: Int): Map<DiceCubeFace, Int> {
    val oppositePairs = listOf(
        1 to 6,
        2 to 5,
        3 to 4,
    )

    val resultPair = oppositePairs.first { pair: Pair<Int, Int> ->
        pair.first == resultValue || pair.second == resultValue
    }

    val remainingPairs = oppositePairs.filterNot { pair: Pair<Int, Int> ->
        pair == resultPair
    }

    val bottomValue = if (resultPair.first == resultValue) {
        resultPair.second
    } else {
        resultPair.first
    }

    // Map resultValue to Top so it's visible from above
    return mapOf(
        DiceCubeFace.Top to resultValue,
        DiceCubeFace.Bottom to bottomValue,
        DiceCubeFace.Front to remainingPairs[0].first,
        DiceCubeFace.Back to remainingPairs[0].second,
        DiceCubeFace.Right to remainingPairs[1].first,
        DiceCubeFace.Left to remainingPairs[1].second,
    )
}

private fun ProceduralDiceColors.colorFor(face: DiceCubeFace): Color {
    return when (face) {
        DiceCubeFace.Front,
        DiceCubeFace.Back -> frontFaceColor

        DiceCubeFace.Left,
        DiceCubeFace.Right -> sideFaceColor

        DiceCubeFace.Top,
        DiceCubeFace.Bottom -> topFaceColor
    }
}

private fun Vec3.rotate(
    rotationX: Double,
    rotationY: Double,
    rotationZ: Double,
): Vec3 {
    val cosX = cos(rotationX).toFloat()
    val sinX = sin(rotationX).toFloat()
    val cosY = cos(rotationY).toFloat()
    val sinY = sin(rotationY).toFloat()
    val cosZ = cos(rotationZ).toFloat()
    val sinZ = sin(rotationZ).toFloat()

    var rotatedX = x
    var rotatedY = y * cosX - z * sinX
    var rotatedZ = y * sinX + z * cosX

    val afterYRotationX = rotatedX * cosY + rotatedZ * sinY
    val afterYRotationZ = -rotatedX * sinY + rotatedZ * cosY

    rotatedX = afterYRotationX
    rotatedZ = afterYRotationZ

    val afterZRotationX = rotatedX * cosZ - rotatedY * sinZ
    val afterZRotationY = rotatedX * sinZ + rotatedY * cosZ

    rotatedX = afterZRotationX
    rotatedY = afterZRotationY

    return Vec3(
        x = rotatedX,
        y = rotatedY,
        z = rotatedZ,
    )
}

private fun Vec3.project(
    center: Offset,
    cubeHalfSize: Float,
): Offset {
    val perspective: Float = CameraDistance / (CameraDistance - z)

    return Offset(
        x = center.x + x * cubeHalfSize * perspective,
        y = center.y + y * cubeHalfSize * perspective,
    )
}

private fun bilinear(
    corners: List<Offset>,
    u: Float,
    v: Float,
): Offset {
    val top = lerp(
        start = corners[0],
        end = corners[1],
        fraction = u,
    )

    val bottom = lerp(
        start = corners[3],
        end = corners[2],
        fraction = u,
    )

    return lerp(
        start = top,
        end = bottom,
        fraction = v,
    )
}

private fun lerp(
    start: Offset,
    end: Offset,
    fraction: Float,
): Offset {
    return Offset(
        x = start.x + (end.x - start.x) * fraction,
        y = start.y + (end.y - start.y) * fraction,
    )
}

private fun nextSettledAngle(
    currentValue: Float,
    extraTurns: Int,
    settledDegrees: Float,
): Float {
    val normalizedCurrent: Float = normalizedDegrees(currentValue)
    val deltaToSettled: Float = normalizedDegrees(settledDegrees - normalizedCurrent)

    return currentValue + extraTurns * FullRotationDegrees + deltaToSettled
}

private fun normalizedDegrees(value: Float): Float {
    return ((value % FullRotationDegrees) + FullRotationDegrees) % FullRotationDegrees
}

private fun Float.toRadians(): Double {
    return this.toDouble() * PI / 180.0
}

private enum class DiceCubeFace {
    Front,
    Back,
    Left,
    Right,
    Top,
    Bottom,
}

private data class ProceduralDiceColors(
    val frontFaceColor: Color,
    val sideFaceColor: Color,
    val topFaceColor: Color,
    val edgeColor: Color,
    val pipColor: Color,
)

private data class Vec3(
    val x: Float,
    val y: Float,
    val z: Float,
)

private data class DiceFaceDefinition(
    val face: DiceCubeFace,
    val vertices: List<Vec3>,
    val normal: Vec3,
)

private data class RenderedDiceFace(
    val face: DiceCubeFace,
    val corners: List<Offset>,
    val facingCamera: Float,
    val depth: Float,
    val number: Int,
)

private val DiceFaceDefinitions: List<DiceFaceDefinition> = listOf(
    DiceFaceDefinition(
        face = DiceCubeFace.Front,
        vertices = listOf(
            Vec3(x = -1f, y = -1f, z = 1f),
            Vec3(x = 1f, y = -1f, z = 1f),
            Vec3(x = 1f, y = 1f, z = 1f),
            Vec3(x = -1f, y = 1f, z = 1f),
        ),
        normal = Vec3(x = 0f, y = 0f, z = 1f),
    ),
    DiceFaceDefinition(
        face = DiceCubeFace.Back,
        vertices = listOf(
            Vec3(x = 1f, y = -1f, z = -1f),
            Vec3(x = -1f, y = -1f, z = -1f),
            Vec3(x = -1f, y = 1f, z = -1f),
            Vec3(x = 1f, y = 1f, z = -1f),
        ),
        normal = Vec3(x = 0f, y = 0f, z = -1f),
    ),
    DiceFaceDefinition(
        face = DiceCubeFace.Right,
        vertices = listOf(
            Vec3(x = 1f, y = -1f, z = 1f),
            Vec3(x = 1f, y = -1f, z = -1f),
            Vec3(x = 1f, y = 1f, z = -1f),
            Vec3(x = 1f, y = 1f, z = 1f),
        ),
        normal = Vec3(x = 1f, y = 0f, z = 0f),
    ),
    DiceFaceDefinition(
        face = DiceCubeFace.Left,
        vertices = listOf(
            Vec3(x = -1f, y = -1f, z = -1f),
            Vec3(x = -1f, y = -1f, z = 1f),
            Vec3(x = -1f, y = 1f, z = 1f),
            Vec3(x = -1f, y = 1f, z = -1f),
        ),
        normal = Vec3(x = -1f, y = 0f, z = 0f),
    ),
    DiceFaceDefinition(
        face = DiceCubeFace.Top,
        vertices = listOf(
            Vec3(x = -1f, y = -1f, z = -1f),
            Vec3(x = 1f, y = -1f, z = -1f),
            Vec3(x = 1f, y = -1f, z = 1f),
            Vec3(x = -1f, y = -1f, z = 1f),
        ),
        normal = Vec3(x = 0f, y = -1f, z = 0f),
    ),
    DiceFaceDefinition(
        face = DiceCubeFace.Bottom,
        vertices = listOf(
            Vec3(x = -1f, y = 1f, z = 1f),
            Vec3(x = 1f, y = 1f, z = 1f),
            Vec3(x = 1f, y = 1f, z = -1f),
            Vec3(x = -1f, y = 1f, z = -1f),
        ),
        normal = Vec3(x = 0f, y = 1f, z = 0f),
    ),
)

private const val MinDiceValue = 1
private const val MaxDiceValue = 6

// Setting `SettledRotationX` to a steep -80f to give it a much clearer "top-down" view.
private const val SettledRotationX = -90f
private const val SettledRotationY = 0f
private const val SettledRotationZ = 0f

private const val RollDurationMillis = 960
private const val FullRotationDegrees = 360f

private const val CameraDistance = 3.2f
private const val PipRadius = 0.064f
private const val PipSegmentCount = 18

private const val FullCircleRadians = PI * 2.0