package com.example.dongsan2mong.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dongsan2mong.adapter.HouseInfoDataAdapter
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.databinding.FragmentDibshomeBinding
import com.example.dongsan2mong.databinding.RowHouseinfoBinding
import com.example.dongsan2mong.event.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DibshomeFragment: Fragment() {

    lateinit var binding: FragmentDibshomeBinding
    lateinit var adapter: HouseInfoDataAdapter

    val data: ArrayList<HouseInfoData> = ArrayList<HouseInfoData>()
    val selected: ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDibshomeBinding.inflate(inflater, container, false)

        initData()
        initRecyclerView()
        return binding.root
    }

    fun initData() {

    }

    fun initRecyclerView() {
        binding.recyclerViewDibshome.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = HouseInfoDataAdapter(data, selected)
        adapter.itemClickListener = object : HouseInfoDataAdapter.OnItemClickListener {
            override fun OnItemClick(
                data: HouseInfoData,
                binding: RowHouseinfoBinding,
                position: Int
            ) {

            }
        }
        binding.recyclerViewDibshome.adapter = adapter

    }
    override fun onResume() {
        super.onResume()
        try {
            EventBus.getDefault().register(this)
            EventBus.getDefault().post(DataEvent(7))
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().post(DataEvent(9))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printData(event: DataEvent) {
        if (event.int == 6) {
            Log.d("dataEvent", "wishlist to dibshome")
        }
        else if (event.int == 8) {
            Log.d("dataEvent", "latesthome to dibshome")
        }
    }
}