package com.example.dongsan2mong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dongsan2mong.R
import com.example.dongsan2mong.adapter.HouseInfoDataAdapter
import com.example.dongsan2mong.adapter.MapGridViewAdapter
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.databinding.FragmentDibshomeBinding
import com.example.dongsan2mong.databinding.RowHouseinfoBinding

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
//        adapter.setDibshomeArr()

    }

    fun initRecyclerView() {
        binding.recyclerViewDibshome.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        adapter = HouseInfoDataAdapter(data, selected)
        adapter.itemClickListener = object: HouseInfoDataAdapter.OnItemClickListener {
            override fun OnItemClick(
                data: HouseInfoData,
                binding: RowHouseinfoBinding,
                position: Int
            ) {

            }
        }
        binding.recyclerViewDibshome.adapter = adapter

    }

    /*
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
            tempImg = 1
        )
        )
    }


     */
}