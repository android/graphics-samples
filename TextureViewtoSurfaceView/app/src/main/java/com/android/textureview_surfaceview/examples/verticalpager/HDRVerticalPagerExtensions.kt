package com.android.textureview_surfaceview.examples.verticalpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return fragmentManager.findFragmentByTag("f$currentItem")
}

fun ViewPager2.findFragmentAtPosition(
    fragmentManager: FragmentManager,
    position: Int
): Fragment? {
    return fragmentManager.findFragmentByTag("f$position")
}