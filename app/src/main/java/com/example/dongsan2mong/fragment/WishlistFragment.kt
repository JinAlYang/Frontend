package com.example.dongsan2mong.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.adapter.WishlistViewPagerAdapter
import com.example.dongsan2mong.databinding.FragmentWishlistBinding
import com.example.dongsan2mong.event.DataEvent
import com.google.android.material.tabs.TabLayoutMediator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WishlistFragment : Fragment() {
    lateinit var binding: FragmentWishlistBinding
    val tabtextarr = arrayListOf("최근 본 집", "찜한 집", "검색 프리셋 불러오기")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        binding.wishlistViewPager.adapter = WishlistViewPagerAdapter(this.requireActivity())
        TabLayoutMediator(binding.tabLayoutWishlist, binding.wishlistViewPager) { tab, pos ->
            tab.text = tabtextarr[pos]
        }.attach()
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().post(DataEvent(3))
    }

    override fun onStart() {
        super.onStart()
        try {
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printData(event: DataEvent) {
        if (event.int == 2) {
            Log.d("dataEvent", "mapFragment to wishlist")
        }
        else if (event.int == 5) {
            Log.d("dataEvent", "latesthome to wishlist")
            EventBus.getDefault().post(DataEvent(4))
        }
        else if (event.int == 7) {
            Log.d("dataEvent", "dibshome to wishlist")
            EventBus.getDefault().post(DataEvent(6))
        }
    }
}