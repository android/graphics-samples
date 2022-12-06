package com.android.textureview_surfaceview.examples.multi

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.MultiViewPlayerBinding

class MultiViewVideoPlayer : Activity(), SurfaceHolder.Callback,
    TextureView.SurfaceTextureListener {

    private lateinit var binding: MultiViewPlayerBinding
    private lateinit var surfaceMediaPlayer: MediaPlayer
    private lateinit var textureMediaPlayer: MediaPlayer
    private lateinit var assetFile: AssetFileDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MultiViewPlayerBinding.inflate(layoutInflater)
        assetFile = applicationContext.assets.openFd(Constants.NON_HDR_VIDEO_PLAYER_ASSET)

        /// Set Up Views
        setUpSurfaceView()
        setupTextureView()

        setContentView(binding.root)
    }

    private fun setupTextureView() {
        textureMediaPlayer = MediaPlayer()
        binding.multiTextureViewVideoPlayerView.surfaceTextureListener = this
        binding.multiTextureViewVideoPlayerView.setAspectRatio(16, 9)
    }

    private fun setUpSurfaceView() {
        surfaceMediaPlayer = MediaPlayer()
        binding.multiSurfaceViewVideoPlayerView.holder.addCallback(this)
        binding.multiSurfaceViewVideoPlayerView.setAspectRatio(16, 9)
    }

    /** SurfaceHolder.Callback Overrides  */
    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceMediaPlayer.setDataSource(assetFile)
        surfaceMediaPlayer.setSurface(holder.surface)
        surfaceMediaPlayer.prepareAsync()
        surfaceMediaPlayer.isLooping = true
        surfaceMediaPlayer.setOnPreparedListener {
            it.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Release unneeded resources
        surfaceMediaPlayer.stop()
        surfaceMediaPlayer.release()
    }

    /** TextureView.SurfaceTextureListener Overrides */

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        textureMediaPlayer.setDataSource(assetFile)
        textureMediaPlayer.setSurface(Surface(surfaceTexture))
        textureMediaPlayer.prepareAsync()
        textureMediaPlayer.isLooping = true
        textureMediaPlayer.setOnPreparedListener {
            it.start()
        }
    }

    override fun onSurfaceTextureSizeChanged(
        surfaceTexture: SurfaceTexture,
        width: Int, height: Int
    ) {
        // Handle size change depending on media needs
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        // Release unneeded resources
        textureMediaPlayer.stop()
        textureMediaPlayer.release()
        return true
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
        // Invoked every time there's a new video frame
    }
}