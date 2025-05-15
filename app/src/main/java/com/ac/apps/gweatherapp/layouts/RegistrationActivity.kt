package com.ac.apps.gweatherapp.layouts

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ac.apps.gweatherapp.R
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        init()

    }

    private fun init(){
        val fullName: EditText = findViewById(R.id.etName)
        val email: EditText = findViewById(R.id.etEmail)
        val pass: EditText = findViewById(R.id.etPassword)
        val confpass: EditText = findViewById(R.id.etConfirmPassword)


        val registerBtn: Button = findViewById(R.id.btnRegister)
        registerBtn.setOnClickListener{
            val name = fullName.text.toString().trim()
            val email = email.text.toString().trim()
            val password = pass.text.toString()
            val confirmPassword = confpass.text.toString()

            // Validate input
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // save info
            SharedPreferencesManager.saveUser(name, confirmPassword, email)

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

            //redirect back to login
            finish()

        }

        val signIn: TextView = findViewById(R.id.signIn)
        signIn.setOnClickListener {
            finish()
        }
    }
}