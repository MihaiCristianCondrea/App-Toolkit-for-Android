package com.d4rk.android.libs.apptoolkit.app.help.ui.components

import android.text.method.LinkMovementMethod
import android.view.SoundEffectConstants
import android.view.View
import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.OutlinedIconButton
import com.d4rk.android.libs.apptoolkit.core.ui.components.modifiers.bounceClick
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.LargeHorizontalSpacer
import com.d4rk.android.libs.apptoolkit.core.ui.components.spacers.SmallVerticalSpacer
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuestionCard(title : String , summary : String , isExpanded : Boolean , onToggleExpand : () -> Unit , modifier : Modifier = Modifier) {
    val hapticFeedback : HapticFeedback = LocalHapticFeedback.current
    val view : View = LocalView.current
    val expandIconRotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f , label = "ExpandIconRotation")
    Card(modifier = modifier
            .bounceClick()
            .clip(shape = RoundedCornerShape(size = SizeConstants.MediumSize))
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                hapticFeedback.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.ContextClick)
                onToggleExpand()
            }
            .padding(all = SizeConstants.LargeSize)
            .animateContentSize()
            .fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically , modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.QuestionAnswer , contentDescription = null , tint = MaterialTheme.colorScheme.primary , modifier = Modifier
                            .size(size = SizeConstants.ExtraExtraLargeSize)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer , shape = CircleShape
                            )
                            .padding(all = SizeConstants.SmallSize)
                )

                LargeHorizontalSpacer()

                Text(
                    text = title , style = MaterialTheme.typography.titleMedium , modifier = Modifier.weight(weight = 1f)
                )

                OutlinedIconButton(
                    onClick = { onToggleExpand() } ,
                    icon = Icons.Filled.ExpandMore ,
                    modifier = Modifier.rotate(degrees = expandIconRotation) ,
                )
            }
            if (isExpanded) {
                SmallVerticalSpacer()
                HtmlText(
                    text = summary ,
                    style = MaterialTheme.typography.bodyMedium ,
                )
            }
        }
    }
}

// TODO: Move in the common composables from core
@Composable
private fun HtmlText(text : String , modifier : Modifier = Modifier , style : TextStyle) {
    val context = LocalContext.current
    val color = MaterialTheme.colorScheme.onSurface
    val fontSize = if (style.fontSize.isSpecified) style.fontSize.value else MaterialTheme.typography.bodyMedium.fontSize.value
    AndroidView(
        modifier = modifier.fillMaxWidth() ,
        factory = {
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        } ,
        update = { textView : TextView ->
            textView.text = HtmlCompat.fromHtml(text , HtmlCompat.FROM_HTML_MODE_COMPACT)
            textView.setTextColor(color.toArgb())
            textView.textSize = fontSize
        }
    )
}
