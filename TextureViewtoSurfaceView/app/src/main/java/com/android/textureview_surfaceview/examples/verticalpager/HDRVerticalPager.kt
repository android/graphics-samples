package com.android.textureview_surfaceview.examples.verticalpager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.textureview_surfaceview.databinding.HdrVerticalPagerBinding

class HDRVerticalPager : AppCompatActivity() {

    private lateinit var binding: HdrVerticalPagerBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: HDRVerticalPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HdrVerticalPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// Set up view pager & adapter
        setUpViewPager2()
    }

    private fun setUpViewPager2() {
        viewPager = binding.hdrVerticalViewpager
        viewPager.offscreenPageLimit = 1
        adapter = HDRVerticalPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state != ViewPager2.SCROLL_STATE_DRAGGING) {
                    Log.w("Testing", "Making the happen on " + viewPager.currentItem)
                    return
                }

                val position = viewPager.currentItem

                viewPager.findFragmentAtPosition(
                    supportFragmentManager,
                    position - 1
                )?.let {
                    (it as HDRVerticalPagerFragmentInterface).setSurfaceViewVisibility(true)
                }

                viewPager.findFragmentAtPosition(
                    supportFragmentManager,
                    position + 1
                )?.let {
                    (it as HDRVerticalPagerFragmentInterface).setSurfaceViewVisibility(true)
                }
            }
        })
    }
}