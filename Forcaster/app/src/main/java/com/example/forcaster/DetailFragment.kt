package com.example.forcaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.forcaster.database.Weather
import org.w3c.dom.Text

class DetailFragment : Fragment() {


    private lateinit var weather:Weather
   private lateinit var repository: WeatherRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        weather = arguments?.getSerializable("weather") as Weather
        repository = WeatherRepository(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setTitle("ForcasterDetail")
        toolbar.inflateMenu(R.menu.menu_detail)
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            val id: Int = item.itemId
            if (id == R.id.action_settings) {
                val intent = Intent(activity, Settings::class.java)
                startActivity(intent)
            } else if (id == R.id.action_share) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                val str = weather.fxDate + " "+repository.getCityName()+ " " + weather.textDay + "-" + weather.textNight + "," + weather.tempMax + "/" + weather.tempMin+"度"
                intent.putExtra(Intent.EXTRA_TEXT, str)
                startActivity(Intent.createChooser(intent, "Share with"))
            }
            true
        })




        if (weather != null) {

//            设置页面
            val detailDate: TextView = view.findViewById(R.id.detail_date)
            val detailTempMax: TextView = view.findViewById(R.id.detail_temp_max)
            val detailTempMin: TextView = view.findViewById(R.id.detail_temp_min)
            val detailWeather: TextView = view.findViewById(R.id.detail_weather)
            val detailHumidity: TextView = view.findViewById(R.id.detail_humidity)
            val detailPrecip: TextView = view.findViewById(R.id.detail_precip)

            val detailIconDay: ImageView = view.findViewById(R.id.detail_icon_day)
            val detailIconNight: ImageView = view.findViewById(R.id.detail_icon_night)

            detailDate.text = weather.fxDate
            detailTempMax.text = weather.tempMax
            detailTempMin.text = weather.tempMin
            detailHumidity.text = "湿度: "+weather.humidity+"%"
            detailPrecip.text = "降水量: "+weather.precip+"mm"

            if (weather.textDay == weather.textNight) {
                detailWeather.text = weather.textDay
            } else {
               detailWeather.text = weather.textDay + "转" + weather.textNight
            }

            val iconDay = weather.iconDay
            val iconNight = weather.iconNight
            val iconDayId = view.resources.getIdentifier(
                "w$iconDay",
                "drawable",
                view.context.packageName
            )
            val iconNightId = view.resources.getIdentifier(
                "w$iconNight",
                "drawable",
                view.context.packageName
            )
            detailIconDay.setImageResource(iconDayId)
            detailIconNight.setImageResource(iconNightId)





        }




        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }

        fun newInstance(weather: Weather): DetailFragment {
            val args = Bundle().apply {
                putSerializable("weather", weather)
            }
            return DetailFragment().apply {
                arguments = args
            }
        }
    }
}