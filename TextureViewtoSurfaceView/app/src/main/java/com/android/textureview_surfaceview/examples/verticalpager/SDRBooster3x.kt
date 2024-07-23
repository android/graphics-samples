package com.android.textureview_surfaceview.examples.verticalpager

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.Display
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class SdrBooster3x(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    Consumer<Display> {

    var hdrSdrRatio = 1.0f
    val paint = Paint()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        hdrSdrRatio = display.hdrSdrRatio
        if (display.isHdrSdrRatioAvailable) {
            display.registerHdrSdrRatioChangedListener({ post(it) }, this)
        }
    }

    override fun onDetachedFromWindow() {
        if (display.isHdrSdrRatioAvailable) {
            display.unregisterHdrSdrRatioChangedListener(this)
        }
        super.onDetachedFromWindow()
    }

    override fun accept(display: Display) {
        hdrSdrRatio = display.hdrSdrRatio

//        if (hdrSdrRatio > 1.0f) {
//            val scale = 3.0f + ((hdrSdrRatio - 1.0f) * .1f)
//            paint.colorFilter = ColorMatrixColorFilter(
//                floatArrayOf(
//                    scale, 0f, 0f, 0f, 0f,
//                    0f, scale, 0f, 0f, 0f,
//                    0f, 0f, scale, 0f, 0f,
//                    0f, 0f, 0f, 1f, 0f
//                )
//            )
//            setLayerType(LAYER_TYPE_HARDWARE, paint)
//        } else {
//            setLayerType(LAYER_TYPE_NONE, null)
//        }
    }
}