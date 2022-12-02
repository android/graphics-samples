package com.android.textureview_surfaceview.examples.single

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display.HdrCapabilities
import android.view.SurfaceHolder
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.decoder.CustomVideoDecoder

class SurfaceViewVideoPlayerHDR : SurfaceViewVideoPlayer() {

    private lateinit var decoder: CustomVideoDecoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.surfaceViewVideoPlayerTitle.text =
            "SurfaceView : HDR? = ${isHDRSupported()}"
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val assetFile = applicationContext.assets.openFd(Constants.HDR_VIDEO_PLAYER_ASSET)
        decoder = CustomVideoDecoder.buildWithAssetFile(assetFile)
        decoder.setSurface(holder.surface)
        decoder.start(loop = true)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        decoder.stop()
    }

    private fun isHDRSupported(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val hdrCapabilities = display?.hdrCapabilities
            val types: IntArray = hdrCapabilities?.supportedHdrTypes ?: IntArray(0)
            if (types.isNotEmpty()) {
                val typeToName: Map<Int, String> =
                    hashMapOf(
                        Pair(HdrCapabilities.HDR_TYPE_DOLBY_VISION, "Dolby Vision"),
                        Pair(HdrCapabilities.HDR_TYPE_HLG, "HLG"),
                        Pair(HdrCapabilities.HDR_TYPE_HDR10, "HDR 10"),
                        Pair(HdrCapabilities.HDR_TYPE_HDR10_PLUS, "HDR 10+")
                    )

                for (i in types.indices) {
                    Log.i(
                        SurfaceViewVideoPlayerHDR::class.java.name,
                        "Supported HDR TYPE: " + typeToName[types[i]]
                    )
                }
                return true
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val configuration = resources.configuration
            return configuration.isScreenHdr
        }

        return false
    }
}

