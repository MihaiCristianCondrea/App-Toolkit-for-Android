package com.d4rk.android.libs.apptoolkit.app.about.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.app.about.ui.contract.AboutEvent
import com.d4rk.android.libs.apptoolkit.app.about.ui.state.AboutUiState
import com.d4rk.android.libs.apptoolkit.app.licenses.ui.LicensesActivity
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.LoadingScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.NoDataScreen
import com.d4rk.android.libs.apptoolkit.core.ui.components.layouts.ScreenStateHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.PreferenceCategoryItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.preferences.SettingsPreferenceItem
import com.d4rk.android.libs.apptoolkit.core.ui.components.snackbar.DefaultSnackbarHandler
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.ExtraTinyVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import org.koin.compose.viewmodel.koinViewModel
import java.util.concurrent.TimeUnit

@Composable
fun AboutSettingsList(paddingValues: PaddingValues = PaddingValues(), snackbarHostState: SnackbarHostState) {
    val context: Context = LocalContext.current
    val viewModel: AboutViewModel = koinViewModel()
    val screenState: UiStateScreen<AboutUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceInfo: String = stringResource(id = R.string.device_info)

    var showKonfettiAnimationForThisInstance: Boolean by rememberSaveable { mutableStateOf(value = false) }
    var appVersionTapCount: Int by rememberSaveable { mutableIntStateOf(value = 0) }

    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = Spread.ROUND,
        position = Position.Relative(0.5, 0.3),
        emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(amount = 100)
    )
    val partyRain =
        Party(
            emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(amount = 60),
            angle = Angle.BOTTOM,
            spread = Spread.SMALL,
            speed = 5f,
            maxSpeed = 15f,
            timeToLive = 3000L,
            position = Position.Relative(x = 0.0, y = 0.0)
                .between(value = Position.Relative(x = 1.0, y = 0.0))
        )

    LaunchedEffect(key1 = showKonfettiAnimationForThisInstance) {
        if (showKonfettiAnimationForThisInstance) {
            delay(3000)
            showKonfettiAnimationForThisInstance = false
        }
    }

    Box(modifier = Modifier.fillMaxHeight()) {
        ScreenStateHandler(
            screenState = screenState,
            onLoading = { LoadingScreen() },
            onEmpty = { NoDataScreen(paddingValues = paddingValues) },
            onSuccess = { data: AboutUiState ->
                LazyColumn(modifier = Modifier.fillMaxHeight(), contentPadding = paddingValues) {
                    item {
                        PreferenceCategoryItem(title = stringResource(id = R.string.app_info))
                        SmallVerticalSpacer()
                        Column(
                            modifier = Modifier
                                .padding(horizontal = SizeConstants.LargeSize)
                                .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                        ) {
                            SettingsPreferenceItem(
                                title = stringResource(id = R.string.app_full_name),
                                summary = stringResource(id = R.string.copyright)
                            )
                            ExtraTinyVerticalSpacer()
                            SettingsPreferenceItem(
                                title = stringResource(id = R.string.app_build_version),
                                summary = "${data.appVersion} (${data.appVersionCode})",
                            ) {
                                appVersionTapCount += 1
                                if (appVersionTapCount >= 5) {
                                    appVersionTapCount = 0
                                    showKonfettiAnimationForThisInstance = true
                                }
                            }
                            ExtraTinyVerticalSpacer()
                            SettingsPreferenceItem(
                                title = stringResource(id = R.string.oss_license_title),
                                summary = stringResource(id = R.string.summary_preference_settings_oss)
                            ) {
                                IntentsHelper.openActivity(
                                    context = context,
                                    activityClass = LicensesActivity::class.java
                                )
                            }
                        }
                    }

                    item {
                        PreferenceCategoryItem(title = deviceInfo)
                        SmallVerticalSpacer()
                        Column(
                            modifier = Modifier
                                .padding(horizontal = SizeConstants.LargeSize)
                                .clip(shape = RoundedCornerShape(size = SizeConstants.LargeSize))
                        ) {
                            SettingsPreferenceItem(title = deviceInfo, summary = data.deviceInfo) {
                                viewModel.onEvent(event = AboutEvent.CopyDeviceInfo(label = deviceInfo))
                            }
                        }
                    }
                }
            })

        if (showKonfettiAnimationForThisInstance) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(party, partyRain),
            )
        }
    }

    DefaultSnackbarHandler(screenState = screenState, snackbarHostState = snackbarHostState, getDismissEvent = { AboutEvent.DismissSnackbar }, onEvent = { viewModel.onEvent(it) })
}
