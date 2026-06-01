/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Inspired by FreeDroidWarn by woheller69
 * https://github.com/woheller69/FreeDroidWarn
 * Redesigned dialog with Material 3 Expressive design
 */

package com.wstxda.toolkit.ui.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.wstxda.toolkit.R
import com.wstxda.toolkit.databinding.DialogFreeAndroidWarnBinding
import com.wstxda.toolkit.utils.Constants

class FreeAndroidWarnDialog : BaseDialog<DialogFreeAndroidWarnBinding>() {

    companion object {
        fun show(fragmentManager: FragmentManager, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (!prefs.getBoolean(Constants.IS_WARN_DISMISSED, false)) {
                FreeAndroidWarnDialog().showSafely(
                    fragmentManager, Constants.FREE_ANDROID_WARN_DIALOG
                )
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater) =
        DialogFreeAndroidWarnBinding.inflate(inflater)

    override fun onSetupDialog(savedInstanceState: Bundle?) {
        binding.apply {
            dialogIcon.setImageResource(R.drawable.ic_warning)
            dialogTitle.text = getString(R.string.free_android_warn_title)
            dialogMessage.text = getString(R.string.free_android_warn_message)
            dialogButtonPositive.text = getString(android.R.string.ok)
            dialogButtonNegative.text = getString(R.string.free_android_warn_solution_button)

            dialogButtonPositive.setOnClickListener { onPositiveClicked() }
            dialogButtonNegative.setOnClickListener { openUrl("https://github.com/woheller69/FreeDroidWarn?tab=readme-ov-file#solutions") }
            dialogLinkMoreInfo.text = getString(R.string.free_android_warn_link)
            dialogLinkMoreInfo.setOnClickListener { openUrl("https://keepandroidopen.org") }
        }
    }

    private fun onPositiveClicked() {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
            putBoolean(Constants.IS_WARN_DISMISSED, true)
        }
        dismiss()
    }

    private fun openUrl(url: String) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, url.toUri())) }
        dismiss()
    }
}