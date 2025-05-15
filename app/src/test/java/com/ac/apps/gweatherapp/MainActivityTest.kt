package com.ac.apps.gweatherapp

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ac.apps.gweatherapp.layouts.DashboardActivity
import com.ac.apps.gweatherapp.layouts.RegistrationActivity
import com.ac.apps.gweatherapp.sharedpref.SharedPreferencesManager
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        // Set up mock data for SharedPreferencesManager
        Mockito.mockStatic(SharedPreferencesManager::class.java)
        `when`(SharedPreferencesManager.isValidUser("test@example.com", "password123")).thenReturn(true)
        `when`(SharedPreferencesManager.getAllUsers()).thenReturn(
            listOf(
                mapOf("email" to "test@example.com", "username" to "Test User")
            )
        )

        // Launch activity
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
        Mockito.clearAllCaches()
    }

    @Test
    fun validLogin_showsDashboard() {
        scenario.onActivity { activity ->
            val emailField: EditText = activity.findViewById(R.id.user_email)
            val passwordField: EditText = activity.findViewById(R.id.password)
            val loginButton: Button = activity.findViewById(R.id.loginButton)

            // Input valid credentials
            emailField.setText("test@example.com")
            passwordField.setText("password123")
            loginButton.performClick()

            // Verify navigation to DashboardActivity
            val expectedIntent = Intent(activity, DashboardActivity::class.java)
            expectedIntent.putExtra("name", "Test User")
            verify(activity).startActivity(Mockito.argThat { intent ->
                intent.component == expectedIntent.component &&
                        intent.getStringExtra("name") == expectedIntent.getStringExtra("name")
            })
        }
    }

    @Test
    fun invalidLogin_showsErrorMessage() {
        scenario.onActivity { activity ->
            val emailField: EditText = activity.findViewById(R.id.user_email)
            val passwordField: EditText = activity.findViewById(R.id.password)
            val loginButton: Button = activity.findViewById(R.id.loginButton)
            val errorTv: TextView = activity.findViewById(R.id.error_message)

            // Input invalid credentials
            emailField.setText("wrong@example.com")
            passwordField.setText("wrongpassword")
            loginButton.performClick()

            // Verify error message visibility
            assert(errorTv.visibility == View.VISIBLE)
            assert(errorTv.text == "Invalid username/password!")
        }
    }

    @Test
    fun registerButton_navigatesToRegistrationActivity() {
        scenario.onActivity { activity ->
            val registerButton: TextView = activity.findViewById(R.id.register)
            registerButton.performClick()

            // Verify navigation to RegistrationActivity
            val expectedIntent = Intent(activity, RegistrationActivity::class.java)
            verify(activity).startActivity(Mockito.argThat { intent ->
                intent.component == expectedIntent.component
            })
        }
    }
}
