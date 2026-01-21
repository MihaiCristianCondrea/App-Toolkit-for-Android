package com.d4rk.android.apps.apptoolkit.app.apps.common

import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.IconButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun AppCard(
    appInfo: AppInfo,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onShareClick: (AppInfo) -> Unit,
    modifier: Modifier
) {
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view = LocalView.current
    Card(
        modifier = modifier
            .bounceClick()
            .fillMaxSize()
            .aspectRatio(ratio = 1f)
            .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onAppClick(appInfo)
            }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(
                    space = SizeConstants.ZeroSize,
                    alignment = Alignment.End
                )
            ) {
                IconButton(
                    onClick = onFavoriteToggle,
                    vectorIcon = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    iconContentDescription = null
                )
                IconButton(
                    onClick = { onShareClick(appInfo) },
                    vectorIcon = Icons.Outlined.Share,
                    iconContentDescription = null
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LargeVerticalSpacer()
                AsyncImage(
                    model = appInfo.iconUrl,
                    contentDescription = appInfo.name,
                    modifier = Modifier
                        .size(size = SizeConstants.ExtraExtraLargeSize + SizeConstants.LargeSize + SizeConstants.SmallSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)),
                    contentScale = ContentScale.Fit
                )
                LargeVerticalSpacer()
                Text(
                    text = appInfo.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SizeConstants.LargeSize)
                        .animateContentSize(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
