package com.android.textureview_surfaceview.examples.multi

import android.app.Activity
import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import android.widget.Toast
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.MultiViewPlayerHdrTransformerBinding
import com.android.textureview_surfaceview.decoder.CustomVideoDecoder
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.transformer.*
import com.google.android.exoplayer2.transformer.Transformer.PROGRESS_STATE_NO_TRANSFORMATION
import com.google.android.exoplayer2.transformer.Transformer.ProgressState
import java.util.*

class MultiViewVideoPlayerHDRTransformer : Activity(), SurfaceHolder.Callback,
    TextureView.SurfaceTextureListener, Transformer.Listener {

    private lateinit var binding: MultiViewPlayerHdrTransformerBinding
    private lateinit var transformer: Transformer

    // Decoders
    private var surfaceViewDecoder: CustomVideoDecoder? = null
    private var textureViewDecoder: CustomVideoDecoder? = null

    // Random File name
    private lateinit var encodedFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MultiViewPlayerHdrTransformerBinding.inflate(layoutInflater)

        /// File Name
        encodedFile = applicationContext.cacheDir.absolutePath + "/" + UUID.randomUUID().toString()
        binding.surfaceView.setAspectRatio(16, 9)
        binding.surfaceView.holder.addCallback(this)
        binding.textureView.setAspectRatio(16, 9)
        binding.textureView.surfaceTextureListener = this

        /// Set up transformer using Transformer.Builder
        val request = TransformationRequest.Builder()
            .setEnableRequestSdrToneMapping(true)
            .build()

        transformer = Transformer.Builder(applicationContext)
            .setTransformationRequest(request)
            .addListener(this)
            .build()

        setContentView(binding.root)
    }

    override fun onDestroy() {
        textureViewDecoder?.stop()
        surfaceViewDecoder?.stop()
        super.onDestroy()
    }

    /** SurfaceHolder.Callback Overrides */
    override fun surfaceCreated(holder: SurfaceHolder) {
        val assetFile = applicationContext.assets.openFd(Constants.HDR_VIDEO_PLAYER_ASSET)
        surfaceViewDecoder = CustomVideoDecoder.buildWithAssetFile(assetFile)
        surfaceViewDecoder?.setSurface(holder.surface)
        surfaceViewDecoder?.start(loop = true)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // No-Op.
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceViewDecoder?.stop()
    }

    /** TextureView.SurfaceTextureListener Overrides */
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        val firstVideoUri = Uri.parse("asset:///" + Constants.HDR_VIDEO_PLAYER_ASSET)
        val item = MediaItem.fromUri(firstVideoUri)
        transformer.startTransformation(item, encodedFile)

        // Set up to update UI of progress
        val progressHolder = ProgressHolder()
        val mainHandler = Handler(applicationContext.mainLooper)
        mainHandler.post(object : Runnable {
            override fun run() {
                val progressState: @ProgressState Int = transformer.getProgress(progressHolder)
                if (progressState != PROGRESS_STATE_NO_TRANSFORMATION) {
                    binding.textureViewTitle.text =
                        "Transformation Progress = ${progressHolder.progress}"
                    mainHandler.postDelayed( /* r = */ this,  /* delayMillis = */16)
                }
            }
        })
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // No-Op
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        textureViewDecoder?.stop()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // No-Op
    }

    /** Transformer.Listener Overrides */
    override fun onTransformationCompleted(
        inputMediaItem: MediaItem, transformationResult: TransformationResult
    ) {
        textureViewDecoder = CustomVideoDecoder.buildWithFilePath(encodedFile)
        textureViewDecoder?.setSurface(Surface(binding.textureView.surfaceTexture))
        textureViewDecoder?.start(loop = true)
    }

    override fun onTransformationError(
        inputMediaItem: MediaItem, exception: TransformationException
    ) {
        val message = "Transformation Failed... ${exception.cause}"
        Log.e(MultiViewVideoPlayerHDRTransformer::class.java.name, message)

        val mainHandler = Handler(applicationContext.mainLooper)
        mainHandler.post { binding.textureViewTitle.text = message }

        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onFallbackApplied(
        inputMediaItem: MediaItem,
        originalTransformationRequest: TransformationRequest,
        fallbackTransformationRequest: TransformationRequest
    ) {
        Log.w(
            MultiViewVideoPlayerHDRTransformer::class.java.name,
            "Fallback applied: $fallbackTransformationRequest"
        )
    }
}

