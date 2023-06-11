package com.example.dongsan2mong.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.activity.MainActivity
import com.example.dongsan2mong.adapter.PresetDataAdapter
import com.example.dongsan2mong.data.PresetInfoData
import com.example.dongsan2mong.databinding.FragmentCallpresetBinding
import com.example.dongsan2mong.databinding.RowPresetBinding

class CallPresetFragment : Fragment(), PresetDataAdapter.OnApplyClickListener {
    lateinit var binding: FragmentCallpresetBinding
    lateinit var adapter: PresetDataAdapter

    val data: ArrayList<PresetInfoData> = ArrayList()
    val selected: ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallpresetBinding.inflate(inflater, container, false)
        initRecyclerView()
        initData()
        return binding.root
    }

    override fun onApplyClick(data: PresetInfoData) {
        // 이곳에서 data를 처리하거나 MainActivity의 MapFragment로 전달하는 로직을 추가하세요.
        val mapFragment = MapFragment()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.changeFragment(mapFragment)
    }

    fun initData() {
        data.add(PresetInfoData(freesetTitle = "중곡동, 보증금2억이하, 월세70이하"))
        for (i: Int in 0 until data.size) {
            selected.add(false)
        }

        adapter.notifyDataSetChanged()
    }

    fun initRecyclerView() {
        binding.recyclerViewFreeset.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = PresetDataAdapter(data, selected)
        adapter.itemClickListener = object : PresetDataAdapter.OnItemClickListener {
            override fun onItemClick(
                data: PresetInfoData,
                binding: RowPresetBinding,
                position: Int
            ) {
                adapter.updateItemAtPosition(position, data)
            }

        }
        binding.recyclerViewFreeset.adapter = adapter
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewFreeset)
    }
}
