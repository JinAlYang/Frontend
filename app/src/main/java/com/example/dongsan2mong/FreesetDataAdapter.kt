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
    }

    fun updateItemAtPosition(position: Int, data: FreesetData) {
        items[position] = data
        notifyItemChanged(position)
    }
}
