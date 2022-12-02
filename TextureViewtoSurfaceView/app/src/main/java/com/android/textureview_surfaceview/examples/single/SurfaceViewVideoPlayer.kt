package com.android.textureview_surfaceview.examples.single

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.SurfaceViewPlayerBinding

open class SurfaceViewVideoPlayer : Activity(), SurfaceHolder.Callback {
    protected lateinit var binding: SurfaceViewPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SurfaceViewPlayerBinding.inflate(layoutInflater)
        mediaPlayer = MediaPlayer()
        binding.surfaceViewVideoPlayerView.holder.addCallback(this)
        binding.surfaceViewVideoPlayerView.setAspectRatio(16, 9)
        setContentView(binding.root)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val assetFile = applicationContext.assets.openFd(Constants.NON_HDR_VIDEO_PLAYER_ASSET)
        mediaPlayer.setDataSource(assetFile)
        mediaPlayer.setSurface(holder.surface)
        mediaPlayer.prepareAsync()
        mediaPlayer.isLooping = true
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Release unneeded resources
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}