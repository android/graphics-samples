package com.android.textureview_surfaceview.examples.verticalpager

import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.FragmentHdrVerticalPagerTextureviewBinding
import com.android.textureview_surfaceview.decoder.CustomVideoDecoder

class HDRVerticalPagerFragmentTextureView :
    Fragment(),
    HDRVerticalPagerFragmentInterface,
    TextureView.SurfaceTextureListener {

    private lateinit var binding: FragmentHdrVerticalPagerTextureviewBinding
    private var decoder: CustomVideoDecoder? = null
    private var asset: AssetFileDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pos = arguments?.getInt(POSITION_ARG)
        pos?.let {
            when (it) {
                0 -> asset = context?.assets?.openFd(Constants.HDR_VERTICAL_VIDEO_1)
                1 -> asset = context?.assets?.openFd(Constants.HDR_VERTICAL_VIDEO_2)
                2 -> asset = context?.assets?.openFd(Constants.HDR_VERTICAL_VIDEO_3)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHdrVerticalPagerTextureviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hdrVerticalViewpagerTextureView.surfaceTextureListener = this
        binding.hdrVerticalViewpagerTextureView.setAspectRatio(9, 16)
    }

    companion object {
        var POSITION_ARG = "position_arg"

        @JvmStatic
        fun newInstance(position: Int) = HDRVerticalPagerFragmentTextureView().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        asset?.let {
            decoder = CustomVideoDecoder.buildWithAssetFile(it)
            decoder?.setSurface(Surface(surfaceTexture))
            decoder?.start(loop = true)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // TODO("Not yet implemented")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        decoder?.stop()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }
}