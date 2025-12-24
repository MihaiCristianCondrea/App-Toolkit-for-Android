package com.d4rk.android.libs.apptoolkit.core.utils.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.d4rk.android.libs.apptoolkit.core.utils.constants.permissions.PermissionsConstants
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestPermissionsHelper {

    @Test
    fun `hasNotificationPermission returns true for API 32 and below`() {
        val context = mockk<Context>()

        withStaticMocks(Build.VERSION::class, ContextCompat::class) {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.S_V2

            assertTrue(PermissionsHelper.hasNotificationPermission(context))

            verify(exactly = 0) {
                ContextCompat.checkSelfPermission(any(), any())
            }
        }
    }

    @Test
    fun `hasNotificationPermission reflects granted state on API 33`() {
        val context = mockk<Context>()

        withStaticMocks(Build.VERSION::class, ContextCompat::class) {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.TIRAMISU
            every {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } returnsMany listOf(
                PackageManager.PERMISSION_GRANTED,
                PackageManager.PERMISSION_DENIED
            )

            assertTrue(PermissionsHelper.hasNotificationPermission(context))
            assertFalse(PermissionsHelper.hasNotificationPermission(context))

            verify(exactly = 2) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    @Test
    fun `requestNotificationPermission requests on API 33 when missing`() {
        val activity = mockk<Activity>()

        withStaticMocks(Build.VERSION::class, ContextCompat::class, ActivityCompat::class) {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.TIRAMISU
            every {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } returns PackageManager.PERMISSION_DENIED

            justRun {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PermissionsConstants.REQUEST_CODE_NOTIFICATION_PERMISSION
                )
            }

            PermissionsHelper.requestNotificationPermission(activity)

            verify(exactly = 1) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PermissionsConstants.REQUEST_CODE_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    @Test
    fun `requestNotificationPermission skips when already granted on API 33`() {
        val activity = mockk<Activity>()

        withStaticMocks(Build.VERSION::class, ContextCompat::class, ActivityCompat::class) {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.TIRAMISU
            every {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } returns PackageManager.PERMISSION_GRANTED

            PermissionsHelper.requestNotificationPermission(activity)

            verify(exactly = 0) {
                ActivityCompat.requestPermissions(any(), any(), any())
            }
        }
    }

    @Test
    fun `requestNotificationPermission ignores API 32 and below`() {
        val activity = mockk<Activity>()

        withStaticMocks(Build.VERSION::class, ContextCompat::class, ActivityCompat::class) {
            every { Build.VERSION.SDK_INT } returns Build.VERSION_CODES.S_V2

            PermissionsHelper.requestNotificationPermission(activity)

            verify(exactly = 0) {
                ContextCompat.checkSelfPermission(any(), any())
            }
            verify(exactly = 0) {
                ActivityCompat.requestPermissions(any(), any(), any())
            }
        }
    }

    private inline fun <T> withStaticMocks(
        vararg targets: kotlin.reflect.KClass<*>,
        block: () -> T
    ): T {
        targets.forEach { mockkStatic(it) }

        return runCatching(block)
                .also {
                    targets.reversed().forEach { k ->
                        runCatching { unmockkStatic(k) }
                    }
                }
                .getOrThrow()
    }
}
