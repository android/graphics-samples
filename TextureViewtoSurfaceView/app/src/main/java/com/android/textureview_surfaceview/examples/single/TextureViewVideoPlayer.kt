package com.android.textureview_surfaceview.examples.single

import android.app.Activity
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.TextureViewPlayerBinding

class TextureViewVideoPlayer : Activity(), SurfaceTextureListener {

    private lateinit var binding: TextureViewPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TextureViewPlayerBinding.inflate(layoutInflater)
        val view = binding.root

        mediaPlayer = MediaPlayer()
        binding.textureViewVideoPlayerView.surfaceTextureListener = this
        binding.textureViewVideoPlayerView.setAspectRatio(16, 9)
        setContentView(view)
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        val assetFile = applicationContext.assets.openFd(Constants.NON_HDR_VIDEO_PLAYER_ASSET)
        mediaPlayer.setDataSource(assetFile)
        mediaPlayer.setSurface(Surface(surfaceTexture))
        mediaPlayer.prepareAsync()
        mediaPlayer.isLooping = true
        mediaPlayer.setOnPreparedListener {
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
        mediaPlayer.stop()
        mediaPlayer.release()
        return true
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
        // Invoked every time there's a new video frame
        Log.d("test", "Just seeing if this works as intended");
    }
}