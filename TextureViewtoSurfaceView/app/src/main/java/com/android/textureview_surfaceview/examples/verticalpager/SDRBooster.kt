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
class SdrBooster(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    Consumer<Display> {

    private var hdrSdrRatio = 1.0f
    private val paint = Paint()
    private var scale = 1.0f

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

        if (hdrSdrRatio > 1.0f) {
            val ratioScale = scale + ((hdrSdrRatio - 1.0f) * .1f)
            paint.colorFilter = ColorMatrixColorFilter(
                floatArrayOf(
                    ratioScale, 0f, 0f, 0f, 0f,
                    0f, ratioScale, 0f, 0f, 0f,
                    0f, 0f, ratioScale, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            setLayerType(LAYER_TYPE_HARDWARE, paint)
        } else {
            setLayerType(LAYER_TYPE_NONE, null)
        }
    }

    fun setScale(newScale: Float) {
        scale = newScale
    }
}