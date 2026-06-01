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

package com.wstxda.toolkit.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.google.android.material.divider.MaterialDivider
import com.wstxda.toolkit.R
import com.wstxda.toolkit.databinding.DialogWriteSecureSettingsBinding
import com.wstxda.toolkit.ui.utils.Haptics

open class WriteSecureSettingsBottomSheet : BaseBottomSheet<DialogWriteSecureSettingsBinding>() {

    private lateinit var haptics: Haptics

    override val topDivider: MaterialDivider get() = binding.dividerTop
    override val bottomDivider: MaterialDivider get() = binding.dividerBottom
    override val scrollView: NestedScrollView get() = binding.scrollView
    override val titleTextView: TextView get() = binding.dialogTitle
    override val titleResId: Int get() = R.string.write_secure_settings_title

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogWriteSecureSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        haptics = Haptics(requireContext().applicationContext)

        val packageName = requireContext().packageName
        val command = "adb shell pm grant $packageName android.permission.WRITE_SECURE_SETTINGS"

        binding.apply {
            dialogMessage.text = getString(R.string.write_secure_settings_message)
            dialogAdbCommand.text = command
            dialogWarningMessage.text = getString(R.string.write_secure_settings_support)

            dialogButtonNegative.apply {
                text = getString(android.R.string.cancel)
                setOnClickListener {
                    haptics.low()
                    dismiss()
                }
            }

            dialogButtonPositive.apply {
                text = getString(R.string.copy_clipboard)
                setOnClickListener {
                    haptics.low()
                    copyToClipboard(command)
                    dismiss()
                }
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("ADB Command", text))
        Toast.makeText(
            requireContext(), R.string.write_secure_settings_clipboard_message, Toast.LENGTH_SHORT
        ).show()
    }
}