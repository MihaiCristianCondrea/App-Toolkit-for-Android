package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun MiniUiPreview(
    background: Brush,
    surface: Color,
    line: Color,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    val deviceShape = RoundedCornerShape(
        topStart = SizeConstants.LargeIncreasedSize,
        topEnd = SizeConstants.LargeIncreasedSize,
        bottomStart = SizeConstants.ZeroSize,
        bottomEnd = SizeConstants.ZeroSize
    )

    val screenCorner = SizeConstants.MediumSize + SizeConstants.ExtraTinySize // 18dp
    val screenShape = RoundedCornerShape(
        topStart = screenCorner,
        topEnd = screenCorner,
        bottomStart = SizeConstants.ZeroSize,
        bottomEnd = SizeConstants.ZeroSize
    )

    val isLight = surface.luminance() > 0.5f
    val deviceBodyColor = if (isLight) {
        Color.White.copy(alpha = 0.64f)
    } else {
        Color.Black.copy(alpha = 0.32f)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        shape = deviceShape,
        color = deviceBodyColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = SizeConstants.ExtraSmallSize,
                    end = SizeConstants.ExtraSmallSize,
                    top = SizeConstants.ExtraSmallSize,
                    bottom = SizeConstants.ZeroSize
                )
                .clip(screenShape)
                .background(background)
                .padding(SizeConstants.ExtraSmallSize),
        ) {
            Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize * 2))
            PreviewPill(
                surface = surface,
                line = line,
                modifier = Modifier.height(SizeConstants.LargeSize - SizeConstants.ExtraSmallSize)
            )
            Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize * 2))
            PreviewRowItem(surface = surface, line = line, accent = accent)
            Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize * 2))
            PreviewRowItem(surface = surface, line = line, accent = accent)
            Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize * 2))
            PreviewRowItem(surface = surface, line = line, accent = accent, trailingDot = true)
        }
    }
}

@Composable
private fun PreviewPill(
    surface: Color,
    line: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(surface)
            .padding(
                horizontal = SizeConstants.SmallSize,
                vertical = SizeConstants.ExtraTinySize * 2
            ),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize * 2),
    ) {
        Spacer(
            Modifier
                .height(SizeConstants.SmallSize)
                .width(SizeConstants.ExtraLargeIncreasedSize)
                .clip(CircleShape)
                .background(line.copy(alpha = 0.9f))
        )
        Spacer(
            Modifier
                .height(SizeConstants.SmallSize)
                .width(SizeConstants.LargeIncreasedSize)
                .clip(CircleShape)
                .background(line.copy(alpha = 0.6f))
        )
    }
}

@Composable
private fun PreviewRowItem(
    surface: Color,
    line: Color,
    accent: Color,
    trailingDot: Boolean = false,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(SizeConstants.SmallSize))
            .background(surface)
            .padding(
                horizontal = SizeConstants.SmallSize,
                vertical = SizeConstants.ExtraSmallSize + SizeConstants.ExtraTinySize
            ),
        horizontalArrangement = Arrangement.spacedBy(SizeConstants.SmallSize),
    ) {
        Spacer(
            Modifier
                .size(SizeConstants.SmallSize)
                .clip(CircleShape)
                .background(accent)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(SizeConstants.ExtraTinySize * 2),
            modifier = Modifier.weight(1f)
        ) {
            Spacer(
                Modifier
                    .height(SizeConstants.ExtraSmallSize)
                    .width(SizeConstants.SeventyTwoSize)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.95f))
            )
            Spacer(
                Modifier
                    .height(SizeConstants.ExtraSmallSize)
                    .width(SizeConstants.FortyFourSize)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.65f))
            )
        }

        if (trailingDot) {
            Spacer(
                Modifier
                    .size(SizeConstants.SmallSize)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.75f))
            )
        }
    }
}
