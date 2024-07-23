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
class ColorDrawOverlay(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    Consumer<Display> {

    private val paint = Paint()


    override fun accept(display: Display) {

        paint.colorFilter = ColorMatrixColorFilter(
            floatArrayOf(
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        setLayerType(LAYER_TYPE_HARDWARE, paint)
    }
}
