package com.d4rk.android.apps.apptoolkit.app.apps.common.screens.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.shimmerEffect
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun ShimmerPlaceholderAppCard(modifier: Modifier = Modifier, aspectRatio: Float = 1f) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(ratio = aspectRatio)
            .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
        ) {
            LargeVerticalSpacer()
            Box(
                modifier = Modifier
                    .size(SizeConstants.ExtraExtraLargeSize + SizeConstants.LargeSize + SizeConstants.SmallSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize))
                    .shimmerEffect()
            )
            LargeVerticalSpacer()
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.7f)
                    .clip(shape = RoundedCornerShape(SizeConstants.SmallSize))
                    .height(height = SizeConstants.LargeSize)
                    .shimmerEffect()
            )
            LargeVerticalSpacer()
        }
    }
}