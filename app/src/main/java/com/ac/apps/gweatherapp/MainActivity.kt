package com.ac.apps.gweatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ac.apps.gweatherapp.layouts.DashboardActivity
import com.ac.apps.gweatherapp.layouts.RegistrationActivity
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager
import com.ac.apps.gweatherapp.ui.theme.GWeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        val etEmail: EditText = findViewById(R.id.user_email)
        val password: EditText  = findViewById(R.id.password)
        val errorTv: TextView = findViewById(R.id.error_message);


        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val userEmail = etEmail.text.toString().trim()
            val userPassword = password.text.toString()

            val isValidUser = SharedPreferencesManager.isValidUser(userEmail, userPassword)
            val users = SharedPreferencesManager.getAllUsers();

            if (isValidUser){
//                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val name: String = users.find { it["email"] == userEmail }?.get("username") ?: "no user found"
                val intent = Intent(this, DashboardActivity::class.java).apply {
                    putExtra("name", name)
                }

                startActivity(intent)
                errorTv.visibility = View.GONE;
            } else {
                errorTv.visibility = View.VISIBLE;
                errorTv.text = "Invalid username/password!";
            }
        }

        val registerButton: TextView = findViewById(R.id.register)
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}