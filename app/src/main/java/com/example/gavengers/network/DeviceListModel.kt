package com.example.gavengers.network

data class User(var userPk: String)
data class UserDevice(var userPk: String, var deviceId: String)
data class Device(var deviceId: String)
data class DeviceListModel(var obj: ArrayList<Sensing>)
data class Sensing(var deviceId: String, var power: String, var date: String, var state: String)
data class Rx(var rx: String)
data class Tx(var tx: String)
data class Power(var power: String)