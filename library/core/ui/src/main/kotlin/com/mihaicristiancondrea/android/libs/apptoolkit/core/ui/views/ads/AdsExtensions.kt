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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.runtime.Composable
import com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.LocalAdsEnabled

/**
 * A Composable function that remembers and observes whether ads are enabled.
 *
 * Change rationale: this used to build its own flow with a hardcoded `default = true`, while
 * Ads initialization and [AppTheme][com.mihaicristiancondrea.android.libs.apptoolkit.app.theme.ui.style.AppTheme]
 * read the same preference source, while ad slots consume the provided UI value.
 * When the two disagreed, ad views loaded ads for an SDK that had never been initialized, and the
 * The host-configured default is therefore preserved without creating one collector per ad slot.
 *
 * @return `true` if ads are enabled, `false` otherwise. The value is lifecycle-aware.
 */
@Composable
fun rememberAdsEnabled(): Boolean = LocalAdsEnabled.current
