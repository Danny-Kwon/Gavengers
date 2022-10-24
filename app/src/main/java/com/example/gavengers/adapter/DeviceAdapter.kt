package com.example.gavengers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gavengers.database.DeviceData
import com.example.gavengers.databinding.ListDeviceBinding
import java.util.*

class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.ItemViewHolder>(){
    var items = ArrayList<DeviceData>()

    fun setListData(data: ArrayList<DeviceData>){
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ItemViewHolder(binding: ListDeviceBinding): RecyclerView.ViewHolder(binding.root){
        private val deviceID = binding.devID
        fun bind(data: DeviceData){
            deviceID.text = data.deviceId
        }
    }
}