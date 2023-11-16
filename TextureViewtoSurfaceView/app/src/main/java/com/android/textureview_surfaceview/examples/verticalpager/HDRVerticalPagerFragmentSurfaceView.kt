package com.android.textureview_surfaceview.examples.verticalpager

import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.textureview_surfaceview.Constants
import com.android.textureview_surfaceview.databinding.FragmentHdrVerticalPagerSurfaceviewBinding
import com.android.textureview_surfaceview.decoder.CustomVideoDecoder

class HDRVerticalPagerFragmentSurfaceView : Fragment(), HDRVerticalPagerFragmentInterface,
    SurfaceHolder.Callback {

    private lateinit var binding: FragmentHdrVerticalPagerSurfaceviewBinding
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
        binding = FragmentHdrVerticalPagerSurfaceviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hdrVerticalViewpagerSurfaceView.setAspectRatio(9, 16)
        binding.hdrVerticalViewpagerSurfaceView.holder.addCallback(this)
    }

    override fun onResume() {
        binding.hdrVerticalViewpagerSurfaceView.visibility = View.VISIBLE
        super.onResume()
    }

    override fun onPause() {
        decoder?.stop()
        binding.hdrVerticalViewpagerSurfaceView.visibility = View.INVISIBLE
        super.onPause()
    }

    companion object {
        var POSITION_ARG = "position_arg"

        @JvmStatic
        fun newInstance(position: Int) = HDRVerticalPagerFragmentSurfaceView().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        asset?.let {
            decoder = CustomVideoDecoder.buildWithAssetFile(it)
            decoder?.setSurface(holder.surface)
            decoder?.start(loop = true)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        decoder?.stop()
    }

    override fun setSurfaceViewVisibility(visibility: Boolean) {
        binding.hdrVerticalViewpagerSurfaceView.visibility =
            when (visibility) {
                false -> View.INVISIBLE
                true -> View.VISIBLE
            }
    }
}