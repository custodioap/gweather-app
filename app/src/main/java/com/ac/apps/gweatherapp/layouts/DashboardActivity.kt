package com.ac.apps.gweatherapp.layouts


import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.ac.apps.gweatherapp.BuildConfig
import com.ac.apps.gweatherapp.R
import com.ac.apps.gweatherapp.adapter.ViewPagerAdapter
import com.ac.apps.gweatherapp.fragments.CurrentWeather
import com.ac.apps.gweatherapp.fragments.HistoryWeather
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val name = intent.getStringExtra("name")
        Toast.makeText(this, "Login Successful! Welcome $name", Toast.LENGTH_SHORT).show()

        init()
    }

    private fun init(){
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val fragments = listOf(
            CurrentWeather(),
            HistoryWeather()
        )

        val adapter = ViewPagerAdapter(this, fragments)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Current Weather"
                1 -> "History"
                else -> null
            }
        }.attach()

    }


}