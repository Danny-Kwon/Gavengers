package com.example.gavengers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gavengers.databinding.ListItemBinding
import com.example.gavengers.network.Sensing

class RetroViewAdapter(private val deviceList: ArrayList<Sensing>, val context: Context): RecyclerView.Adapter<RetroViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    class MyViewHolder(binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        private val deviceId = binding.deviceId
        private val timeInfo = binding.timeInfo
        private val inOutInfo = binding.inOutInfo
        private val powerInfo = binding.powerInfo
        fun bind(device: Sensing) {
            deviceId.text = device.deviceId
            timeInfo.text = device.date
            inOutInfo.text = device.state
            powerInfo.text = device.power
        }
    }
}