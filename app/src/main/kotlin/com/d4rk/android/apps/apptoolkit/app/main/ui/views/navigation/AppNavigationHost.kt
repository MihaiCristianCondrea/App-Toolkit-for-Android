package com.d4rk.android.apps.apptoolkit.app.main.ui.views.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.d4rk.android.apps.apptoolkit.app.main.utils.constants.AppNavKey
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationAnimations
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationEntryBuilder
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.NavigationState
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.Navigator
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.entryProviderFor
import com.d4rk.android.libs.apptoolkit.core.ui.navigation.rememberNavigationEntryDecorators
import com.d4rk.android.libs.apptoolkit.core.utils.window.AppWindowWidthSizeClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navigationState: NavigationState<AppNavKey>,
    navigator: Navigator<AppNavKey>,
    paddingValues: PaddingValues,
    windowWidthSizeClass: AppWindowWidthSizeClass,
    onRandomAppHandlerChanged: (AppNavKey, RandomAppHandler?) -> Unit,
    additionalEntryBuilders: ImmutableList<NavigationEntryBuilder<AppNavKey>> = persistentListOf(),
) {
    val entryBuilders: List<NavigationEntryBuilder<AppNavKey>> =
        remember(
            paddingValues,
            windowWidthSizeClass,
            onRandomAppHandlerChanged,
            additionalEntryBuilders
        ) {
            val context = AppNavigationEntryContext(
                paddingValues = paddingValues,
                windowWidthSizeClass = windowWidthSizeClass,
                onRandomAppHandlerChanged = onRandomAppHandlerChanged,
            )
            appNavigationEntryBuilders(
                context = context,
                additionalEntryBuilders = additionalEntryBuilders,
            )
        }

    val entryDecorators = rememberNavigationEntryDecorators<AppNavKey>()

    val entryProvider: (AppNavKey) -> NavEntry<AppNavKey> =
        remember(entryBuilders) {
            entryProviderFor(entryBuilders)
        }

    NavDisplay(
        modifier = modifier,
        backStack = navigationState.currentBackStack,
        entryDecorators = entryDecorators,
        entryProvider = entryProvider,
        onBack = { navigator.goBack() },
        transitionSpec = { NavigationAnimations.default() },
        popTransitionSpec = { NavigationAnimations.default() },
        predictivePopTransitionSpec = { NavigationAnimations.default() },
    )
}