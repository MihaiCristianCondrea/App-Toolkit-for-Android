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

package com.d4rk.android.libs.apptoolkit.core.ui.views.layouts.sections

import android.content.Context
import android.util.Log
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.spacers.MediumVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.views.text.LearnMoreText
import com.d4rk.android.libs.apptoolkit.core.utils.constants.logging.INFO_MESSAGE_LOG_TAG
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.openUrl


/**
 * Displays an information message section with an optional "Learn More" link.
 *
 * This composable displays a section containing:
 * - An info icon.
 * - A text message.
 * - Optionally, a clickable "Learn More" link that opens a specified URL.
 *
 * @param message The main message to be displayed.
 * @param modifier The modifier to be applied to the container (Column).
 * @param learnMoreText The text to be displayed for the "Learn More" link. If null or empty, the link will not be shown.
 * @param learnMoreUrl The URL to be opened when the "Learn More" link is clicked. If null or empty, the link will not be shown.
 *
 * Example Usage:
 * ```
 * import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
 *
 * InfoMessageSection(
 *     message = "This is an important information message.",
 *     modifier = Modifier.padding(SizeConstants.LargeSize),
 *     learnMoreText = "Learn More",
 *     learnMoreUrl = "https://www.example.com"
 * )
 *
 * InfoMessageSection(
 *     message = "Another message without a learn more link",
 */
@Composable
fun InfoMessageSection(
    message: String,
    modifier: Modifier = Modifier,
    learnMoreText: String? = null,
    learnMoreUrl: String? = null,
    learnMoreAction: (() -> Unit)? = null,
    newLine: Boolean = true
) {
    val context: Context = LocalContext.current
    val hapticFeedback: HapticFeedback = LocalHapticFeedback.current
    val view: View = LocalView.current
    val hasLearnMore =
        !learnMoreText.isNullOrEmpty() && (learnMoreAction != null || !learnMoreUrl.isNullOrEmpty())

    Column(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(id = R.string.about)
        )

        MediumVerticalSpacer()

        when {
            newLine || !hasLearnMore -> {
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
                if (hasLearnMore) {
                    LearnMoreText(
                        text = learnMoreText,
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                            when {
                                learnMoreAction != null -> learnMoreAction()
                                !learnMoreUrl.isNullOrEmpty() -> {
                                    if (!context.openUrl(learnMoreUrl)) {
                                        Log.w(
                                            INFO_MESSAGE_LOG_TAG,
                                            "Failed to open learn more url: $learnMoreUrl"
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            else -> {
                val annotatedString = buildAnnotatedString {
                    append("$message ")

                    val linkInteraction = LinkInteractionListener {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                        when {
                            learnMoreAction != null -> learnMoreAction()
                            !learnMoreUrl.isNullOrEmpty() -> {
                                if (!context.openUrl(learnMoreUrl)) {
                                    Log.w(
                                        INFO_MESSAGE_LOG_TAG,
                                        "Failed to open learn more url: $learnMoreUrl"
                                    )
                                }
                            }
                        }
                    }

                    pushLink(
                        link = LinkAnnotation.Clickable(
                            tag = "learn_more",
                            linkInteractionListener = linkInteraction
                        )
                    )

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(learnMoreText)
                    }

                    pop()
                }

                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
