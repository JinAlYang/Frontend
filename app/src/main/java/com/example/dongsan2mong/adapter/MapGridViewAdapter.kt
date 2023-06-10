package com.example.dongsan2mong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.databinding.GridviewListItemBinding


class MapGridViewAdapter (val items: ArrayList<Int>)
    : RecyclerView.Adapter<MapGridViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick (position: Int, item: Int)
    }

    var itemClickListener: OnItemClickListener?= null

    inner class ViewHolder(val binding: GridviewListItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.gridviewText.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition, items[adapterPosition])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = GridviewListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MapGridViewAdapter.ViewHolder, position: Int) {
        holder.binding.gridviewText.text = items[position].toString()
    }
}