package com.android.textureview_surfaceview.examples.verticalpager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class HDRVerticalPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3
    override fun createFragment(position: Int) = HDRVerticalPagerFragmentSurfaceView.newInstance(position)
}

