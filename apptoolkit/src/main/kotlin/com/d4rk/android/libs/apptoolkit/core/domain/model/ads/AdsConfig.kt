package com.d4rk.android.libs.apptoolkit.core.domain.model.ads

import androidx.compose.runtime.Immutable
import com.google.android.gms.ads.AdSize

/**
 * Data class representing the configuration for displaying ads.
 *
 * @property bannerAdUnitId The unique identifier for the banner ad unit. Defaults to an empty string.
 * @property adSize The size of the ad to be displayed. Defaults to [AdSize.BANNER].
 */
@Immutable
data class AdsConfig(
    val bannerAdUnitId: String = "",
    val adSize: AdSize = AdSize.BANNER,
)