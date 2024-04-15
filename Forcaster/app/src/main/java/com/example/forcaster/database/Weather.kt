package com.example.forcaster.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "weatherlist")
data class Weather(
    @ColumnInfo(name = "fxDate")
    @PrimaryKey
    val fxDate: String,

    @ColumnInfo(name = "tempMax")
    val tempMax: String,

    @ColumnInfo(name = "tempMin")
    val tempMin: String,

    @ColumnInfo(name = "textDay")
    val textDay: String,

    @ColumnInfo(name = "textNight")
    val textNight: String,

    @ColumnInfo(name = "iconDay")
    val iconDay: String,

    @ColumnInfo(name = "iconNight")
    val iconNight: String,

    @ColumnInfo(name = "humidity")
    val humidity: String,

    @ColumnInfo(name="precip")
    val precip:String

    ):Serializable