package com.example.dongsan2mong.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dongsan2mong.fragment.CallfreesetFragment
import com.example.dongsan2mong.fragment.DibshomeFragment
import com.example.dongsan2mong.fragment.LatesthomeFragment

class WishlistViewPagerAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return LatesthomeFragment()
            1 -> return DibshomeFragment()
            2 -> return CallfreesetFragment()
            else -> return LatesthomeFragment()
        }
    }
}