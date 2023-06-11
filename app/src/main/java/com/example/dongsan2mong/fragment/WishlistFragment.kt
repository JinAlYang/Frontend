package com.example.dongsan2mong.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.adapter.WishlistViewPagerAdapter
import com.example.dongsan2mong.databinding.FragmentWishlistBinding
import com.example.dongsan2mong.event.MapEvent
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
        // init()
        binding = FragmentWishlistBinding.inflate(inflater, container, false)

        Log.e("dataEvent", "Wishlist : onCreateView()")
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

        Log.e("dataEvent", "Wishlist : init()")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("dataEvent", "Wishlist : onAttach()")
    }

    override fun onResume() {
        super.onResume()
        Log.e("dataEvent", "Wishlist : onResume()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("dataEvent", "Wishlist : onDetach()")
    }

    override fun onPause() {
        super.onPause()
        Log.e("dataEvent", "Wishlist : onPause()")
    }

    override fun onStart() {
        super.onStart()
        Log.e("dataEvent", "Wishlist : onStart()")
        try {
            Log.d("dataEvent", "wishlistFragment : onResume()")
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("dataEvent", "Wishlist : onStop()")
    }



//    override fun onResume() {
//        super.onResume()
//        try {
//            Log.d("dataEvent", "wishlistFragment : onResume()")
//            EventBus.getDefault().register(this)
//        } catch (e: Exception) {
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//        EventBus.getDefault().post(DataEvent("from Wishlist...ClusterActivity3"))
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun printId(event: MainEvent) {
//        Log.d("dataEvent", "WishlistFragment : ${event.helloMainBus}")
////        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printMap(event: MapEvent) {
        event.str = "from map? wish"

        Log.d("dataEvent", "WishlistFragment : ${event.str}")
//        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
    }



}