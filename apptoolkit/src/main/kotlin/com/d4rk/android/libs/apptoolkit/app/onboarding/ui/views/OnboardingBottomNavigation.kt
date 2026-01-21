package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralButton
import com.d4rk.android.libs.apptoolkit.core.ui.views.buttons.GeneralOutlinedButton
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun OnboardingBottomNavigation(
    pagerState: PagerState, pageCount: Int, onNextClicked: () -> Unit, onBackClicked: () -> Unit
) {

    FlexibleBottomAppBar(
        contentPadding = PaddingValues(
            horizontal = SizeConstants.LargeSize,
            vertical = SizeConstants.SmallSize
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage > 0,
                enter = slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
            ) {
                GeneralOutlinedButton(
                    onClick = onBackClicked,
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    iconContentDescription = stringResource(id = R.string.back_button_content_description),
                    label = stringResource(id = R.string.back_button_text)
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            PageIndicatorDots(
                pagerState = pagerState,
                pageCount = pageCount
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .animateContentSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            val isLastPage = pagerState.currentPage == pageCount - 1
            GeneralButton(
                onClick = onNextClicked,
                modifier = Modifier.animateContentSize(),
                icon = if (isLastPage) Icons.Filled.Check else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                iconContentDescription = if (isLastPage) stringResource(id = R.string.done_button_content_description) else stringResource(
                    id = R.string.next_button_content_description
                ),
                label = if (isLastPage) stringResource(id = R.string.done_button_text) else stringResource(
                    id = R.string.next_button_text
                )
            )
        }
    }
}
