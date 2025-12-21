package com.d4rk.android.libs.apptoolkit.app.licenses.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.components.navigation.LargeTopAppBarWithScaffold
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(onBackClicked: (() -> Unit)? = null) {
    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    val activity = LocalActivity.current
    val defaultBackClicked: () -> Unit = remember(activity) { { activity?.finish() } }
    val backClicked: () -> Unit = onBackClicked ?: defaultBackClicked


    LargeTopAppBarWithScaffold(
        title = stringResource(id = R.string.oss_license_title),
        onBackClicked = backClicked,
        scrollBehavior = scrollBehavior
    ) { paddingValues ->
        val libraries: Libs? by produceLibraries(resId = R.raw.aboutlibraries)

        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            padding = LibraryDefaults.libraryPadding(),
            dimensions = LibraryDefaults.libraryDimensions(),
            showDescription = true,
            showFundingBadges = true,
        )
    }
}
