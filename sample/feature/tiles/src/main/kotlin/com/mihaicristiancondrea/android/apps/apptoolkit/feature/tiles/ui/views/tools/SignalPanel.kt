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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.tools

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.icons.ToolSymbols
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing

@Composable
internal fun SignalPanel(active: Boolean, message: String, code: String) {
    val outer = MaterialTheme.shapes.extraLarge
    val inner = MaterialTheme.shapes.small
    Surface(shape = outer.copy(bottomStart = inner.bottomStart, bottomEnd = inner.bottomEnd), color = MaterialTheme.colorScheme.tertiaryContainer) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmallIncreased),
        ) {
            Icon(ToolSymbols.Emergency, null, Modifier.size(size = MaterialTheme.size.extraExtraLarge))
            Text(message, style = MaterialTheme.typography.displaySmall, maxLines = 2)
            Text(stringResource(if (active) R.string.morse_active else R.string.morse_ready), style = MaterialTheme.typography.labelLarge)
        }
    }
    Surface(shape = outer.copy(topStart = inner.topStart, topEnd = inner.topEnd), color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Box(Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium), contentAlignment = Alignment.Center) {
            Text(code, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
