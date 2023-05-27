package com.example.dongsan2mong

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.databinding.RowFreesetBinding

class FreesetDataAdapter (val items:ArrayList<FreesetData>, val selected:ArrayList<Boolean>)
    : RecyclerView.Adapter<FreesetDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: FreesetData, binding: RowFreesetBinding, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowFreesetBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.freeset.setOnClickListener {
                if (selected[adapterPosition] == false)
                    selected[adapterPosition] = true
                else if (selected[adapterPosition] == true)
                    selected[adapterPosition] = false
                itemClickListener?.OnItemClick(items[adapterPosition], binding, adapterPosition)
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
        val view = RowFreesetBinding.inflate(
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
        }
    }

    fun updateItemAtPosition(position: Int, data: FreesetData) {
        items[position] = data
        notifyItemChanged(position)
    }
}
