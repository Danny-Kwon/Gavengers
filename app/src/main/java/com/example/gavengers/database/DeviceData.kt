package com.example.gavengers.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "device")
@Parcelize
data class DeviceData(
    @PrimaryKey var deviceId: String,
    @ColumnInfo var userId: String?,
):Parcelable