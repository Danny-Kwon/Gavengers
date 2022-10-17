package com.example.gavengers.network

data class User(var userPk: String?)
data class UserDevice(var userPk: String?, var deviceId: String?)
data class OkSign(var OkSign: String)
data class Device(var deviceId: String)
data class DeviceListModel(var items: ArrayList<DeviceInfo>)
data class DeviceInfo(var deviceId: String, var power: String, var date: String, var state: String)
data class Rx(var Rx: String)
data class Tx(var Tx: String)
data class Power(var Power: String)