package com.d4rk.android.libs.apptoolkit.core.ui.components.text

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val context = LocalContext.current
    val color = MaterialTheme.colorScheme.onSurface
    val fontSize =
        if (style.fontSize.isSpecified) style.fontSize.value else MaterialTheme.typography.bodyMedium.fontSize.value
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView: TextView ->
            textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            textView.setTextColor(color.toArgb())
            textView.textSize = fontSize
        }
    )
}
