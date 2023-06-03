package com.example.dongsan2mong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.adapter.WishlistViewPagerAdapter
import com.example.dongsan2mong.databinding.FragmentWishlistBinding
import com.google.android.material.tabs.TabLayoutMediator

class WishlistFragment : Fragment() {
    lateinit var binding: FragmentWishlistBinding
    val tabtextarr = arrayListOf("최근 본 집", "찜한 집", "검색 프리셋 불러오기")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // init()
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        init()
        // return inflater.inflate(R.layout.fragment_wishlist, container, false)
        return binding.root
    }

    private fun init() {
        // Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()
        binding.wishlistViewPager.adapter = WishlistViewPagerAdapter(this.requireActivity())
        TabLayoutMediator(binding.tabLayoutWishlist, binding.wishlistViewPager) { tab, pos ->
            tab.text = tabtextarr[pos]
        }.attach()
    }
}