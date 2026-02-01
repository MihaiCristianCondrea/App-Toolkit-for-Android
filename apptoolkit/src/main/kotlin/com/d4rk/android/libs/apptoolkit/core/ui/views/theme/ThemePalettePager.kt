package com.d4rk.android.libs.apptoolkit.core.ui.views.theme

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A horizontal pager used for theme palette selection.
 *
 * This shared container keeps onboarding and settings aligned on the same paging behavior
 * while allowing each screen to supply its own page content.
 */
@Composable
fun ThemePalettePager(
    pagerState: PagerState,
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        pages.getOrNull(page)?.invoke()
    }
}
