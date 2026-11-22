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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.features.onboarding.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralButtonStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.R

/** Displays the onboarding privacy note and a button that opens the privacy policy. */
@Composable
fun PrivacyPolicySection() {
    val context: Context = LocalContext.current
    val appContext = remember(key1 = context) { context.applicationContext }

    val errorText: String = stringResource(id = R.string.error)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = SizeConstants.LargeSize),
            thickness = SizeConstants.ExtraTinySize / 4,
        )

        Text(
            text = stringResource(id = R.string.onboarding_crashlytics_privacy_info),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SizeConstants.LargeIncreasedSize + SizeConstants.ExtraSmallSize),
        )

        MediumVerticalSpacer()

        GeneralButton(
            style = GeneralButtonStyle.Outlined,
            onClick = {
                if (!context.openUrl(url = AppLinks.PRIVACY_POLICY)) {
                    Toast.makeText(appContext, errorText, Toast.LENGTH_SHORT).show()
                }
            },
            icon = ToolkitIcon.Vector(imageVector = Icons.AutoMirrored.Filled.Launch),
            label = stringResource(id = R.string.learn_more_privacy_policy),
        )
    }
}

