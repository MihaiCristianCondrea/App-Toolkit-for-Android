package com.d4rk.android.libs.apptoolkit.app.diagnostics.ui.views.headers

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

/**
 * A composable that displays a styled header for a section within a consent screen.
 *
 * This is typically used to visually separate and title different parts of a consent-related UI,
 * such as "Data Collection" or "Third-Party Services". It uses the `titleMedium` typography
 * from the current [MaterialTheme], with a semi-bold weight and the primary color scheme color.
 *
 * @param title The text to be displayed as the header title.
 */
@Composable
fun ConsentSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            top = SizeConstants.LargeSize,
            bottom = SizeConstants.SmallSize,
            start = SizeConstants.SmallSize,
            end = SizeConstants.SmallSize
        )
    )
}
