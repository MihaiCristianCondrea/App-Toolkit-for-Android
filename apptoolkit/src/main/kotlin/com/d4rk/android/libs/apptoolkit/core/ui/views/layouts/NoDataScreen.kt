package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.NoDataNativeAdCard
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.IconButtonWithText
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

/**
 * Displays a placeholder screen when no data is available.
 *
 * A progress indicator with an optional [icon] is shown in the center of the
 * screen. A retry button and native ad card can be toggled through [showRetry] and
 * [showAd] respectively. When [isError] is true error colors are used.
 *
 * @param text Label for the retry action button.
 * @param textMessage Message describing the empty state.
 * @param icon Icon rendered at the center of the indicator.
 * @param showRetry Whether to display the retry button.
 * @param onRetry Callback invoked when the retry button is pressed.
 * @param showAd Whether a [NoDataNativeAdCard] should be displayed.
 * @param isError Shows the indicator with error styling when true.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NoDataScreen(
    text: Int = R.string.try_again,
    textMessage: Int = R.string.try_again,
    icon: ImageVector = Icons.Default.Info,
    showRetry: Boolean = false,
    onRetry: () -> Unit = {},
    showAd: Boolean = true,
    isError: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(),
) {
    val adUnitId = koinInject<com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig>(
        qualifier = named(name = "no_data_native_ad")
    ).bannerAdUnitId
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LargeVerticalSpacer()
        Box(
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(
                modifier = Modifier.size(size = SizeConstants.OneFortyFourSize),
                color = if (isError) MaterialTheme.colorScheme.errorContainer else LoadingIndicatorDefaults.indicatorColor
            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(size = SizeConstants.ExtraExtraLargeSize + SizeConstants.SmallSize + SizeConstants.ExtraTinySize),
                tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryContainer
            )
        }

        Text(
            text = stringResource(id = textMessage),
            style = MaterialTheme.typography.displaySmall.copy(textAlign = TextAlign.Center),
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )
        if (showRetry) {
            LargeVerticalSpacer()
            IconButtonWithText(
                onClick = onRetry,
                icon = Icons.Filled.Refresh,
                label = stringResource(id = text)
            )
        }

        LargeVerticalSpacer()

        if (showAd) {
            NoDataNativeAdCard(
                adUnitId = adUnitId,
                modifier = Modifier
                    .padding(all = SizeConstants.MediumSize),
            )
        }

        LargeVerticalSpacer()
    }
}
