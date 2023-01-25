package com.android.textureview_surfaceview.examples.multi

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.MultiViewPlayerHdrBinding
import com.android.textureview_surfaceview.decoder.CustomVideoDecoder

class MultiViewVideoPlayerHDR : AppCompatActivity() {

    private lateinit var binding: MultiViewPlayerHdrBinding
    private lateinit var assetFile: AssetFileDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MultiViewPlayerHdrBinding.inflate(layoutInflater)
        assetFile = applicationContext.assets.openFd(Constants.HDR_VIDEO_PLAYER_ASSET)

        /// Set Up Views
        setUpSurfaceViewCustomDecoder()
        setupTextureViewCustomDecoder()
        setupTextureViewMediaPlayer()

        setContentView(binding.root)
    }

    private fun setUpSurfaceViewCustomDecoder() {
        val decoder = CustomVideoDecoder.buildWithAssetFile(assetFile)
        binding.multiSurfaceViewVideoPlayerView.holder.addCallback(object : SurfaceHolder.Callback {
            /** SurfaceHolder.Callback Overrides  */
            override fun surfaceCreated(holder: SurfaceHolder) {
                decoder.setSurface(holder.surface)
                decoder.start(loop = true)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                decoder.stop()
            }
        })
        binding.multiSurfaceViewVideoPlayerView.setAspectRatio(16, 9)
    }

    private fun setupTextureViewCustomDecoder() {
        val decoder = CustomVideoDecoder.buildWithAssetFile(assetFile)
        binding.multiTextureViewVideoPlayerViewDecoder.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surfaceTexture: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    decoder.setSurface(Surface(surfaceTexture))
                    decoder.start(loop = true)
                }

                override fun onSurfaceTextureSizeChanged(
                    surfaceTexture: SurfaceTexture,
                    width: Int, height: Int
                ) {
                    // Handle size change depending on media needs
                }

                override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                    decoder.stop()
                    return true
                }

                override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                    // Invoked every time there's a new video frame
                }
            }
        binding.multiTextureViewVideoPlayerViewDecoder.setAspectRatio(16, 9)
    }

    private fun setupTextureViewMediaPlayer() {
        val player = MediaPlayer()
        binding.multiTextureViewVideoPlayerViewMediaPlayer.surfaceTextureListener =
            object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    player.setDataSource(assetFile)
                    player.setSurface(Surface(binding.multiTextureViewVideoPlayerViewMediaPlayer.surfaceTexture))
                    player.prepareAsync()
                    player.isLooping = true
                    player.setOnPreparedListener {
                        it.start()
                    }
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    // Release unneeded resources
                    player.stop()
                    player.release()
                    return true
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                }
            }
        binding.multiTextureViewVideoPlayerViewMediaPlayer.setAspectRatio(16, 9)
    }
}