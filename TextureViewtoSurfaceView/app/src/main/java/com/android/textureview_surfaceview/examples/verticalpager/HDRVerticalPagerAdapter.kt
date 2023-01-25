package com.android.textureview_surfaceview.examples.verticalpager

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HDRVerticalPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int) = HDRVerticalPagerFragment.newInstance(position)
}