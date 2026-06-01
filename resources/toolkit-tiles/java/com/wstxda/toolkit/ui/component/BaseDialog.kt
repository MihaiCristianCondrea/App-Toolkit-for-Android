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

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseDialog<VB : ViewBinding> : DialogFragment() {

    companion object {
        fun DialogFragment.showSafely(
            fragmentManager: FragmentManager,
            tag: String,
        ) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                show(fragmentManager, tag)
            }
        }
    }

    private var _binding: VB? = null

    protected val binding: VB
        get() = requireNotNull(_binding)

    protected abstract fun inflateBinding(inflater: LayoutInflater): VB
    protected abstract fun onSetupDialog(savedInstanceState: Bundle?)

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = inflateBinding(requireActivity().layoutInflater)

        onSetupDialog(savedInstanceState)

        return MaterialAlertDialogBuilder(requireContext()).setView(binding.root).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}