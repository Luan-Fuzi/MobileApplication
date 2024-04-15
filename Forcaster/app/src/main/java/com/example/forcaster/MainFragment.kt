package com.example.forcaster

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.forcaster.database.Weather

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository
    private lateinit var weatherRecyclerView: RecyclerView

    private var weatherAdapter: WeatherAdapter? = null

    interface Callbacks {
        fun onWeatherSelected(weather: Weather)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        repository = WeatherRepository(requireContext())

        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        weatherViewModel.setRepository(requireContext())

//        repository.setCityLocation("49.9042", "116.4074")





    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)


        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setTitle("Forcaster")
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            val id: Int = item.itemId
            if (id == R.id.action_settings) {
                val intent = Intent(activity, Settings::class.java)
                startActivity(intent)
            } else if (id == R.id.action_map) {
                val uri = "geo:0,0?q=" + repository.getCityLocationLatLon()
                val gmmIntentUri = Uri.parse(uri)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            true
        })

        weatherRecyclerView = view.findViewById(R.id.weather_recycler_view) as RecyclerView
        weatherRecyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
//        weatherAdapter = WeatherAdapter(emptyList())
//        weatherRecyclerView.adapter = weatherAdapter
        weatherViewModel.weather7d.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreateView: $it")
            weatherAdapter = WeatherAdapter(it)
            weatherRecyclerView.adapter = weatherAdapter
        })


        val todayCity:TextView = view.findViewById(R.id.today_city)
        val todayTemp:TextView = view.findViewById(R.id.today_temp)
        val todayIcon:ImageView = view.findViewById(R.id.today_icon)
        val todayIconDescribe:TextView = view.findViewById(R.id.today_icon_describe)
        todayCity.text = repository.getCityName()

        weatherViewModel.weatherNow.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreateView: $it")
            todayCity.text = repository.getCityName()
            todayTemp.text = it.temp
            todayIconDescribe.text = it.text
            val iconId = resources.getIdentifier(
                "w${it.icon}",
                "drawable",
                requireContext().packageName
            )
            todayIcon.setImageResource(iconId)
        })











        return view

    }


    override fun onStart() {
        weatherViewModel.updateCity()
        weatherViewModel.getWeather7d()
        weatherViewModel.getWeatherNow()
        super.onStart()
        Log.d(TAG, "onStart: ")

        val send = repository.getSend()
        if(send){
            PollService.setServiceAlarm(requireContext(),true)
        }else{
            PollService.setServiceAlarm(requireContext(),false)
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }


    private inner class WeatherHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }
        private lateinit var weather: Weather
        private val item_max_weather: TextView = view.findViewById(R.id.item_max_weather)
        private val item_min_weather: TextView = view.findViewById(R.id.item_min_weather)
        private val item_icon_left: ImageView = view.findViewById(R.id.item_icon_left)
        private val item_icon_right: ImageView = view.findViewById(R.id.item_icon_right)
        private val item_date: TextView = view.findViewById(R.id.item_date)
        private val item_weather: TextView = view.findViewById(R.id.item_weather)
        fun bind(weather: Weather) {
            this.weather = weather
            item_max_weather.text = weather.tempMax
            item_min_weather.text = weather.tempMin
            item_date.text = weather.fxDate
            if (weather.textDay == weather.textNight) {
                item_weather.text = weather.textDay
            } else {
                item_weather.text = (weather.textDay + "转" + weather.textNight)
            }
//            通过天气代码获取天气图标
            val iconDay = weather.iconDay
            val iconNight = weather.iconNight
            val iconDayId = itemView.resources.getIdentifier(
                "w$iconDay",
                "drawable",
                itemView.context.packageName
            )
            val iconNightId = itemView.resources.getIdentifier(
                "w$iconNight",
                "drawable",
                itemView.context.packageName
            )
            item_icon_left.setImageResource(iconDayId)
            item_icon_right.setImageResource(iconNightId)


        }

        override fun onClick(v: View?) {
            callbacks?.onWeatherSelected(weather)
        }


    }

    private inner class WeatherAdapter(var weathers: List<Weather>) :
        RecyclerView.Adapter<WeatherHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
            return WeatherHolder(view)
        }

        override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
            val weather = weathers[position]
            holder.bind(weather)
        }

        override fun getItemCount(): Int {
            return weathers.size
        }

    }


}