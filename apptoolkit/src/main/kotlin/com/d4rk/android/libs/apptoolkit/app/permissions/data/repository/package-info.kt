/**
 * Repository implementations for permissions-related settings.
 *
 * Repositories act as data boundaries that can persist or observe state via suspend functions
 * or [kotlinx.coroutines.flow.Flow]. Prefer providers for static, read-only configuration, and
 * use repository implementations in this package when the consumer needs observable or async data.
 */
package com.d4rk.android.libs.apptoolkit.app.permissions.data.repository
