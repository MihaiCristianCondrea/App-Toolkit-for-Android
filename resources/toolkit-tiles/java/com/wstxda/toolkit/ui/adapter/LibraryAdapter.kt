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

package com.wstxda.toolkit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemLayout
import com.mikepenz.aboutlibraries.entity.Library
import com.wstxda.toolkit.databinding.ListItemLibraryBinding
import com.wstxda.toolkit.ui.utils.Haptics

class LibraryAdapter(
    private val onClick: (Library) -> Unit
) : ListAdapter<Library, LibraryAdapter.LibraryViewHolder>(LibraryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LibraryViewHolder(
        ListItemLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(getItem(position), position, itemCount)
    }

    inner class LibraryViewHolder(private val binding: ListItemLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val haptics = Haptics(itemView.context.applicationContext)

        fun bind(library: Library, position: Int, totalItems: Int) = with(binding) {
            titleItem.text = library.name

            val licenseName = library.licenses.firstOrNull()?.name

            if (!licenseName.isNullOrEmpty()) {
                licenseItem.text = licenseName
                licenseItem.isVisible = true
            } else {
                licenseItem.isVisible = false
            }

            val version = library.artifactVersion
            if (!version.isNullOrEmpty()) {
                versionChip.text = version
                versionChip.isVisible = true
            } else {
                versionChip.isVisible = false
            }

            cardItem.setOnClickListener {
                haptics.low()
                onClick(library)
            }

            val listItemLayout = itemView as ListItemLayout
            listItemLayout.updateAppearance(position, totalItems)
        }
    }
}