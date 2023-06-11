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
import com.example.dongsan2mong.databinding.FragmentLatesthomeBinding
import com.example.dongsan2mong.databinding.RowHouseinfoBinding
import com.example.dongsan2mong.event.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LatesthomeFragment: Fragment() {
    lateinit var binding: FragmentLatesthomeBinding
    lateinit var adapter: HouseInfoDataAdapter

    val data: ArrayList<HouseInfoData> = ArrayList()
    val selected: ArrayList<Boolean> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLatesthomeBinding.inflate(inflater, container, false)
        Log.e("dataEvent", "Latesthome : onCreateView()")
        initRecyclerView()
        initData()
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
            tempImg = 1
        )
        )
    }

    fun initRecyclerView() {
        binding.recyclerViewLatestHome.layoutManager = LinearLayoutManager(requireContext(),
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
        binding.recyclerViewLatestHome.adapter = adapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            Log.e("dataEvent", "Latesthome : onAttach()")
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }
//    override fun onStart() {
//        super.onStart()
//        try {
//            EventBus.getDefault().register(this)
//        } catch (e: Exception) {
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        Log.e("dataEvent", "Latesthome : onDetach()")
        EventBus.getDefault().unregister(this)
    }
    override fun onStop() {
        super.onStop()
        Log.e("dataEvent", "Latesthome : onStop()")
        EventBus.getDefault().post(LatesthomeEvent("from latest..."))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printDibs(event: DibsEvent) {
        event.str = "from dibs? late"

        Log.d("dataEvent", "LatesthomeFragment : ${event.str}")
//        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printMap(event: MapEvent) {
        event.str = "from map? late"

        Log.d("dataEvent", "LatesthomeFragment : ${event.str}")
//        Toast.makeText(this@DataEvent, "${event.helloEventBus}", Toast.LENGTH_SHORT).show()
    }
}