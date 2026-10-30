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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.data.models.AppSummary
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics.AppInteractionType
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.analytics.logAppInteraction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.AppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.contracts.MoreAppsEvent
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.states.MoreAppsUiState
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.utils.AnalyticsAppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.utils.AndroidAppActionLauncher
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.utils.rememberOpenAppAction
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.AppDetailsBottomSheet
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.MoreAppsIcon
import com.mihaicristiancondrea.android.apps.apptoolkit.integration.ads.constants.AppAdsQualifiers
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.coroutines.dispatchers.DispatcherProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.ClipboardRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.InstalledAppsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.links.AppLinks
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.analytics.logSelectContent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.extensions.context.openUrl
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.MessageToastHost
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.utils.AppLauncher
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase.AppShowcase
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase.AppShowcaseLabels
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase.AppShowcaseState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase.ShowcaseApp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.showcase.appShowcaseLabels
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

/** Where the details sheet's own actions are reported from, as the former apps page reported them. */
private const val APP_DETAILS_INTERACTION_SOURCE: String = "app_details"

/** The developer's page on Google Play, which "View all" opens. */
internal val DeveloperPageUrl: String = AppLinks.PLAY_STORE_DEVELOPER + AppLinks.DEFAULT_DEVELOPER_ID

/**
 * "More apps": the developer's other apps as a carousel at the end of Quick Tools, which the app
 * places there through the screen's footer.
 *
 * A tap on an installed app opens it; on one that is not, it opens the app's details sheet, whose
 * Play Store action installs it. A card's own button opens the app or its store page directly, and
 * "View all" opens the developer's page on Google Play. It shows no ads of its own; the details
 * sheet keeps its existing one.
 */
@Composable
fun MoreAppsSection(modifier: Modifier = Modifier) {
    val viewModel: MoreAppsViewModel = koinViewModel()
    val state: MoreAppsUiState by viewModel.state.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val telemetry = LocalTelemetry.current
    val context = LocalContext.current
    val installedApps: InstalledAppsRepository = koinInject()
    val dispatchers: DispatcherProvider = koinInject()
    val clipboard: ClipboardRepository = koinInject()
    val detailsAdsConfig: AdsConfig = koinInject(qualifier = named(AppAdsQualifiers.APP_DETAILS_NATIVE_AD))
    val launcher = remember(installedApps, dispatchers) { AppLauncher(installedApps, dispatchers) }
    // Opens an installed app, and falls back to its store page when it cannot be launched.
    val openApp: (AppSummary) -> Unit = rememberOpenAppAction(launcher, installedApps::isInstalled)
    val appActionLauncher = remember(context, clipboard, installedApps) { AndroidAppActionLauncher(context, clipboard, installedApps) }
    val selectedApp = state.selectedApp
    val detailsActionLauncher: AppActionLauncher = remember(appActionLauncher, telemetry, selectedApp) {
        selectedApp?.let { app ->
            AnalyticsAppActionLauncher(
                delegate = appActionLauncher,
                telemetryRepository = telemetry,
                appInfo = app,
                source = APP_DETAILS_INTERACTION_SOURCE,
            )
        } ?: appActionLauncher
    }

    MessageToastHost(messages = messages, onShown = viewModel::messageShown, errorDuration = Toast.LENGTH_SHORT)

    MoreAppsSectionContent(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenApp = { app ->
            telemetry.logAppInteraction(MoreAppsViewModel.INTERACTION_SOURCE, app, AppInteractionType.OpenInstalledApp)
            openApp(app)
        },
        onOpenStore = { app ->
            telemetry.logAppInteraction(MoreAppsViewModel.INTERACTION_SOURCE, app, AppInteractionType.OpenInPlayStore)
            appActionLauncher.openPlayStore(app.packageName)
        },
        onViewAll = {
            telemetry.logSelectContent(contentType = "developer_page", itemId = AppLinks.DEFAULT_DEVELOPER_ID)
            context.openUrl(DeveloperPageUrl)
        },
        onDetailsClosed = { app ->
            telemetry.logAppInteraction(MoreAppsViewModel.INTERACTION_SOURCE, app, AppInteractionType.CloseDetailsBottomSheet)
        },
        onFavoriteToggle = { app ->
            telemetry.logAppInteraction(
                MoreAppsViewModel.INTERACTION_SOURCE,
                app,
                if (app.packageName in state.favorites) AppInteractionType.RemoveFavorite else AppInteractionType.AddFavorite,
            )
            viewModel.onEvent(MoreAppsEvent.FavoriteToggled(app.packageName))
        },
        detailsActionLauncher = detailsActionLauncher,
        detailsAdsConfig = detailsAdsConfig,
        modifier = modifier,
    )
}

