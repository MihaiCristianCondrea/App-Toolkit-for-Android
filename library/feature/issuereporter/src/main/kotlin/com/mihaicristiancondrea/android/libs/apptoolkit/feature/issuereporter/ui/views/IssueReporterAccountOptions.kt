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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.contracts.IssueReporterEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.IssueReporterActionNames
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.ui.utils.issueReporterActionEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.RadioButtonPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.groupedPreferenceItem
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.R

/**
 * Who the report is filed as.
 *
 * The GitHub-account option is present but disabled: the flow only supports anonymous reports today,
 * and showing the alternative greyed out is what tells the user that.
 */
@Composable
internal fun IssueReporterAccountOptions(
    anonymous: Boolean,
    firebaseController: FirebaseController,
    onEvent: (IssueReporterEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    RadioButtonPreferenceItem(
        text = stringResource(id = R.string.send_anonymously),
        isChecked = anonymous,
        onCheckedChange = { checked ->
            if (checked) {
                firebaseController.logEvent(
                    issueReporterActionEvent(
                        actionName = IssueReporterActionNames.SET_ANONYMOUS_MODE,
                        params = mapOf("anonymous" to AnalyticsValue.Bool(true)),
                    )
                )
                onEvent(IssueReporterEvent.SetAnonymous(anonymous = true))
            }
        },
        modifier = modifier.groupedPreferenceItem(
            position = GroupedItemPosition.FIRST,
            outerRadius = SizeConstants.LargeMediumSize,
        ),
    )

    RadioButtonPreferenceItem(
        text = stringResource(id = R.string.use_github_account),
        isChecked = !anonymous,
        onCheckedChange = { },
        enabled = false,
        modifier = Modifier.groupedPreferenceItem(
            position = GroupedItemPosition.LAST,
            outerRadius = SizeConstants.LargeMediumSize,
        ),
    )
}
