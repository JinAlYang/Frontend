package com.example.dongsan2mong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.activity.MainActivity
import com.example.dongsan2mong.data.PresetInfoData
import com.example.dongsan2mong.databinding.RowPresetBinding
import com.example.dongsan2mong.fragment.MapFragment

class PresetDataAdapter(val items: ArrayList<PresetInfoData>, val selected: ArrayList<Boolean>) :
    RecyclerView.Adapter<PresetDataAdapter.ViewHolder>() {
    var onApplyClickListener: OnApplyClickListener? = null

    interface OnItemClickListener {
        fun OnItemClick(data: PresetInfoData, binding: RowPresetBinding, position: Int)
    }

    interface OnApplyClickListener {
        fun onApplyClick(data: PresetInfoData)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowPresetBinding) : RecyclerView.ViewHolder(binding.root) {
        private val mainActivity: MainActivity? = binding.root.context as MainActivity?

        init {
            binding.freeset.setOnClickListener {
                if (selected[adapterPosition] == false)
                    selected[adapterPosition] = true
                else if (selected[adapterPosition] == true)
                    selected[adapterPosition] = false
                itemClickListener?.OnItemClick(items[adapterPosition], binding, adapterPosition)
            }

            binding.freesetApply.setOnClickListener {
                val clickedPosition = adapterPosition
                val clickedData = items[clickedPosition]
                mainActivity?.changeFragment(MapFragment())
            }
        }
    }

    fun moveItem(oldPos: Int, newPos: Int) {
        val data = items.get(oldPos)
        items[oldPos] = items.get(newPos)
        items[newPos] = data
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowPresetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.freesetTitle.text = items[position].freesetTitle
        holder.binding.freesetApply.setOnClickListener {
            val currentItem = items[position]
            val currentBinding = holder.binding
            itemClickListener?.OnItemClick(currentItem, currentBinding, position)
            // 클릭 이벤트 처리 코드 작성
        }

        // freesetDelete ImageView 클릭 이벤트 처리
        holder.binding.freesetDelete.setOnClickListener {
            // 클릭한 아이템의 위치(position) 가져오기
            val clickedPosition = holder.adapterPosition
            // 해당 아이템 삭제
            items.removeAt(clickedPosition)
            notifyItemRemoved(clickedPosition)

            // 여기서 Http 통신으로 프리셋 삭제도 해줄 필요
        }
    }

    fun updateItemAtPosition(position: Int, data: PresetInfoData) {
        items[position] = data
        notifyItemChanged(position)
    }
}
