package com.example.gavengers.network

data class DeviceListModel(var items: ArrayList<DeviceInfo>)
data class DeviceInfo(var deviceId: String, var power: String, var date: String, var state: String)