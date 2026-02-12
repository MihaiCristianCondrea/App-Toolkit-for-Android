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

package com.d4rk.android.libs.apptoolkit.core.ui.views.drawable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private const val VP_W = 888f
private const val VP_H = 678f

@Composable
fun rememberPaletteImageVector(): ImageVector {
    val colorScheme = MaterialTheme.colorScheme

    // Girl - Keep these stable to maintain character identity
    val skin = Color(0xFFFAEED4)
    val hair = colorScheme.onSurfaceVariant
    val cloth = colorScheme.primary
    val legs = colorScheme.onPrimaryContainer
    val shoes = colorScheme.outline

    // Env - These will shift with the theme
    val leafDark = colorScheme.primaryContainer
    val leafShadow = colorScheme.secondary
    val branch = colorScheme.secondaryContainer
    val sun = colorScheme.tertiary
    val grass = colorScheme.surfaceVariant
    val backgroundTrees = colorScheme.surfaceContainerLowest

    return remember(
        skin, leafDark, cloth, leafShadow, hair, sun, shoes, branch
    ) {
        buildPalette(
            skin = skin,
            hand = cloth,
            leafDark = leafDark,
            cloth = cloth,
            leafShadow = leafShadow,
            hair = hair,
            sun = sun,
            shoes = shoes,
            branch = branch,
            grass = grass,
            legs = legs,
            backgroundTrees = backgroundTrees,
        )
    }
}

