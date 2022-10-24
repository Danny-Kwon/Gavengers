package com.example.gavengers.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DeviceDao {
    //DeviceData의 모든 값 반환
    @Query("SELECT * FROM device")
    fun getAllId(): List<DeviceData>?

    //DeviceData의 테이블에 데이터 저장
    @Insert(onConflict = REPLACE)
    fun insertDevice(deviceData: DeviceData)

    //값 삭제
    @Delete
    fun deleteDevice(deviceData: DeviceData)

    @Query("DELETE FROM device")
    fun deleteAll()
}