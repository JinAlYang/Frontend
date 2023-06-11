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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }
//    override fun onStart() {
//        super.onStart()
//
//    }

    override fun onDetach() {
        super.onDetach()
        super.onStop()
        EventBus.getDefault().unregister(this)


    }
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(DibsEvent("from dibs..."))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printLatesthome(event: LatesthomeEvent) {
        event.str = "from late? dibs"

        Log.d("dataEvent", "DibsHomeFragment : ${event.str}")
//        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printMap(event: MapEvent) {
        event.str = "from map? dibs"

        Log.d("dataEvent", "DibsHomeFragment : ${event.str}")
//        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
    }
}