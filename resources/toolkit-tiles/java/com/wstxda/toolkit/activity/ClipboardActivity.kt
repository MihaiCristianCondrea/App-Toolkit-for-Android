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

package com.wstxda.toolkit.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.clipboard.ClipboardDispatcher

class ClipboardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isCleared = ClipboardDispatcher.clearClipboard(this)

        Toast.makeText(
            this,
            if (isCleared) getString(R.string.clipboard_cleared) else getString(R.string.not_supported),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}