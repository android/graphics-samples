package com.android.textureview_surfaceview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.textureview_surfaceview.databinding.ActivityMainBinding
import com.android.textureview_surfaceview.examples.multi.MultiViewVideoPlayer
import com.android.textureview_surfaceview.examples.multi.MultiViewVideoPlayerHDR
import com.android.textureview_surfaceview.examples.multi.MultiViewVideoPlayerHDRTransformer
import com.android.textureview_surfaceview.examples.single.SurfaceViewVideoPlayer
import com.android.textureview_surfaceview.examples.single.SurfaceViewVideoPlayerHDR
import com.android.textureview_surfaceview.examples.single.TextureViewVideoPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setBindings()
        setContentView(view)
    }

    private fun setBindings() {
        binding.textureViewVideoPlayer.setOnClickListener {
            val intent = Intent(this, TextureViewVideoPlayer::class.java)
            startActivity(intent)
        }

        binding.surfaceViewVideoPlayer.setOnClickListener {
            val intent = Intent(this, SurfaceViewVideoPlayer::class.java)
            startActivity(intent)
        }

        binding.surfaceViewHdrVideoPlayer.setOnClickListener {
            val intent = Intent(this, SurfaceViewVideoPlayerHDR::class.java)
            startActivity(intent)
        }

        binding.multiViewVideoPlayers.setOnClickListener {
            val intent = Intent(this, MultiViewVideoPlayer::class.java)
            startActivity(intent)
        }

        binding.multiViewVideoPlayersHdr.setOnClickListener {
            val intent = Intent(this, MultiViewVideoPlayerHDR::class.java)
            startActivity(intent)
        }

        binding.multiViewVideoPlayersHdrTransformer.setOnClickListener {
            val intent = Intent(this, MultiViewVideoPlayerHDRTransformer::class.java)
            startActivity(intent)
        }
    }
}