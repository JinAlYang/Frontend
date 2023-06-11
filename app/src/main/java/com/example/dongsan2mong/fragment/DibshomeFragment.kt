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
    var dibshomeArr: ArrayList<HouseInfoData>? = ArrayList()

    var data: ArrayList<HouseInfoData> = ArrayList<HouseInfoData>()
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
        data.add(HouseInfoData())
        data.add(
            HouseInfoData(
                type = "월세",
                price = "500/74",
                space = "33.06m^2",
                floor = "1층",
                area = "광진구 구의동",
                roomNum = "투룸",
                imgURL = ""
            )
        )

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
            EventBus.getDefault().post(DataEvent(7, data!!))
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().post(DataEvent(9, data!!))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printData(event: DataEvent) {
        if (event.int == 6) {
            Log.d("dataEvent", "wishlist to dibshome")
            data = event.dibsArr
//            for (i in data) {
//                println(i)
//            }

            adapter.changeArr(event.dibsArr)
            adapter.changeDibshomeArr(event.dibsArr)
            adapter.notifyDataSetChanged()
            for (i in adapter.dibshomeArr) {
                println("${i} ${i.area} , ${i.floor}, ${i.roomNum}, ${i.location}")
            }
            for (i in adapter.items) {
                println("${i} ${i.area} , ${i.floor}, ${i.roomNum}, ${i.location}")
            }

        }
        else if (event.int == 8) {
            Log.d("dataEvent", "latesthome to dibshome")
            data = event.dibsArr
//            for (i in data) {
//                println(i)
//            }
            adapter.notifyDataSetChanged()
        }
    }
}