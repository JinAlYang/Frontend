package com.example.dongsan2mong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.R
import com.example.dongsan2mong.activity.MainActivity
import com.example.dongsan2mong.databinding.RowHouseinfoBinding

class HouseInfoDataAdapter (val items:ArrayList<HouseInfoData>, val selected:ArrayList<Boolean>)
    : RecyclerView.Adapter<HouseInfoDataAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(data: HouseInfoData, binding: RowHouseinfoBinding, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null
    // 찜목록에 들어갈 매물들
    var dibshomeArr: ArrayList<HouseInfoData> = ArrayList<HouseInfoData>()

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

    // 찜목록에 들어갈 매물 정보 얻기 (리스트로) - searchActivity에서 사용한 adapter의 정보를
    // dibshome fragement에 어떻게 가져가지...

    fun changeDibshomeArr(arr: ArrayList<HouseInfoData>) {
        dibshomeArr.clear()
        dibshomeArr.addAll(arr)
        notifyDataSetChanged()
    }
    fun findDibshomeArr(): ArrayList<HouseInfoData> {
        return dibshomeArr
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowHouseinfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // dibshomeArr에 해당 매물이 있다면 찜 아이콘 채워지고, 아니면 stroke만
        holder.binding.favorite.isSelected = dibshomeArr.contains(items[position])

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

        holder.binding.apply {
            favorite.setOnClickListener {
                if (favorite.isSelected) {
                    favorite.isSelected = false
                    dibshomeArr.remove(items[position])
                    notifyDataSetChanged()

                }
                else {
                    favorite.isSelected = true
                    dibshomeArr.add(items[position])
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}