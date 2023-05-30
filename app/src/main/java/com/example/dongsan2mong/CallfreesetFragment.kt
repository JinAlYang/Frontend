package com.example.dongsan2mong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.databinding.FragmentCallfreesetBinding
import com.example.dongsan2mong.databinding.RowFreesetBinding

class CallfreesetFragment: Fragment() {
    lateinit var binding: FragmentCallfreesetBinding
    lateinit var adapter: FreesetDataAdapter

    val data:ArrayList<FreesetData> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCallfreesetBinding.inflate(inflater, container, false)
        initRecyclerView()
        adapter.onApplyClickListener = object : FreesetDataAdapter.OnApplyClickListener {
            override fun onApplyClick(data: FreesetData) {
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
        data.add(FreesetData(freesetTitle = "어대역보증금3000이상"))
        data.add(FreesetData(freesetTitle = "화양동,자양동오피스텔만"))
        data.add(FreesetData(freesetTitle = "구의동3층이상투룸월세70이하"))


        for (i: Int in 0 until data.size) {
            selected.add(false)
        }

        adapter.notifyDataSetChanged()
    }

    fun initRecyclerView() {
        binding.recyclerViewFreeset.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        adapter = FreesetDataAdapter(data, selected)
        adapter.itemClickListener = object :FreesetDataAdapter.OnItemClickListener {
            override fun OnItemClick(data: FreesetData, binding: RowFreesetBinding, position: Int) {
                adapter.updateItemAtPosition(position, data)
            }

        }
        binding.recyclerViewFreeset.adapter = adapter
        val simpleCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
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
