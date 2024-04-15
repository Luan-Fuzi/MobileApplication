package com.example.forcaster

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.forcaster.database.Weather
import com.example.forcaster.network.Now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "WeatherViewModel"

class WeatherViewModel : ViewModel() {
    lateinit var repository: WeatherRepository
    lateinit var resources: Resources


    fun setRepository(context: Context) {
        this.resources = context.resources
        this.repository = WeatherRepository(context)

    }

    val weather7d = MutableLiveData<List<Weather>>()
    val weatherNow = MutableLiveData<Now>()


    fun getWeather7d(){
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.fetchWeather7d()
            if(result.code!="200"){
                Log.d("WeatherViewModel", "getWeather7d: ${result.code}")
//    从数据库取值
                val weather7d = repository.queryAll()
                this@WeatherViewModel.weather7d.postValue(weather7d)
                Log.d(TAG, "GETDATAFROMDATABASEWeather7d: $weather7d")
                return@launch
            }
//            将result的Daily取出到weather7d中
            val weather7d = result.daily
            Log.d("WeatherViewModel", "getWeather7d: $weather7d")
//            清空数据库，然后插入新的七天的数据
            repository.deleteAll()
            repository.insertBatch(weather7d)
            Log.d(TAG,"INSERTED")
            this@WeatherViewModel.weather7d.postValue(weather7d)

        }

    }

    fun getWeatherNow(){
        viewModelScope.launch(Dispatchers.IO) {

            val result = repository.fetchWeatherNow()
            if(result.code!="200"){
                Log.d(TAG, "getWeather: ${result.code}")

                return@launch
            }
            val weatherNow = result.now
            Log.d(TAG, "getWeatherNow: $weatherNow")
            this@WeatherViewModel.weatherNow.postValue(weatherNow)

        }

    }

    fun updateCity(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetchCity()
            if(result.code!="200"){
                Log.d(TAG, "updateCity: ${result.code}")
                return@launch
            }
            repository.setCityLocation(result.location[0].lat,result.location[0].lon)
            repository.setCityName(result.location[0].name)

        }





    }


//    一方面这里网络请求要重构，另一方面记得try-catch
//    val weather7d = liveData(Dispatchers.IO) {
//            val result = repository.fetchWeather7d()
//            Log.d("WeatherViewModel", "getWeather7dFromLiveData: $result")
//            emit(result)
//    }
//
//    val weather = liveData(Dispatchers.IO) {
//        val result = repository.fetchWeatherNow()
//        Log.d("WeatherViewModel", "getWeatherNowFromLiveData: $result")
//        emit(result)
//    }

//获取Preference中的城市信息
//    fun getCityName(): String {
//        return repository.getCityName()
//    }
//
//    fun setCityName(cityName: String) {
//        repository.setCityName(cityName)
//    }
//
//    fun getCityLocation(): String {
//        return repository.getCityLocation()
//    }
//
//    fun setCityLocation(cityLocation: String) {
//        repository.setCityLocation(cityLocation)
//    }

//    数据库操作
//    fun insertWeather(weather: Weather) {
//        viewModelScope.launch (Dispatchers.IO){
//            repository.insert(weather)
//        }
//    }


//    获取本地天气图标
//    fun getWeatherIcon(Num:String,context: Context):Drawable{
//        val resoureces = context.resources
//        val resId = resources.getIdentifier("w$Num", "drawable", "com.example.forcaster")
//        val drawable = ContextCompat.getDrawable(context, resId)
//        return drawable?:ContextCompat.getDrawable(context, R.drawable.w999)!!
//    }






}