/**
 * Renders [state] with the library's [AppShowcase], and the details sheet of the selected app.
 * Holds no ViewModel and opens nothing itself, so it renders in tests with plain values.
 *
 * @param onOpenApp An installed app's card or button was tapped.
 * @param onOpenStore A not-installed app's button was tapped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreAppsSectionContent(
    state: MoreAppsUiState,
    onEvent: (MoreAppsEvent) -> Unit,
    onOpenApp: (AppSummary) -> Unit,
    onOpenStore: (AppSummary) -> Unit,
    onViewAll: () -> Unit,
    onDetailsClosed: (AppSummary) -> Unit,
    onFavoriteToggle: (AppSummary) -> Unit,
    detailsActionLauncher: AppActionLauncher,
    modifier: Modifier = Modifier,
    detailsAdsConfig: AdsConfig = AdsConfig(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val apps = (state.apps as? Loadable.Ready)?.value.orEmpty()
    val summaries = remember(apps) { apps.associateBy { it.packageName } }
    val showcase: AppShowcaseState = when (val loadable = state.apps) {
        is Loadable.Ready -> AppShowcaseState.Ready(
            loadable.value.map { it.toShowcaseApp(installed = it.packageName in state.installedPackages) }.toImmutableList(),
        )
        is Loadable.Empty -> AppShowcaseState.Empty
        is Loadable.Failed -> AppShowcaseState.Failed
        else -> AppShowcaseState.Loading
    }
    AppShowcase(
        state = showcase,
        labels = moreAppsLabels(),
        icon = MoreAppsIcon,
        onAppClick = { app ->
            val summary = summaries[app.key] ?: return@AppShowcase
            if (app.installed) onOpenApp(summary) else onEvent(MoreAppsEvent.AppSelected(app.key))
        },
        onAppAction = { app ->
            val summary = summaries[app.key] ?: return@AppShowcase
            if (app.installed) onOpenApp(summary) else onOpenStore(summary)
        },
        onViewAll = onViewAll,
        onRetry = { onEvent(MoreAppsEvent.Load) },
        modifier = modifier,
        contentPadding = contentPadding,
    )

    state.selectedApp?.let { app ->
        val sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
        val coroutineScope = rememberCoroutineScope()
        val details = state.selectedAppDetails
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = {
                onDetailsClosed(app)
                coroutineScope.launch {
                    sheetState.hide()
                    onEvent(MoreAppsEvent.AppDetailsDismissed)
                }
            },
        ) {
            AppDetailsBottomSheet(
                appInfo = app,
                appDetails = (details as? Loadable.Ready)?.value,
                isDetailsLoading = details is Loadable.Loading,
                hasDetailsError = details is Loadable.Failed,
                isFavorite = app.packageName in state.favorites,
                isAppInstalled = state.selectedAppInstallInfo?.isInstalled,
                installedVersionInfo = state.selectedAppInstallInfo?.versionInfo,
                actionLauncher = detailsActionLauncher,
                onFavoriteClick = { onFavoriteToggle(app) },
                onRetryDetails = { onEvent(MoreAppsEvent.RetryAppDetails) },
                adsConfig = detailsAdsConfig,
            )
        }
    }
}

@Composable
private fun moreAppsLabels(): AppShowcaseLabels {
    val storeDescription = stringResource(R.string.more_apps_store_action_description)
    val openDescription = stringResource(R.string.more_apps_open_action_description)
    return appShowcaseLabels(
        title = stringResource(R.string.more_apps_title),
        viewAll = stringResource(R.string.more_apps_view_all),
        viewAllDescription = stringResource(R.string.more_apps_view_all_description),
        store = stringResource(R.string.more_apps_google_play),
        open = stringResource(R.string.more_apps_open),
        empty = stringResource(R.string.more_apps_empty),
        failed = stringResource(R.string.error_failed_to_load_apps),
        storeActionDescription = { name -> storeDescription.format(name) },
        openActionDescription = { name -> openDescription.format(name) },
    )
}

/** The card the library draws for this app: its catalogue category, or nothing when it has none. */
internal fun AppSummary.toShowcaseApp(installed: Boolean): ShowcaseApp = ShowcaseApp(
    key = packageName,
    name = name,
    category = category?.label.orEmpty(),
    description = shortDescription,
    iconUrl = iconUrl,
    installed = installed,
)
