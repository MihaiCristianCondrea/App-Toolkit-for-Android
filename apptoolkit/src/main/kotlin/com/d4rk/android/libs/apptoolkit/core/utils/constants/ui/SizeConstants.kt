package com.d4rk.android.libs.apptoolkit.core.utils.constants.ui

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SizeConstants {
    // Spacers
    val ButtonIcon: Dp = ButtonDefaults.IconSpacing

    // Sizes
    val ExtraExtraLargeSize: Dp = 48.dp
    val ExtraLargeIncreasedSize: Dp = 32.dp
    val ExtraLargeSize: Dp = 28.dp
    val LargeIncreasedSize: Dp = 20.dp
    val LargeSize: Dp = 16.dp
    val MediumSize: Dp = 12.dp
    val SmallSize: Dp = 8.dp
    val ExtraSmallSize: Dp = 4.dp
    val ExtraTinySize: Dp = 2.dp
    val ZeroSize: Dp = 0.dp
    val TwentyFourSize: Dp = LargeSize + SmallSize
    val FortyFourSize: Dp = ExtraLargeSize + LargeSize
    val SeventyTwoSize: Dp = ExtraExtraLargeSize + LargeIncreasedSize + ExtraSmallSize
    val EightySize: Dp = ExtraExtraLargeSize + ExtraLargeIncreasedSize
    val NinetySixSize: Dp = ExtraExtraLargeSize * 2
    val OneHundredSize: Dp = ExtraLargeIncreasedSize * 3 + ExtraSmallSize
    val OneFortyFourSize: Dp = ExtraExtraLargeSize * 3
    val OneEightySize: Dp = OneFortyFourSize + LargeIncreasedSize + LargeSize
    val TwoHundredSize: Dp = ExtraExtraLargeSize * 4 + SmallSize
    val TwoHundredTwentySize: Dp = ExtraExtraLargeSize * 4 + ExtraLargeSize
    val TwoHundredFortySize: Dp = ExtraExtraLargeSize * 5
    val TwoHundredFiftySixSize: Dp = TwoHundredFortySize + LargeSize
    val TwoHundredFiftyEightSize: Dp = TwoHundredFortySize + MediumSize + ExtraSmallSize + ExtraTinySize
    val ButtonIconSize: Dp = ButtonDefaults.IconSize
    val SwitchIconSize: Dp = SwitchDefaults.IconSize
}