private fun buildPalette(
    skin: Color,
    hand: Color,
    leafDark: Color,
    cloth: Color,
    leafShadow: Color,
    hair: Color,
    sun: Color,
    shoes: Color,
    branch: Color,
    grass: Color,
    legs: Color,
    backgroundTrees: Color,
): ImageVector {
    return ImageVector.Builder(
        name = "palette",
        defaultWidth = 100.dp,
        defaultHeight = 100.dp * (VP_H / VP_W),
        viewportWidth = VP_W,
        viewportHeight = VP_H
    ).apply {
        group {
            path(fill = SolidColor(skin)) {
                moveTo(307.693f, 659.535f)
                lineTo(316.417f, 662.148f)
                lineTo(330.648f, 629.74f)
                lineTo(317.772f, 625.884f)
                lineTo(307.693f, 659.535f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hand)) {
                moveTo(306.32f, 656.02f)
                lineTo(323.502f, 661.166f)
                lineTo(323.503f, 661.166f)
                curveTo(326.407f, 662.036f, 328.846f, 664.024f, 330.284f, 666.692f)
                curveTo(331.722f, 669.361f, 332.042f, 672.491f, 331.172f, 675.395f)
                lineTo(331.065f, 675.751f)
                lineTo(302.934f, 667.325f)
                lineTo(306.32f, 656.02f)
                close()
            }
        }
        group {
            path(fill = SolidColor(skin)) {
                moveTo(390.602f, 666.663f)
                lineTo(399.153f, 663.528f)
                lineTo(391.13f, 629.055f)
                lineTo(378.51f, 633.682f)
                lineTo(390.602f, 666.663f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hand)) {
                moveTo(387.398f, 664.671f)
                lineTo(404.237f, 658.496f)
                lineTo(404.238f, 658.496f)
                curveTo(407.084f, 657.453f, 410.228f, 657.583f, 412.978f, 658.858f)
                curveTo(415.729f, 660.132f, 417.86f, 662.447f, 418.904f, 665.293f)
                lineTo(419.031f, 665.642f)
                lineTo(391.46f, 675.751f)
                lineTo(387.398f, 664.671f)
                close()
            }
        }
        group {
            path(fill = SolidColor(legs)) {
                moveTo(322.321f, 600.868f)
                lineTo(307.464f, 649.897f)
                lineTo(325.292f, 655.097f)
                lineTo(342.378f, 609.782f)
                lineTo(322.321f, 600.868f)
                close()
            }
        }
        group {
            path(fill = SolidColor(legs)) {
                moveTo(369.121f, 612.011f)
                lineTo(382.493f, 658.068f)
                lineTo(401.064f, 649.154f)
                lineTo(387.693f, 606.068f)
                lineTo(369.121f, 612.011f)
                close()
            }
        }
        group {
            path(fill = SolidColor(cloth)) {
                moveTo(362.162f, 620.318f)
                curveTo(350.27f, 620.263f, 338.406f, 619.169f, 326.705f, 617.047f)
                lineTo(326.407f, 616.987f)
                verticalLineTo(605.657f)
                lineTo(319.617f, 604.903f)
                lineTo(324.917f, 585.975f)
                curveTo(322.446f, 556.815f, 325.957f, 519.885f, 327.092f, 509.256f)
                curveTo(327.352f, 506.764f, 327.524f, 505.365f, 327.524f, 505.365f)
                lineTo(333.482f, 454.715f)
                lineTo(343.453f, 445.512f)
                lineTo(347.982f, 448.46f)
                lineTo(356.384f, 456.861f)
                curveTo(366.15f, 480.895f, 373.899f, 503.522f, 373.948f, 504.976f)
                lineTo(406.699f, 609.929f)
                lineTo(406.479f, 610.085f)
                curveTo(395.106f, 618.113f, 377.77f, 620.318f, 362.162f, 620.318f)
                close()
            }
        }
        group {
            path(fill = SolidColor(leafShadow)) {
                moveTo(337.708f, 477.181f)
                lineTo(335.418f, 491.475f)
                lineTo(350.921f, 497.87f)
                lineTo(337.708f, 477.181f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hair)) {
                moveTo(368.128f, 445.437f)
                horizontalLineTo(332.284f)
                curveTo(331.547f, 445.436f, 330.84f, 445.143f, 330.319f, 444.622f)
                curveTo(329.797f, 444.1f, 329.504f, 443.394f, 329.503f, 442.656f)
                verticalLineTo(427.206f)
                curveTo(329.514f, 421.723f, 331.7f, 416.467f, 335.581f, 412.593f)
                curveTo(339.463f, 408.72f, 344.722f, 406.544f, 350.206f, 406.544f)
                curveTo(355.69f, 406.544f, 360.95f, 408.72f, 364.831f, 412.593f)
                curveTo(368.712f, 416.467f, 370.898f, 421.723f, 370.909f, 427.206f)
                verticalLineTo(442.656f)
                curveTo(370.908f, 443.394f, 370.615f, 444.1f, 370.094f, 444.622f)
                curveTo(369.572f, 445.143f, 368.865f, 445.436f, 368.128f, 445.437f)
                verticalLineTo(445.437f)
                close()
            }
        }
        group {
            path(fill = SolidColor(skin)) {
                moveTo(354.164f, 444.507f)
                curveTo(362.547f, 444.507f, 369.342f, 437.711f, 369.342f, 429.328f)
                curveTo(369.342f, 420.945f, 362.547f, 414.149f, 354.164f, 414.149f)
                curveTo(345.781f, 414.149f, 338.985f, 420.945f, 338.985f, 429.328f)
                curveTo(338.985f, 437.711f, 345.781f, 444.507f, 354.164f, 444.507f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hair)) {
                moveTo(375.807f, 428.751f)
                horizontalLineTo(353.889f)
                lineTo(353.665f, 425.605f)
                lineTo(352.541f, 428.751f)
                horizontalLineTo(349.166f)
                lineTo(348.721f, 422.515f)
                lineTo(346.493f, 428.751f)
                horizontalLineTo(339.963f)
                verticalLineTo(428.442f)
                curveTo(339.968f, 424.101f, 341.695f, 419.938f, 344.765f, 416.868f)
                curveTo(347.835f, 413.797f, 351.998f, 412.07f, 356.34f, 412.065f)
                horizontalLineTo(359.43f)
                curveTo(363.772f, 412.07f, 367.935f, 413.797f, 371.005f, 416.868f)
                curveTo(374.075f, 419.938f, 375.802f, 424.1f, 375.807f, 428.442f)
                verticalLineTo(428.751f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hair)) {
                moveTo(353.71f, 448.321f)
                curveTo(353.545f, 448.321f, 353.38f, 448.307f, 353.218f, 448.278f)
                lineTo(337.169f, 445.446f)
                verticalLineTo(418.922f)
                horizontalLineTo(354.836f)
                lineTo(354.398f, 419.432f)
                curveTo(348.313f, 426.529f, 352.897f, 438.038f, 356.172f, 444.266f)
                curveTo(356.413f, 444.722f, 356.521f, 445.237f, 356.482f, 445.752f)
                curveTo(356.444f, 446.266f, 356.261f, 446.76f, 355.954f, 447.175f)
                curveTo(355.698f, 447.53f, 355.36f, 447.819f, 354.97f, 448.018f)
                curveTo(354.58f, 448.218f, 354.148f, 448.321f, 353.71f, 448.321f)
                verticalLineTo(448.321f)
                close()
            }
        }
        group {
            path(fill = SolidColor(sun)) {
                moveTo(382.029f, 512.433f)
                horizontalLineTo(373.231f)
                curveTo(372.949f, 512.434f, 372.677f, 512.33f, 372.468f, 512.14f)
                curveTo(372.259f, 511.95f, 372.128f, 511.689f, 372.101f, 511.408f)
                lineTo(370.34f, 493.359f)
                horizontalLineTo(384.919f)
                lineTo(383.159f, 511.408f)
                curveTo(383.132f, 511.689f, 383.001f, 511.95f, 382.792f, 512.14f)
                curveTo(382.583f, 512.33f, 382.311f, 512.434f, 382.029f, 512.433f)
                verticalLineTo(512.433f)
                close()
            }
        }
        group {
            path(fill = SolidColor(shoes)) {
                moveTo(384.896f, 495.63f)
                horizontalLineTo(370.364f)
                curveTo(370.063f, 495.63f, 369.774f, 495.51f, 369.561f, 495.297f)
                curveTo(369.348f, 495.085f, 369.229f, 494.796f, 369.228f, 494.495f)
                verticalLineTo(491.77f)
                curveTo(369.229f, 491.469f, 369.348f, 491.18f, 369.561f, 490.968f)
                curveTo(369.774f, 490.755f, 370.063f, 490.635f, 370.364f, 490.635f)
                horizontalLineTo(384.896f)
                curveTo(385.197f, 490.635f, 385.486f, 490.755f, 385.699f, 490.968f)
                curveTo(385.911f, 491.18f, 386.031f, 491.469f, 386.031f, 491.77f)
                verticalLineTo(494.495f)
                curveTo(386.031f, 494.796f, 385.911f, 495.085f, 385.699f, 495.297f)
                curveTo(385.486f, 495.51f, 385.197f, 495.63f, 384.896f, 495.63f)
                verticalLineTo(495.63f)
                close()
            }
        }
        group {
            path(fill = SolidColor(leafShadow)) {
                moveTo(327.892f, 504.667f)
                curveTo(331.932f, 509.345f, 337.412f, 512.547f, 343.47f, 513.772f)
                curveTo(349.528f, 514.996f, 355.821f, 514.173f, 361.36f, 511.432f)
                lineTo(364.664f, 509.797f)
                lineTo(327.892f, 504.667f)
                close()
            }
        }
        group {
            path(fill = SolidColor(skin)) {
                moveTo(380.711f, 501.173f)
                curveTo(380.034f, 500.412f, 379.198f, 499.809f, 378.263f, 499.406f)
                curveTo(377.327f, 499.004f, 376.315f, 498.811f, 375.297f, 498.842f)
                curveTo(374.279f, 498.872f, 373.28f, 499.126f, 372.371f, 499.584f)
                curveTo(371.462f, 500.043f, 370.664f, 500.695f, 370.034f, 501.495f)
                lineTo(354.709f, 497.193f)
                lineTo(349.823f, 505.869f)
                lineTo(371.551f, 511.64f)
                curveTo(372.965f, 512.61f, 374.686f, 513.026f, 376.387f, 512.812f)
                curveTo(378.088f, 512.597f, 379.651f, 511.766f, 380.78f, 510.475f)
                curveTo(381.909f, 509.185f, 382.526f, 507.525f, 382.513f, 505.811f)
                curveTo(382.5f, 504.096f, 381.859f, 502.446f, 380.711f, 501.173f)
                verticalLineTo(501.173f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hand)) {
                moveTo(344.139f, 510.801f)
                curveTo(336.852f, 510.802f, 326.993f, 506.53f, 315.29f, 498.255f)
                curveTo(314.637f, 497.803f, 314.086f, 497.221f, 313.671f, 496.545f)
                curveTo(313.255f, 495.869f, 312.985f, 495.114f, 312.876f, 494.327f)
                curveTo(312.013f, 488.858f, 317.348f, 481.464f, 317.871f, 480.756f)
                lineTo(323.479f, 465.355f)
                curveTo(323.543f, 465.105f, 325.351f, 458.441f, 329.888f, 456.071f)
                curveTo(330.843f, 455.581f, 331.893f, 455.303f, 332.966f, 455.258f)
                curveTo(334.039f, 455.212f, 335.108f, 455.399f, 336.102f, 455.806f)
                curveTo(344.744f, 458.953f, 337.996f, 483.254f, 337.069f, 486.441f)
                lineTo(348.52f, 491.83f)
                lineTo(355.791f, 496.465f)
                lineTo(365.748f, 497.507f)
                lineTo(363.045f, 510.019f)
                lineTo(347.922f, 510.359f)
                curveTo(346.684f, 510.662f, 345.413f, 510.811f, 344.139f, 510.801f)
                verticalLineTo(510.801f)
                close()
            }
        }

        group {
            path(fill = SolidColor(backgroundTrees)) {
                moveTo(887.675f, 396.659f)
                curveTo(887.667f, 382.038f, 884.407f, 367.601f, 878.13f, 354.396f)
                curveTo(871.854f, 341.19f, 862.719f, 329.546f, 851.386f, 320.307f)
                curveTo(840.054f, 311.068f, 826.808f, 304.465f, 812.609f, 300.978f)
                curveTo(798.409f, 297.49f, 783.612f, 297.205f, 769.289f, 300.143f)
                curveTo(754.966f, 303.08f, 741.475f, 309.167f, 729.795f, 317.963f)
                curveTo(718.115f, 326.758f, 708.538f, 338.042f, 701.757f, 350.996f)
                curveTo(694.976f, 363.95f, 691.162f, 378.25f, 690.591f, 392.86f)
                curveTo(690.02f, 407.47f, 692.706f, 422.025f, 698.454f, 435.469f)
                curveTo(698.358f, 435.362f, 698.258f, 435.257f, 698.162f, 435.15f)
                curveTo(702.541f, 445.355f, 708.611f, 454.748f, 716.116f, 462.932f)
                curveTo(716.139f, 462.957f, 716.162f, 462.981f, 716.184f, 463.006f)
                curveTo(716.79f, 463.666f, 717.399f, 464.322f, 718.022f, 464.966f)
                curveTo(727.031f, 474.362f, 737.816f, 481.874f, 749.752f, 487.068f)
                curveTo(761.687f, 492.261f, 774.536f, 495.032f, 787.551f, 495.219f)
                lineTo(784.22f, 676.148f)
                horizontalLineTo(794.512f)
                lineTo(792.429f, 556.733f)
                lineTo(807.316f, 548.895f)
                lineTo(805.045f, 544.582f)
                lineTo(792.334f, 551.274f)
                lineTo(791.356f, 495.21f)
                curveTo(817.102f, 494.62f, 841.595f, 483.978f, 859.596f, 465.56f)
                curveTo(877.596f, 447.143f, 887.674f, 422.412f, 887.675f, 396.659f)
                verticalLineTo(396.659f)
                close()
            }
        }

        group {
            path(fill = SolidColor(backgroundTrees)) {
                moveTo(595.087f, 348.612f)
                curveTo(595.078f, 331.477f, 591.257f, 314.558f, 583.902f, 299.082f)
                curveTo(576.547f, 283.606f, 565.841f, 269.96f, 552.56f, 259.133f)
                curveTo(539.28f, 248.306f, 523.757f, 240.568f, 507.116f, 236.481f)
                curveTo(490.476f, 232.394f, 473.135f, 232.06f, 456.349f, 235.502f)
                curveTo(439.564f, 238.945f, 423.754f, 246.079f, 410.066f, 256.386f)
                curveTo(396.378f, 266.694f, 385.154f, 279.917f, 377.208f, 295.098f)
                curveTo(369.262f, 310.279f, 364.792f, 327.038f, 364.122f, 344.159f)
                curveTo(363.453f, 361.281f, 366.601f, 378.338f, 373.337f, 394.093f)
                curveTo(373.224f, 393.967f, 373.108f, 393.845f, 372.995f, 393.719f)
                curveTo(378.127f, 405.679f, 385.24f, 416.686f, 394.036f, 426.278f)
                curveTo(394.062f, 426.307f, 394.089f, 426.335f, 394.116f, 426.364f)
                curveTo(394.825f, 427.137f, 395.539f, 427.907f, 396.269f, 428.661f)
                curveTo(406.826f, 439.672f, 419.466f, 448.476f, 433.454f, 454.563f)
                curveTo(447.441f, 460.649f, 462.499f, 463.896f, 477.752f, 464.115f)
                lineTo(473.848f, 676.148f)
                horizontalLineTo(485.908f)
                lineTo(483.467f, 536.204f)
                lineTo(500.913f, 527.019f)
                lineTo(498.252f, 521.964f)
                lineTo(483.356f, 529.806f)
                lineTo(482.21f, 464.105f)
                curveTo(512.382f, 463.413f, 541.086f, 450.941f, 562.181f, 429.358f)
                curveTo(583.276f, 407.774f, 595.087f, 378.792f, 595.087f, 348.612f)
                verticalLineTo(348.612f)
                close()
            }
        }

        group {
            path(fill = SolidColor(backgroundTrees)) {
                moveTo(263.259f, 303.418f)
                curveTo(263.249f, 283.919f, 258.901f, 264.666f, 250.531f, 247.055f)
                curveTo(242.16f, 229.444f, 229.977f, 213.915f, 214.864f, 201.593f)
                curveTo(199.751f, 189.272f, 182.086f, 180.467f, 163.15f, 175.816f)
                curveTo(144.213f, 171.165f, 124.48f, 170.785f, 105.378f, 174.702f)
                curveTo(86.2763f, 178.62f, 68.2854f, 186.738f, 52.7086f, 198.468f)
                curveTo(37.1319f, 210.197f, 24.3595f, 225.245f, 15.3167f, 242.521f)
                curveTo(6.27402f, 259.797f, 1.18748f, 278.868f, 0.425642f, 298.352f)
                curveTo(-0.336191f, 317.836f, 3.24577f, 337.246f, 10.912f, 355.175f)
                curveTo(10.7835f, 355.032f, 10.6506f, 354.893f, 10.5227f, 354.75f)
                curveTo(16.362f, 368.36f, 24.4571f, 380.886f, 34.4667f, 391.801f)
                curveTo(34.4966f, 391.834f, 34.5273f, 391.866f, 34.5572f, 391.899f)
                curveTo(35.3649f, 392.779f, 36.1771f, 393.655f, 37.0081f, 394.513f)
                curveTo(49.022f, 407.043f, 63.4053f, 417.062f, 79.323f, 423.989f)
                curveTo(95.2408f, 430.915f, 112.376f, 434.61f, 129.733f, 434.859f)
                lineTo(125.291f, 676.148f)
                horizontalLineTo(139.016f)
                lineTo(136.238f, 516.895f)
                lineTo(156.091f, 506.442f)
                lineTo(153.062f, 500.69f)
                lineTo(136.111f, 509.614f)
                lineTo(134.807f, 434.847f)
                curveTo(169.142f, 434.06f, 201.807f, 419.868f, 225.812f, 395.306f)
                curveTo(249.818f, 370.744f, 263.258f, 337.763f, 263.259f, 303.418f)
                verticalLineTo(303.418f)
                close()
            }
        }

        group {
            path(fill = SolidColor(sun)) {
                moveTo(756.685f, 171.952f)
                curveTo(804.169f, 171.952f, 842.661f, 133.459f, 842.661f, 85.9757f)
                curveTo(842.661f, 38.4926f, 804.169f, 0f, 756.685f, 0f)
                curveTo(709.202f, 0f, 670.71f, 38.4926f, 670.71f, 85.9757f)
                curveTo(670.71f, 133.459f, 709.202f, 171.952f, 756.685f, 171.952f)
                close()
            }
        }

        group {
            path(fill = SolidColor(leafDark)) {
                moveTo(245.559f, 359.928f)
                curveTo(340.724f, 359.928f, 417.871f, 282.782f, 417.871f, 187.616f)
                curveTo(417.871f, 92.4513f, 340.724f, 15.3047f, 245.559f, 15.3047f)
                curveTo(150.394f, 15.3047f, 73.2471f, 92.4513f, 73.2471f, 187.616f)
                curveTo(73.2471f, 282.782f, 150.394f, 359.928f, 245.559f, 359.928f)
                close()
            }
        }

        group {
            path(fill = SolidColor(leafShadow)) {
                moveTo(118.329f, 72.5254f)
                curveTo(102.257f, 110.123f, 100.076f, 152.21f, 112.178f, 191.267f)
                curveTo(124.28f, 230.324f, 149.875f, 263.805f, 184.39f, 285.727f)
                curveTo(218.905f, 307.65f, 260.09f, 316.586f, 300.587f, 310.937f)
                curveTo(341.084f, 305.289f, 378.252f, 285.425f, 405.452f, 254.895f)
                curveTo(395.633f, 277.865f, 380.938f, 298.426f, 362.384f, 315.154f)
                curveTo(343.831f, 331.881f, 321.862f, 344.374f, 298.001f, 351.768f)
                curveTo(274.139f, 359.162f, 248.956f, 361.28f, 224.195f, 357.975f)
                curveTo(199.433f, 354.67f, 175.687f, 346.021f, 154.6f, 332.628f)
                curveTo(133.513f, 319.234f, 115.591f, 301.416f, 102.074f, 280.408f)
                curveTo(88.5569f, 259.401f, 79.7689f, 235.705f, 76.3188f, 210.964f)
                curveTo(72.8686f, 186.223f, 74.8388f, 161.027f, 82.0929f, 137.123f)
                curveTo(89.3469f, 113.218f, 101.711f, 91.1768f, 118.329f, 72.5254f)
                verticalLineTo(72.5254f)
                close()
            }
        }

        group {
            path(fill = SolidColor(branch)) {
                moveTo(246.032f, 187.616f)
                horizontalLineTo(246.506f)
                lineTo(255.027f, 676.148f)
                horizontalLineTo(237.038f)
                lineTo(246.032f, 187.616f)
                close()
            }
        }

        group {
            path(fill = SolidColor(branch)) {
                moveTo(273.438f, 446.177f)
                lineTo(244.954f, 461.173f)
                lineTo(248.923f, 468.713f)
                lineTo(277.407f, 453.717f)
                lineTo(273.438f, 446.177f)
                close()
            }
        }

        group {
            path(fill = SolidColor(grass)) {
                moveTo(509.115f, 671.578f)
                curveTo(509.115f, 671.578f, 509.737f, 658.552f, 522.482f, 660.066f)
                close()
            }
        }
        group {
            path(fill = SolidColor(sun)) {
                moveTo(505.514f, 659.182f)
                curveTo(509.037f, 659.182f, 511.893f, 656.326f, 511.893f, 652.803f)
                curveTo(511.893f, 649.281f, 509.037f, 646.425f, 505.514f, 646.425f)
                curveTo(501.991f, 646.425f, 499.135f, 649.281f, 499.135f, 652.803f)
                curveTo(499.135f, 656.326f, 501.991f, 659.182f, 505.514f, 659.182f)
                close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(506.277f, 663.544f)
                horizontalLineTo(504.476f)
                verticalLineTo(676.148f)
                horizontalLineTo(506.277f)
                verticalLineTo(663.544f)
                close()
            }
        }

        group {
            path(fill = SolidColor(grass)) {
                moveTo(67.0829f, 669.778f)
                curveTo(67.0829f, 669.778f, 67.7046f, 656.751f, 80.4493f, 658.266f)
                close()
            }
        }
        group {
            path(fill = SolidColor(sun)) {
                moveTo(63.4818f, 657.381f)
                curveTo(67.0046f, 657.381f, 69.8605f, 654.526f, 69.8605f, 651.003f)
                curveTo(69.8605f, 647.48f, 67.0046f, 644.624f, 63.4818f, 644.624f)
                curveTo(59.959f, 644.624f, 57.1032f, 647.48f, 57.1032f, 651.003f)
                curveTo(57.1032f, 654.526f, 59.959f, 657.381f, 63.4818f, 657.381f)
                close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(64.2445f, 661.744f)
                horizontalLineTo(62.4439f)
                verticalLineTo(674.348f)
                horizontalLineTo(64.2445f)
                verticalLineTo(661.744f)
                close()
            }
        }

        group {
            path(fill = SolidColor(grass)) {
                moveTo(171.514f, 670.678f)
                curveTo(171.514f, 670.678f, 172.136f, 657.651f, 184.881f, 659.165f)
                close()
            }
        }
        group {
            path(fill = SolidColor(sun)) {
                moveTo(167.913f, 658.282f)
                curveTo(171.436f, 658.282f, 174.292f, 655.426f, 174.292f, 651.903f)
                curveTo(174.292f, 648.38f, 171.436f, 645.524f, 167.913f, 645.524f)
                curveTo(164.39f, 645.524f, 161.534f, 648.38f, 161.534f, 651.903f)
                curveTo(161.534f, 655.426f, 164.39f, 658.282f, 167.913f, 658.282f)
                close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(168.676f, 662.645f)
                horizontalLineTo(166.875f)
                verticalLineTo(675.248f)
                horizontalLineTo(168.676f)
                verticalLineTo(662.645f)
                close()
            }
        }

        group {
            path(fill = SolidColor(hair)) {
                moveTo(449.243f, 83.2986f)
                lineTo(462.038f, 73.0652f)
                curveTo(452.098f, 71.9686f, 448.014f, 77.3895f, 446.343f, 81.6802f)
                curveTo(438.578f, 78.4557f, 430.124f, 82.6815f, 430.124f, 82.6815f)
                lineTo(455.724f, 91.9753f)
                curveTo(454.433f, 88.5257f, 452.185f, 85.5159f, 449.243f, 83.2986f)
                verticalLineTo(83.2986f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hair)) {
                moveTo(643.827f, 187.54f)
                lineTo(656.622f, 177.306f)
                curveTo(646.682f, 176.21f, 642.598f, 181.631f, 640.927f, 185.921f)
                curveTo(633.161f, 182.697f, 624.708f, 186.923f, 624.708f, 186.923f)
                lineTo(650.308f, 196.216f)
                curveTo(649.016f, 192.767f, 646.768f, 189.757f, 643.827f, 187.54f)
                verticalLineTo(187.54f)
                close()
            }
        }
        group {
            path(fill = SolidColor(hair)) {
                moveTo(433.955f, 276.492f)
                lineTo(446.749f, 266.259f)
                curveTo(436.81f, 265.162f, 432.726f, 270.583f, 431.054f, 274.874f)
                curveTo(423.289f, 271.65f, 414.835f, 275.875f, 414.835f, 275.875f)
                lineTo(440.435f, 285.169f)
                curveTo(439.144f, 281.72f, 436.896f, 278.71f, 433.955f, 276.492f)
                close()
            }
        }

        group {
            path(fill = SolidColor(grass)) {
                moveTo(683.655f, 676.307f); curveTo(
                683.655f,
                676.307f,
                684.277f,
                663.28f,
                697.022f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(563.919f, 676.307f); curveTo(
                563.919f,
                676.307f,
                564.541f,
                663.28f,
                577.286f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(127.289f, 676.307f); curveTo(
                127.289f,
                676.307f,
                127.91f,
                663.28f,
                140.655f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(737.671f, 676.307f); curveTo(
                737.671f,
                676.307f,
                738.293f,
                663.28f,
                751.038f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(712.464f, 676.307f); curveTo(
                712.464f,
                676.307f,
                713.086f,
                663.28f,
                725.83f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(660.465f, 676.307f); curveTo(
                660.465f,
                676.307f,
                659.843f,
                663.28f,
                647.099f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(453.403f, 676.307f); curveTo(
                453.403f,
                676.307f,
                452.781f,
                663.28f,
                440.037f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(281.452f, 676.307f); curveTo(
                281.452f,
                676.307f,
                280.83f,
                663.28f,
                268.085f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(98.6969f, 676.307f); curveTo(
                98.6969f,
                676.307f,
                98.0752f,
                663.28f,
                85.3305f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(791.904f, 676.307f); curveTo(
                791.904f,
                676.307f,
                791.283f,
                663.28f,
                778.538f,
                664.794f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(714.481f, 677.207f); curveTo(
                714.481f,
                677.207f,
                713.859f,
                664.181f,
                701.115f,
                665.695f
            ); close()
            }
        }
        group {
            path(fill = SolidColor(grass)) {
                moveTo(888f, 674.604f)
                horizontalLineTo(0f)
                verticalLineTo(676.604f)
                horizontalLineTo(888f)
                verticalLineTo(674.604f)
                close()
            }
        }
    }.build()
}
