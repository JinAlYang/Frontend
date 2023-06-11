package com.example.dongsan2mong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.adapter.HouseInfoDataAdapter
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.databinding.FragmentRealestatecompareBinding

class RealestatecompareFragment: Fragment() {

    lateinit var binding: FragmentRealestatecompareBinding
    lateinit var adapter: HouseInfoDataAdapter

    val data: ArrayList<HouseInfoData> = ArrayList<HouseInfoData>()
    val selected: ArrayList<Boolean> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRealestatecompareBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
}