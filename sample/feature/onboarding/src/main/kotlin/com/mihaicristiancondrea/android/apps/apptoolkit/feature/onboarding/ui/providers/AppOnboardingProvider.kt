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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.ui.providers

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Widgets
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.onboarding.ui.models.OnboardingKeys
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.diagnostics.ui.onboarding.TelemetryOnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.models.OnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.providers.OnboardingProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.finish.FinishOnboardingPage
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.onboarding.ui.views.pages.theme.ThemeOnboardingPageTab

class AppOnboardingProvider : OnboardingProvider {

    override fun getOnboardingPages(context: Context): List<OnboardingPage> {
        return listOf(
            OnboardingPage.DefaultPage(
                key = OnboardingKeys.WELCOME,
                title = context.getString(R.string.onboarding_welcome_title),
                description = context.getString(R.string.onboarding_welcome_description),
                imageVector = Icons.Outlined.Handyman
            ),
            OnboardingPage.CustomPage(
                key = OnboardingKeys.THEME_OPTIONS,
                content = { _ ->
                    ThemeOnboardingPageTab()
                }
            ),
            OnboardingPage.DefaultPage(
                key = OnboardingKeys.QUICK_SETTINGS,
                title = context.getString(R.string.onboarding_quick_settings_title),
                description = context.getString(R.string.onboarding_quick_settings_description),
                imageVector = Icons.Outlined.Widgets
            ),
            OnboardingPage.CustomPage(
                key = OnboardingKeys.CRASHLYTICS_OPTIONS,
                content = { isSelected ->
                    TelemetryOnboardingPage(isSelected = isSelected)
                }
            ),
            OnboardingPage.CustomPage(
                key = OnboardingKeys.ONBOARDING_COMPLETE,
                content = {
                    FinishOnboardingPage()
                }
            ),
        ).filter {
            when (it) {
                is OnboardingPage.DefaultPage -> it.isEnabled
                is OnboardingPage.CustomPage -> it.isEnabled
            }
        }
    }
}
