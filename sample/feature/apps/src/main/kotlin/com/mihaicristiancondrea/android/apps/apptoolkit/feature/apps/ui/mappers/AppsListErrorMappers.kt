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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.mappers

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.Loadable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.UiMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toErrorMessage
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toFailed
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen.toUiText

private val LoadAppsFailedText = UiTextHelper.StringResource(R.string.error_failed_to_load_apps)
private val UpdateFavoriteFailedText = UiTextHelper.StringResource(R.string.error_failed_to_update_favorite)
private val TryAgainText = UiTextHelper.StringResource(CoreUiR.string.try_again)

/**
 * This failure as the catalogue's [Loadable.Failed], for a load with no saved catalogue to show.
 *
 * The apps list throws no failures of its own, so every one gets the shared text from `toUiText`,
 * or "Failed to load apps" when there is none.
 */
internal fun Throwable.toCatalogueFailed(): Loadable.Failed = toFailed(fallback = LoadAppsFailedText)

/**
 * This failure as the message shown over the saved catalogue, with a Try again action that loads
 * the catalogue again.
 */
internal fun Throwable.toStaleCatalogueMessage(): UiMessage = UiMessage(
    text = toUiText(fallback = LoadAppsFailedText),
    isError = true,
    actionLabel = TryAgainText,
)

/** This failure as the details sheet's [Loadable.Failed]. */
internal fun Throwable.toAppDetailsFailed(): Loadable.Failed = toFailed(fallback = LoadAppsFailedText)

/** This failure as the message shown when adding or removing a favorite failed. */
internal fun Throwable.toFavoriteErrorMessage(): UiMessage = toErrorMessage(fallback = UpdateFavoriteFailedText)
