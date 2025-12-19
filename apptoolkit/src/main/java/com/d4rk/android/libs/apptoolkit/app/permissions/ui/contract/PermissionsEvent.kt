package com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent

/**
 * Events that [PermissionsViewModel] can react to.
 *
 * Keeping events free of any Android framework dependencies ensures the
 * ViewModel remains agnostic of the Android lifecycle as recommended by the
 * Android architecture guidelines.
 */
sealed interface PermissionsEvent : UiEvent {
    /**
     * Requests that the ViewModel loads the permissions configuration.
     */
    data object Load : PermissionsEvent
}

