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
        initData()
        return binding.root
    }

    fun initData() {
        data.add(FreesetData(freesetTitle = "슉슈슈슈슉슈슈슈슈슈슉"))
        data.add(FreesetData(freesetTitle = "오의1각성2지배배마"))
        data.add(FreesetData(freesetTitle = "배틀마스터는집짓는비버야"))
        data.add(FreesetData(freesetTitle = "쿠쿠루삥뽕쿠루쿠루쿠삥뽕"))
        data.add(FreesetData(freesetTitle = "전혀 성장하지 않았구나 쿠크셰이튼!"))
        data.add(FreesetData(freesetTitle = "짓이겨주마 아브렐슈드!"))
        data.add(FreesetData(freesetTitle = "끝이야 일리아칸!"))
        data.add(FreesetData(freesetTitle = "이게 바로 레기오로스의 수염이라고~"))
        data.add(FreesetData(freesetTitle = "용광로를 지펴라~ 망치를 꺼내라~"))
        data.add(FreesetData(freesetTitle = "낡고 허름해 보이지만"))
        data.add(FreesetData(freesetTitle = "우리의 혼이 쌓여있다고~"))
        selected.add(false)

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
