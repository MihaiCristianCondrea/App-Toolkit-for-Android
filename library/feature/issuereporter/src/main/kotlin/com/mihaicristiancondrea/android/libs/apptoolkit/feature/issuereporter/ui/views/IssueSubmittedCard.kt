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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/** Confirmation shown once a report has been filed, linking to the created issue. */
@Composable
internal fun IssueSubmittedCard(
    issueUrl: String,
    firebaseController: FirebaseController,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = SizeConstants.ExtraSmallSize),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SizeConstants.LargeSize),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = stringResource(id = R.string.issue_submitted_successfully),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(SizeConstants.LauncherIconSize),
            )
            SmallVerticalSpacer()
            Text(
                text = stringResource(id = R.string.issue_submitted),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            SmallVerticalSpacer()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                GeneralOutlinedButton(
                    onClick = {
                        firebaseController.logEvent(
                            issueReporterActionEvent(
                                actionName = IssueReporterActionNames.OPEN_CREATED_ISSUE,
                                params = mapOf(
                                    "has_issue_url" to AnalyticsValue.Bool(issueUrl.isNotBlank()),
                                ),
                            )
                        )
                        uriHandler.openUri(issueUrl)
                    },
                    vectorIcon = Icons.AutoMirrored.Outlined.OpenInNew,
                    iconContentDescription = stringResource(R.string.open_issue_in_browser),
                    label = stringResource(R.string.open_button_label),
                )
            }
        }
    }
}
