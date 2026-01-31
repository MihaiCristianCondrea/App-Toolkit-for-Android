package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.onboarding.domain.model.OnboardingThemeChoice
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.cards.AmoledModeToggleCard
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.cards.ThemeChoicePreviewCard
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.DarkModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.LightModePreview
import com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews.SystemModePreview
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.LargeVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.datastore.DataStoreNamesConstants
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.datastore.rememberThemePreferencesState
import com.d4rk.android.libs.apptoolkit.data.local.datastore.rememberCommonDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ThemeOnboardingPageTab() {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val dataStore = rememberCommonDataStore()
    val themePreferences = rememberThemePreferencesState()

    val defaultThemeModeKey: String = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM
    val currentThemeMode: String = themePreferences.themeMode.ifBlank { defaultThemeModeKey }
    val isAmoledMode: Boolean = themePreferences.amoledMode

    val isLightSelected = currentThemeMode == DataStoreNamesConstants.THEME_MODE_LIGHT
    val amoledAllowed = !isLightSelected

    val themeChoices: List<OnboardingThemeChoice> = listOf(
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_LIGHT,
            displayName = stringResource(id = R.string.light_mode),
            icon = Icons.Filled.LightMode,
            description = stringResource(R.string.onboarding_theme_light_desc)
        ),
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_DARK,
            displayName = stringResource(id = R.string.dark_mode),
            icon = Icons.Filled.DarkMode,
            description = stringResource(R.string.onboarding_theme_dark_desc)
        ),
        OnboardingThemeChoice(
            key = DataStoreNamesConstants.THEME_MODE_FOLLOW_SYSTEM,
            displayName = stringResource(id = R.string.follow_system),
            icon = Icons.Filled.BrightnessAuto,
            description = stringResource(R.string.onboarding_theme_system_desc)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SizeConstants.LargeSize),
        verticalArrangement = Arrangement.spacedBy(SizeConstants.LargeSize),
    ) {
        LargeVerticalSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(SizeConstants.MediumSize),
        ) {
            themeChoices.forEach { choice ->
                ThemeChoicePreviewCard(
                    choice = choice,
                    isSelected = currentThemeMode == choice.key,
                    onClick = {
                        coroutineScope.launch {
                            dataStore.saveThemeMode(mode = choice.key)
                            if (choice.key == DataStoreNamesConstants.THEME_MODE_LIGHT && isAmoledMode) {
                                dataStore.saveAmoledMode(isChecked = false)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    preview = {
                        when (choice.key) {
                            DataStoreNamesConstants.THEME_MODE_LIGHT -> LightModePreview(Modifier.fillMaxWidth())
                            DataStoreNamesConstants.THEME_MODE_DARK -> DarkModePreview(Modifier.fillMaxWidth())
                            else -> SystemModePreview(Modifier.fillMaxWidth())
                        }
                    }
                )
            }
        }

        AmoledModeToggleCard(
            isAmoledMode = isAmoledMode,
            enabled = amoledAllowed,
            onCheckedChange = { isChecked ->
                if (!amoledAllowed) return@AmoledModeToggleCard
                coroutineScope.launch {
                    dataStore.saveAmoledMode(isChecked = isChecked)
                }
            }
        )

        // TODO: Think 2-3 days for the colors implementation.
        Spacer(modifier = Modifier.height(SizeConstants.ExtraTinySize))
    }
}
