package com.example.gavengers.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gavengers.database.DeviceData
import com.example.gavengers.database.DeviceDatabase

class DeviceViewModel(app: Application) : AndroidViewModel(app) {
    var allDevices : MutableLiveData<List<DeviceData>> = MutableLiveData()

    fun getAllIdsObservers(): MutableLiveData<List<DeviceData>> {
        return allDevices
    }

    fun getAllIds(){
        val deviceDao = DeviceDatabase.getDatabase((getApplication())).deviceDao()
        val list = deviceDao.getAllId()

        allDevices.postValue(list!!)
    }

    fun insertDevice(entity: DeviceData){
        val deviceDao = DeviceDatabase.getDatabase((getApplication())).deviceDao()
        deviceDao.insertDevice(entity)
        getAllIds()
    }

    fun deleteDevice(entity: DeviceData){
        val deviceDao = DeviceDatabase.getDatabase((getApplication())).deviceDao()
        deviceDao.deleteDevice(entity)
        getAllIds()
    }

    fun deleteAll(){
        val deviceDao = DeviceDatabase.getDatabase((getApplication())).deviceDao()
        deviceDao.deleteAll()
    }
}