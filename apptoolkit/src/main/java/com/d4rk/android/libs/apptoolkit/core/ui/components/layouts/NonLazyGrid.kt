package com.d4rk.android.libs.apptoolkit.core.ui.components.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants

@Composable
fun NonLazyGrid(
    columns : Int , itemCount : Int , modifier : Modifier = Modifier, content : @Composable (Int) -> Unit,
) {
    Column(  modifier = modifier) {
        val rows : Int = (itemCount + columns - 1) / columns

        (0 until rows).forEach { row ->
            Row {
                (0 until columns).forEachIndexed { col: Int, _: Int ->
                    val index : Int = row * columns + col
                    if (index < itemCount) {
                        Box(
                            modifier = Modifier
                                    .weight(weight = 1f)
                                    .padding(all = SizeConstants.SmallSize)
                                    .aspectRatio(ratio = 1f)
                        ) {
                            content(index)
                        }
                    }
                    else {
                        Spacer(modifier = Modifier.weight(weight = 1f))
                    }
                }
            }
        }
    }
}