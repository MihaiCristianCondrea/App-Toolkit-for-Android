package com.d4rk.android.libs.apptoolkit.app.permissions.ui.contract

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent

/**
 * Side-effect actions sent from [com.d4rk.android.libs.apptoolkit.app.permissions.ui.PermissionsViewModel]
 * to the UI layer.
 *
 * Currently no actions are defined but this sealed interface leaves room for
 * future one-off events such as navigation.
 */
sealed interface PermissionsAction : ActionEvent

