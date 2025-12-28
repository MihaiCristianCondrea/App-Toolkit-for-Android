package com.d4rk.android.libs.apptoolkit.core.utils.platform

import android.content.Context
import android.widget.Toast
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.di.DispatcherProvider
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.context.safeStartActivity
import kotlinx.coroutines.withContext

open class AppInfoHelper(
    private val dispatchers: DispatcherProvider,
) {

    /**
     * Checks if a specific app is installed on the device.
     * @return True if the app is installed, false otherwise.
     */
    suspend fun isAppInstalled(context: Context, packageName: String): Boolean =
        withContext(dispatchers.io) {
            runCatching { context.packageManager.getApplicationInfo(packageName, 0) }.isSuccess
        }

    /**
     * Opens a specific app if installed.
     *
     * Returns `true` if the app was successfully launched and `false` otherwise.
     * For callers that need to react programmatically to failures, use
     * [openAppResult].
     */
    suspend fun openApp(context: Context, packageName: String): Boolean =
        openAppResult(context = context, packageName = packageName).getOrElse { false }

    /**
     * Opens a specific app if installed and exposes the operation result.
     *
     * @return A [Result] containing `true` when the app was launched or a failure when
     *         the launch intent could not be obtained or starting the activity failed.
     */
    suspend fun openAppResult(context: Context, packageName: String): Result<Boolean> {
        val launchIntent = withContext(dispatchers.io) {
            runCatching { context.packageManager.getLaunchIntentForPackage(packageName) }.getOrNull()
        }

        return if (launchIntent != null) {
            val launched = context.safeStartActivity(
                intent = launchIntent,
                onFailure = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.app_not_installed),
                        Toast.LENGTH_SHORT
                    ).show()
                },
            )
            if (launched) {
                Result.success(true)
            } else {
                Result.failure(IllegalStateException("App not installed"))
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.app_not_installed),
                Toast.LENGTH_SHORT
            ).show()
            Result.failure(IllegalStateException("App not installed"))
        }
    }
}
