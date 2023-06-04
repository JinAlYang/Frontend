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
import com.example.dongsan2mong.data.PresetData
import com.example.dongsan2mong.databinding.FragmentCallpresetBinding
import com.example.dongsan2mong.databinding.RowPresetBinding

class CallPresetFragment : Fragment() {
    lateinit var binding: FragmentCallpresetBinding
    lateinit var adapter: PresetDataAdapter

    val data: ArrayList<PresetData> = ArrayList()
    val selected: ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallpresetBinding.inflate(inflater, container, false)
        initRecyclerView()
        adapter.onApplyClickListener = object : PresetDataAdapter.OnApplyClickListener {
            override fun onApplyClick(data: PresetData) {
                val mapFragment = MapFragment()
                val bundle = Bundle()
                bundle.putParcelable("freesetData", data)
                mapFragment.arguments = bundle

                val mainActivity = activity as? MainActivity
                mainActivity?.changeFragment(MapFragment())
            }
        }
        initData()
        return binding.root
    }

    fun initData() {
        data.add(PresetData(freesetTitle = "어대역보증금3000이상"))
        data.add(PresetData(freesetTitle = "화양동,자양동오피스텔만"))
        data.add(PresetData(freesetTitle = "구의동3층이상투룸월세70이하"))


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
            override fun OnItemClick(data: PresetData, binding: RowPresetBinding, position: Int) {
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
