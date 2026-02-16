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

package com.d4rk.android.apps.apptoolkit.app.onboarding.utils.interfaces.providers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Star
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.apps.apptoolkit.app.main.ui.MainActivity
import com.d4rk.android.apps.apptoolkit.app.onboarding.utils.constants.OnboardingKeys
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.model.OnboardingPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.finish.FinishOnboardingPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.firebase.FirebaseOnboardingPage
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.ThemeOnboardingPageTab
import com.d4rk.android.libs.apptoolkit.app.onboarding.utils.interfaces.providers.OnboardingProvider

class AppOnboardingProvider : OnboardingProvider {

    override fun getOnboardingPages(context: Context): List<OnboardingPage> {
        return listOf(
            OnboardingPage.DefaultPage(
                key = OnboardingKeys.WELCOME,
                title = context.getString(R.string.onboarding_welcome_title),
                description = context.getString(R.string.onboarding_welcome_description),
                imageVector = Icons.Outlined.Star
            ),
            OnboardingPage.DefaultPage(
                key = OnboardingKeys.PERSONALIZATION_OPTIONS,
                title = context.getString(R.string.onboarding_personalization_title),
                description = context.getString(R.string.onboarding_personalization_description),
                imageVector = Icons.Outlined.AccountCircle
            ),
            OnboardingPage.CustomPage(
                key = OnboardingKeys.THEME_OPTIONS,
                content = { _ ->
                    ThemeOnboardingPageTab()
                }
            ),
            OnboardingPage.DefaultPage(
                key = OnboardingKeys.FEATURE_HIGHLIGHT_1,
                title = context.getString(R.string.onboarding_feature1_title),
                description = context.getString(R.string.onboarding_feature1_description),
                imageVector = Icons.Outlined.Build
            ),
            OnboardingPage.CustomPage(
                key = OnboardingKeys.CRASHLYTICS_OPTIONS,
                content = { isSelected ->
                    FirebaseOnboardingPage(isSelected = isSelected)
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

    override fun onOnboardingFinished(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
        if (context is Activity) {
            context.finish()
        }
    }
}
