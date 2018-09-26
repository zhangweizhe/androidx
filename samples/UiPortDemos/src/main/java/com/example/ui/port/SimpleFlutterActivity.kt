/*
 * Copyright 2018 The Android Open Source Project
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

package com.example.ui.port

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.ui.CraneView
import android.widget.ImageView
import android.widget.Toast
import androidx.ui.foundation.Key
import androidx.ui.painting.Image
import androidx.ui.painting.alignment.Alignment
import androidx.ui.widgets.basic.Align
import androidx.ui.widgets.basic.RawImage
import androidx.ui.widgets.framework.BuildContext
import androidx.ui.widgets.framework.State
import androidx.ui.widgets.framework.StatefulWidget
import androidx.ui.widgets.framework.Widget
import androidx.ui.widgets.view.createViewWidget

class SimpleFlutterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(CraneView(this, createTestMirrorImageWidget()))
    }

    private fun createTestMirrorImageWidget(): MirrorImageWidget {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
        return MirrorImageWidget(Key.createKey("jetpack image widget!"), bitmap)
    }

    class MirrorImageWidget(key: Key, private val bitmap: Bitmap) : StatefulWidget(key) {
        override fun createState() =
                MirrorImageWidgetState(this, bitmap) as State<StatefulWidget>
    }

    class MirrorImageWidgetState(
        widget: MirrorImageWidget,
        private var bitmap: Bitmap
    ) : State<MirrorImageWidget>(widget) {

        private var showViewWidget = true

        init {
            fun mirrorImageDelayed() {
                Handler(Looper.getMainLooper()).postDelayed({
                    setState {
                        showViewWidget = !showViewWidget
                    }
                    mirrorImageDelayed()
                }, 700 /* 700 milliseconds */)
            }

            mirrorImageDelayed()

            val prevBitmap = bitmap
            bitmap = bitmap.mirror()
            prevBitmap.recycle()
        }

        override fun build(context: BuildContext): Widget {
            return Align(
                key = Key.createKey("alignWidget"),
                alignment = Alignment.center,
                child = createContentWidget()
            )
        }

        private fun createContentWidget(): Widget {
            return if (showViewWidget) {
                return createViewWidget(Key.createKey("testViewWidget")) {
                        ctx: Context ->
                    val imageView = ImageView(ctx)
                    imageView.setImageResource(R.drawable.test)
                    imageView.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    imageView.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            Toast.makeText(v!!.context, "Clicked ImageView!",
                                Toast.LENGTH_SHORT).show()
                        }
                    })
                    imageView
                }
            } else {
                return RawImage(
                    image = Image(bitmap),
                    key = Key.createKey("jetpack image")
                )
            }
        }

        private fun Bitmap.mirror(): Bitmap {
            val m = Matrix().apply {
                preScale(-1f, 1f)
            }
            return Bitmap.createBitmap(this, 0, 0, width, height, m, false)
        }
    }
}
