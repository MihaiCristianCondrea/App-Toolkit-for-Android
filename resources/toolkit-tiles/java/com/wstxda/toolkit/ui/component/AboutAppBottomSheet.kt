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

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.divider.MaterialDivider
import com.wstxda.toolkit.R
import com.wstxda.toolkit.databinding.DialogAboutAppBinding
import com.wstxda.toolkit.services.UpdaterService
import com.wstxda.toolkit.ui.adapter.AboutAppAdapter
import com.wstxda.toolkit.ui.utils.Haptics
import com.wstxda.toolkit.utils.Constants
import com.wstxda.toolkit.viewmodel.AboutAppViewModel

class AboutAppBottomSheet : BaseBottomSheet<DialogAboutAppBinding>() {

    private lateinit var haptics: Haptics
    private val viewModel: AboutAppViewModel by viewModels()

    override val topDivider: MaterialDivider get() = binding.dividerTop
    override val bottomDivider: MaterialDivider get() = binding.dividerBottom
    override val scrollView: NestedScrollView get() = binding.scrollView
    override val defaultExpanded: Boolean = true

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogAboutAppBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        haptics = Haptics(requireContext().applicationContext)

        val adapter = AboutAppAdapter(
            onUrlClick = viewModel::openUrl, onActionClick = {
                LibraryBottomSheet().show(parentFragmentManager, Constants.LIBRARY_DIALOG)
            })
        binding.dialogRecyclerLinks.adapter = adapter

        viewModel.applicationVersion.observe(viewLifecycleOwner) { version ->
            binding.dialogButtonUpdate.text = getString(R.string.about_version, version)

            binding.dialogButtonUpdate.setOnClickListener {
                haptics.low()
                UpdaterService.checkForUpdates(
                    scope = lifecycleScope,
                    context = requireContext(),
                    fragmentManager = parentFragmentManager,
                    anchorView = it
                )
            }

            binding.dialogIconContainer.setOnClickListener {
                haptics.low()
                viewModel.openAppInfo()
            }
        }

        viewModel.links.observe(viewLifecycleOwner) { links ->
            adapter.submitList(links)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}