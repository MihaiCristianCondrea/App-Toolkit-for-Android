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
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import coil3.compose.AsyncImage
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.apps.list.domain.model.AppInfo
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.AppDetailsNativeAd
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.OutlinedIconButtonWithText
import com.d4rk.android.libs.apptoolkit.core.ui.views.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppDetailsBottomSheet(
    appInfo: AppInfo,
    isFavorite: Boolean,
    isAppInstalled: Boolean?,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onOpenAppClick: () -> Unit,
    onOpenInPlayStoreClick: () -> Unit,
    adsConfig: AdsConfig,
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
                    fontWeight = FontWeight.SemiBold,
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
            when (isAppInstalled) {
                true -> {
                    OutlinedIconButtonWithText(
                        onClick = onOpenAppClick,
                        icon = Icons.AutoMirrored.Outlined.OpenInNew,
                        label = stringResource(id = R.string.app_details_open_app)
                    )
                }

                false -> {
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

                null -> {
                    CircularProgressIndicator()
                }
            }
        }

        AppDetailsNativeAd(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SizeConstants.LargeSize),
            adsConfig = adsConfig
        )

        if (appInfo.description.isNotEmpty()) {
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
                    text = stringResource(id = R.string.app_details_about_title),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            ExtraSmallVerticalSpacer()
            Text(
                text = appInfo.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SizeConstants.LargeSize)
            )
        }
        if (appInfo.screenshots.isNotEmpty()) {
            LargeVerticalSpacer()
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.app_details_screenshots_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
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
                                    .height(SizeConstants.TwoHundredFortySize)
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
                icon = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                label = stringResource(id = R.string.favorite_apps)
            )
        }
    }
}
