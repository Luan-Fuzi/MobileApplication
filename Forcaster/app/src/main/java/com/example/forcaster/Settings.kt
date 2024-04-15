package com.example.forcaster

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Settings"

class Settings :AppCompatActivity() {

    lateinit var repository: WeatherRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)


        repository = WeatherRepository(this)


//   地址输入
        val cityText: EditText = findViewById(R.id.city_text)
        cityText.setText(repository.getCityName())


//单位选择
        val unitSpinner: Spinner = findViewById(R.id.spinner_unit)
        val oldUnit = if (repository.getUnit() == "m") 0 else 1
        unitSpinner.setSelection(oldUnit)

//通知选择
        val sendSpinner: Spinner = findViewById(R.id.spinner_send)
        val oldSend = if (repository.getSend()) 1 else 0
        sendSpinner.setSelection(oldSend)


    }

    override fun onPause() {
        super.onPause()
        val cityText: EditText = findViewById(R.id.city_text)
        val city = cityText.text.toString()
        repository.setCityName(city)

        val unitSpinner: Spinner = findViewById(R.id.spinner_unit)
        val unit = unitSpinner.selectedItem.toString()
        val unit2 = if (unit == "摄氏度") "m" else "i"
        repository.setUnit(unit2)

        val sendSpinner: Spinner = findViewById(R.id.spinner_send)
        val send = sendSpinner.selectedItem.toString()
        val send2 = send != "关闭通知"
        repository.setSend(send2)


    }
}