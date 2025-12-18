package com.d4rk.android.apps.apptoolkit.app.main.ui.components.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.apps.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.buttons.fab.AnimatedExtendedFloatingActionButton

@Composable
fun MainFloatingActionButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val label = stringResource(id = R.string.open_random_app)
    AnimatedExtendedFloatingActionButton(
        modifier = modifier,
        visible = visible,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Casino,
                contentDescription = label,
            )
        },
        text = {
            Text(text = label)
        },
        expanded = expanded,
    )
}
