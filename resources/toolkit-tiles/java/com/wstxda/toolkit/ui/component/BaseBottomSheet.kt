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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDivider

abstract class BaseBottomSheet<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val topDivider: MaterialDivider
    protected abstract val bottomDivider: MaterialDivider
    protected open val scrollView: NestedScrollView? = null
    protected open val titleTextView: TextView? = null
    protected open val titleResId: Int? = null
    protected open val defaultExpanded: Boolean = false

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleResId?.let { resId -> titleTextView?.text = getString(resId) }
        setupContentFragment(savedInstanceState)
        setupScrollListener()
    }

    override fun onStart() {
        super.onStart()
        if (defaultExpanded) {
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let { BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun setupContentFragment(savedInstanceState: Bundle?) {}

    protected open fun setupScrollListener() {
        scrollView?.setOnScrollChangeListener { _, _, _, _, _ ->
            updateDividerVisibility(
                scrollView!!.canScrollVertically(-1), scrollView!!.canScrollVertically(1)
            )
        }
    }

    protected fun updateDividerVisibility(canScrollUp: Boolean, canScrollDown: Boolean) {
        topDivider.isVisible = canScrollUp
        bottomDivider.isVisible = canScrollDown
    }
}