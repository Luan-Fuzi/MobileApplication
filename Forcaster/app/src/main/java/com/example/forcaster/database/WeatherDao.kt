package com.example.forcaster.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    //获取所有天气
    @Query("SELECT * FROM weatherlist")
    suspend fun getAll(): List<Weather>

    //插入一个天气，不常用
    @Insert
    suspend fun insert(weather: Weather)

    //插入一堆天气
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(weathers: List<Weather>)

    //删库
    @Query("DELETE FROM weatherlist")
    suspend fun deleteAll()



}