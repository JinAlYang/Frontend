package com.example.dongsan2mong

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.databinding.RowHouseinfoBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class HouseInfoDataAdapter (val items:ArrayList<HouseInfoData>, val selected:ArrayList<Boolean>)
    : RecyclerView.Adapter<HouseInfoDataAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(data: HouseInfoData, binding: RowHouseinfoBinding, position: Int)
    }

    var itemClickListener: HouseInfoDataAdapter.OnItemClickListener? = null

    inner class ViewHolder(val binding: RowHouseinfoBinding)
        : RecyclerView.ViewHolder(binding.root) {
        private val mainActivity: MainActivity? = binding.root.context as? MainActivity
        init {
            binding.houseInfo.setOnClickListener {
                if (selected[adapterPosition] == false)
                    selected[adapterPosition] = true
                else if (selected[adapterPosition] == true)
                    selected[adapterPosition] = false
                itemClickListener?.OnItemClick(items[adapterPosition], binding, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowHouseinfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.typeAndPrice.text = items[position].type + " " +
                items[position].price
        holder.binding.spaceAndFloor.text = items[position].space + " " +
                items[position].floor
        holder.binding.area.text = items[position].area
        holder.binding.roomNum.text = items[position].roomNum
        if (items[position].tempImg == 0) {
            holder.binding.houseImg.setImageResource(R.drawable.img_house_1)
        } else {
            holder.binding.houseImg.setImageResource(R.drawable.img_house_2)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}