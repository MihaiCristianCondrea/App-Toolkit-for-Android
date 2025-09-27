package com.d4rk.android.apps.apptoolkit.app.apps.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.OutlinedIconButtonWithText
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraSmallHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.MediumHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppDetailsBottomSheet(
    appInfo: AppInfo,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onOpenInPlayStoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LargeVerticalSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SizeConstants.LargeSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize)
        ) {
            Card(
                shape = RoundedCornerShape(SizeConstants.LargeSize),
            ) {
                AsyncImage(
                    model = appInfo.iconUrl,
                    contentDescription = appInfo.name,
                    modifier = Modifier.size(SizeConstants.ExtraExtraLargeSize * 2)
                )
            }
            Column {
                Text(
                    text = appInfo.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }
        LargeVerticalSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SizeConstants.LargeSize),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.get_it_on_google_play),
                contentDescription = stringResource(R.string.app_details_view_on_play_store),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .bounceClick()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onOpenInPlayStoreClick()
                    }
            )
        }
        if (appInfo.description.isNotEmpty()) {
            LargeVerticalSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(id = com.d4rk.android.libs.apptoolkit.R.string.about)
                )
                MediumHorizontalSpacer()
                Text(
                    text = "About this app",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            ExtraSmallHorizontalSpacer()
            Text(
                text = appInfo.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        if (appInfo.screenshots.isNotEmpty()) {
            LargeVerticalSpacer()
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Screenshots",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                )
                LargeVerticalSpacer()
                LazyRow(
                    contentPadding = PaddingValues(horizontal = SizeConstants.LargeSize),
                    horizontalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize)
                ) {
                    items(appInfo.screenshots) { screenshotUrl ->
                        Card(
                            shape = RoundedCornerShape(SizeConstants.LargeSize),
                        ) {
                            AsyncImage(
                                model = screenshotUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(240.dp)
                                    .aspectRatio(9f / 16f)
                                    .clip(RoundedCornerShape(SizeConstants.LargeSize))
                            )
                        }
                    }
                }
            }
        }
        LargeVerticalSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                SizeConstants.LargeSize,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButtonWithText(
                onClick = onShareClick,
                icon = Icons.Outlined.Share,
                label = stringResource(id = R.string.app_details_share_content_description)
            )
            OutlinedIconButtonWithText(
                onClick = onFavoriteClick,
                icon = Icons.Outlined.StarOutline,
                label = stringResource(id = R.string.favorite_apps)
            )
        }
    }
}