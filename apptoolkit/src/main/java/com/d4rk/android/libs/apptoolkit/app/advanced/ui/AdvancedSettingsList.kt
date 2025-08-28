package com.d4rk.android.libs.apptoolkit.app.advanced.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.issuereporter.ui.IssueReporterActivity
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.AdvancedSettingsProvider
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdvancedSettingsList(
    paddingValues: PaddingValues = PaddingValues(),
    provider: AdvancedSettingsProvider,
    viewModel: AdvancedSettingsViewModel = koinViewModel(),
) {
    val context: Context = LocalContext.current
    val uiState: AdvancedSettingsUiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.cacheClearMessage) {
        uiState.cacheClearMessage?.let { messageRes ->
            Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }

    LazyColumn(contentPadding = paddingValues, modifier = Modifier.fillMaxHeight()) {
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.error_reporting))
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.bug_report),
                    summary = stringResource(id = R.string.summary_preference_settings_bug_report),
                    onClick = {
                        IntentsHelper.openActivity(
                            context = context,
                            activityClass = IssueReporterActivity::class.java,
                        )
                    },
                )
            }
        }
        item {
            PreferenceCategoryItem(title = stringResource(id = R.string.cache_management))
            SmallVerticalSpacer()
            Column(
                modifier = Modifier
                    .padding(horizontal = SizeConstants.LargeSize)
                    .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
            ) {
                SettingsPreferenceItem(
                    title = stringResource(id = R.string.clear_cache),
                    summary = stringResource(id = R.string.summary_preference_settings_clear_cache),
                    onClick = { viewModel.onClearCache() },
                )
            }
        }
    }
}