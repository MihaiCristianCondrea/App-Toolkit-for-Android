package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.default.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultOnboardingIconContainer(imageVector: ImageVector) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(
            modifier = Modifier.size(SizeConstants.TwoHundredFiftySixSize)
        )

        Box(
            modifier = Modifier
                .padding(SizeConstants.LargeSize), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(SizeConstants.EightySize),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
