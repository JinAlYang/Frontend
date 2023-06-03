package com.example.dongsan2mong.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dongsan2mong.R
import com.example.dongsan2mong.databinding.SelectedDistrictBinding
class SeoulAdapter(val items: Array<String>, var count: Array<Boolean>)
    : RecyclerView.Adapter<SeoulAdapter.ViewHolder>() {
    var selectedPosition = -1
    interface OnItemClickListener {
        fun OnItemClick (position: Int)
    }

    var itemClickListener: OnItemClickListener?= null

    inner class ViewHolder(val binding: SelectedDistrictBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.selectedDistrictName.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }

        }
    }

    // RecyclerView 어댑터 내부에 선택된 아이템 인덱스를 저장하는 Set 변수를 추가합니다.
    private val selectedItems = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectedDistrictBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.selectedDistrictName.text = items[position]
        val item = items[position]
//        if (position == selectedPosition) {
//            holder.binding.selectedDistrictName.setTextColor(R.color.main_blue)
//            holder.binding.selectedDistrictName.
//            setBackgroundResource(R.drawable.background_map_option_selected)
//        } else {
//            holder.binding.selectedDistrictName.setTextColor(R.color.black)
//            holder.binding.selectedDistrictName.
//            setBackgroundResource(R.drawable.background_map_option_expand)
//        }
        if (count[position] || position == selectedPosition) {
            holder.binding.selectedDistrictName.setTextColor(R.color.main_blue)
            holder.binding.selectedDistrictName.
            setBackgroundResource(R.drawable.background_map_option_selected)
        }
        else {
            holder.binding.selectedDistrictName.setTextColor(R.color.black)
            holder.binding.selectedDistrictName.
            setBackgroundResource(R.drawable.background_map_option_expand)
        }
    }
}