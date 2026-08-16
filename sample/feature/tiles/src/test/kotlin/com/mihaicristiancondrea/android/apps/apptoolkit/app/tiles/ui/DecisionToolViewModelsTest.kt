/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui

import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.CoinFlipToolState
import com.mihaicristiancondrea.android.apps.apptoolkit.app.tiles.ui.states.DiceRollToolState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class DecisionToolViewModelsTest {

    @Test
    fun `coin flip publishes a new request and resets on dismiss`() {
        val viewModel = CoinFlipToolViewModel()

        viewModel.flip()
        assertEquals(1, viewModel.state.value.request)

        viewModel.dismiss()
        assertEquals(CoinFlipToolState(), viewModel.state.value)
    }

    @Test
    fun `dice roll stays in range and resets on dismiss`() {
        val viewModel = DiceRollToolViewModel()

        viewModel.roll()
        assertTrue(viewModel.state.value.result in 1..6)
        assertEquals(1, viewModel.state.value.request)

        viewModel.dismiss()
        assertEquals(DiceRollToolState(), viewModel.state.value)
    }

    @Test
    fun `counter increments and resets`() {
        val viewModel = CounterToolViewModel()

        viewModel.increment()
        viewModel.increment()
        assertEquals(2, viewModel.count.value)

        viewModel.dismiss()
        assertEquals(0, viewModel.count.value)
    }
}
