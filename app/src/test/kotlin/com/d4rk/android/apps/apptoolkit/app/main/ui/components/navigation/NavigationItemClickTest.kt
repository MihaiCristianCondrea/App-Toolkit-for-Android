package com.d4rk.android.apps.apptoolkit.app.main.ui.components.navigation

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.ui.graphics.vector.ImageVector
import com.d4rk.android.libs.apptoolkit.app.help.ui.HelpActivity
import com.d4rk.android.libs.apptoolkit.app.main.utils.constants.NavigationDrawerRoutes
import com.d4rk.android.libs.apptoolkit.app.settings.settings.ui.SettingsActivity
import com.d4rk.android.libs.apptoolkit.core.domain.model.navigation.NavigationDrawerItem
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.core.utils.helpers.IntentsHelper
import io.mockk.Called
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationItemClickTest {

    private val context = mockk<Context>(relaxed = true)

    private fun navigationItem(route: String) = NavigationDrawerItem(
        title = 0,
        selectedIcon = ImageVector.Builder(
            name = "test",
            defaultWidth = SizeConstants.TwentyFourSize,
            defaultHeight = SizeConstants.TwentyFourSize,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).build(),
        route = route
    )

    @BeforeEach
    fun setup() {
        mockkObject(IntentsHelper)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `settings route opens settings activity and closes drawer`() = runTest {
        every { IntentsHelper.openActivity(context, SettingsActivity::class.java) } returns true
        val drawerState = mockk<DrawerState>()
        coEvery { drawerState.close() } just Runs

        handleNavigationItemClick(
            context = context,
            item = navigationItem(NavigationDrawerRoutes.ROUTE_SETTINGS),
            drawerState = drawerState,
            coroutineScope = this
        )
        advanceUntilIdle()

        verify(exactly = 1) { IntentsHelper.openActivity(context, SettingsActivity::class.java) }
        coVerify(exactly = 1) { drawerState.close() }
        confirmVerified(IntentsHelper, drawerState)
    }

    @Test
    fun `help and feedback route opens help activity and closes drawer`() = runTest {
        every { IntentsHelper.openActivity(context, HelpActivity::class.java) } returns true
        val drawerState = mockk<DrawerState>()
        coEvery { drawerState.close() } just Runs

        handleNavigationItemClick(
            context = context,
            item = navigationItem(NavigationDrawerRoutes.ROUTE_HELP_AND_FEEDBACK),
            drawerState = drawerState,
            coroutineScope = this
        )
        advanceUntilIdle()

        verify(exactly = 1) { IntentsHelper.openActivity(context, HelpActivity::class.java) }
        coVerify(exactly = 1) { drawerState.close() }
        confirmVerified(IntentsHelper, drawerState)
    }

    @Test
    fun `updates route triggers changelog callback and closes drawer`() = runTest {
        val drawerState = mockk<DrawerState>()
        coEvery { drawerState.close() } just Runs
        var changelogRequests = 0

        handleNavigationItemClick(
            context = context,
            item = navigationItem(NavigationDrawerRoutes.ROUTE_UPDATES),
            drawerState = drawerState,
            coroutineScope = this,
            onChangelogRequested = { changelogRequests++ }
        )
        advanceUntilIdle()

        assertEquals(1, changelogRequests)
        verify { IntentsHelper wasNot Called }
        coVerify(exactly = 1) { drawerState.close() }
        confirmVerified(drawerState)
    }

    @Test
    fun `share route invokes share app and closes drawer`() = runTest {
        every {
            IntentsHelper.shareApp(
                context,
                com.d4rk.android.libs.apptoolkit.R.string.summary_share_message
            )
        } returns true
        val drawerState = mockk<DrawerState>()
        coEvery { drawerState.close() } just Runs

        handleNavigationItemClick(
            context = context,
            item = navigationItem(NavigationDrawerRoutes.ROUTE_SHARE),
            drawerState = drawerState,
            coroutineScope = this
        )
        advanceUntilIdle()

        verify(exactly = 1) {
            IntentsHelper.shareApp(
                context,
                com.d4rk.android.libs.apptoolkit.R.string.summary_share_message
            )
        }
        coVerify(exactly = 1) { drawerState.close() }
        confirmVerified(IntentsHelper, drawerState)
    }

    @Test
    fun `unknown route produces no action`() {
        var changelogInvoked = false

        handleNavigationItemClick(
            context = context,
            item = navigationItem("unknown"),
            onChangelogRequested = { changelogInvoked = true }
        )

        assertFalse(changelogInvoked)
        verify { IntentsHelper wasNot Called }
    }
}
