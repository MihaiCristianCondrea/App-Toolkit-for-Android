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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.ui.mappers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.models.WeatherEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.theme.R

/** Icon shown for the weather effect in the app bar action and its dialog. */
internal fun WeatherEffect.iconImageVector(): ImageVector = when (this) {
    WeatherEffect.Automatic -> Icons.Outlined.AutoAwesome
    WeatherEffect.Snow -> Icons.Outlined.AcUnit
    WeatherEffect.Rain -> Icons.Outlined.WaterDrop
    WeatherEffect.Off -> Icons.Outlined.CloudOff
}

/** Label of the weather effect as it is offered in the dialog. */
@StringRes
internal fun WeatherEffect.labelResId(): Int = when (this) {
    WeatherEffect.Automatic -> R.string.weather_effect_automatic
    WeatherEffect.Snow -> R.string.weather_effect_snow
    WeatherEffect.Rain -> R.string.weather_effect_rain
    WeatherEffect.Off -> R.string.weather_effect_off
}
