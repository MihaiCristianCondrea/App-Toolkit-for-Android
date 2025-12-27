package com.d4rk.android.libs.apptoolkit.app.permissions.ui

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract.PermissionsEvent
import com.d4rk.android.libs.apptoolkit.app.settings.settings.domain.model.SettingsConfig
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.views.navigation.LargeTopAppBarWithScaffold
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen() {
    val viewModel: PermissionsViewModel = koinViewModel()
    val screenState: UiStateScreen<SettingsConfig> by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(PermissionsEvent.Load)
    }

    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.permissions),
        onBackClicked = { (context as Activity).finish() }) { paddingValues ->
        ScreenStateHandler(screenState = screenState, onLoading = { LoadingScreen() }, onEmpty = {
            NoDataScreen(
                icon = Icons.Outlined.Settings,
                showRetry = true,
                onRetry = { viewModel.onEvent(PermissionsEvent.Load) },
                paddingValues = paddingValues
            )
        }, onSuccess = { settingsConfig ->
            PermissionsContent(paddingValues, settingsConfig)
        })
    }
}

@Composable
fun PermissionsContent(
    paddingValues: PaddingValues,
    settingsConfig: SettingsConfig
) {
    LazyColumn(contentPadding = paddingValues, modifier = Modifier.fillMaxHeight()) {
        settingsConfig.categories.forEach { category ->
            item {
                category.title.let { title ->
                    PreferenceCategoryItem(title = title)
                    SmallVerticalSpacer()
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = SizeConstants.LargeSize)
                        .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                ) {
                    category.preferences.forEach { preference ->
                        SettingsPreferenceItem(
                            icon = preference.icon,
                            title = preference.title,
                            summary = preference.summary,
                            onClick = { preference.action.invoke() })
                        ExtraTinyVerticalSpacer()
                    }
                }
            }
        }
    }
}
