/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.ui.layout

import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.emit
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.LayoutEmitHelper
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.util.annotation.VisibleForTesting

/** @hide */
@Deprecated(
    "It is a test API, do not use it in the real applications",
    level = DeprecationLevel.ERROR
)
@VisibleForTesting
class TestModifierUpdater internal constructor(private val node: LayoutNode) {
    fun updateModifier(modifier: Modifier) {
        node.modifier = modifier
    }
}

/** @hide */
@Deprecated(
    "It is a test API, do not use it in the real applications",
    level = DeprecationLevel.ERROR
)
@VisibleForTesting
@Composable
@Suppress("DEPRECATION_ERROR")
fun TestModifierUpdaterLayout(onAttached: (TestModifierUpdater) -> Unit) {
    val measureBlocks = MeasuringIntrinsicsMeasureBlocks { _, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {}
    }
    emit<LayoutNode, Applier<Any>>(
        ctor = LayoutEmitHelper.constructor,
        update = {
            set(measureBlocks, LayoutEmitHelper.setMeasureBlocks)
            set(Unit) { onAttached(TestModifierUpdater(this)) }
        }
    )
}