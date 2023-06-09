package com.example.dongsan2mong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.databinding.SelectedAreaBinding

class MapSelectedAreaAdapter(val items: ArrayList<String>)
    : RecyclerView.Adapter<MapSelectedAreaAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick (position: Int, item: String)
    }

    var itemClickListener: OnItemClickListener?= null

        inner class ViewHolder(val binding: SelectedAreaBinding): RecyclerView.ViewHolder(binding.root) {
            init {
                binding.selectedAreaName.setOnClickListener {
                    itemClickListener?.OnItemClick(adapterPosition, items[adapterPosition])
                }

            }
        }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectedAreaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.selectedAreaName.text = items[position]
    }
}