package com.d4rk.android.libs.apptoolkit.app.help.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.app.help.domain.data.model.FaqItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HelpUiState(
    val questions: ImmutableList<FaqItem> = persistentListOf()
)