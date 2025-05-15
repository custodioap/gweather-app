package com.ac.apps.gweatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ac.apps.gweatherapp.R
import com.ac.apps.gweatherapp.adapter.WeatherHistoryAdapter
import com.ac.apps.gweatherapp.dataclass.WeatherItem
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager


class HistoryWeather : Fragment() {

    private lateinit var adapter: WeatherHistoryAdapter
    private lateinit var weatherList: List<WeatherItem>
    private lateinit var rv: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_weather, container, false)


        val weatherSharedPref = SharedPreferencesManager.getAllWeather()
        weatherList = weatherSharedPref.map { weather ->
            WeatherItem(
                name = weather["city"] ?: "unknown",
                temp = weather["temperature"] ?: "unknown"
            )
        }


        adapter = WeatherHistoryAdapter(weatherList)
        rv = view.findViewById(R.id.weatherRecyclerView)
        rv.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            rv.context,
            (rv.layoutManager as LinearLayoutManager).orientation
        )
        rv.addItemDecoration(dividerItemDecoration)
        rv.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}