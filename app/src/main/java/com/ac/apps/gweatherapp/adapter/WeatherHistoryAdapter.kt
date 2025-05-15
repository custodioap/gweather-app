package com.ac.apps.gweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.ac.apps.gweatherapp.R
import com.ac.apps.gweatherapp.databinding.WeatherHistoryItemBinding
import com.ac.apps.gweatherapp.dataclass.WeatherItem
import com.ac.apps.gweatherapp.dataclass.WeatherResponse


/**
 *
 *
 * Created by   :   Alex Custodio on 15/05/2025.
 * Company      :   SMS Global Technologies, Inc.
 * Email        :   alex.custodio@smsgt.com
 *
 *
 **/
class WeatherHistoryAdapter (private val items: List<WeatherItem>) : RecyclerView.Adapter<WeatherHistoryAdapter.WeatherViewHolder>() {


    inner class WeatherViewHolder(val binding: WeatherHistoryItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weather: WeatherItem){
            binding.weather = weather
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }
}