package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragments.Configuration
import com.example.myapplication.fragments.ContactUs
import com.google.android.material.bottomnavigation.BottomNavigationView

class DailyReports : AppCompatActivity() {
    private lateinit var goBack: ImageView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_reports)
        initComponents()
        initListeners()
    }

    private fun initComponents(){
        goBack = findViewById(R.id.btnBack)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }

    private fun initListeners(){
        goBack.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_fragment -> {
                    startActivity(Intent(this, MainMenu::class.java))
                    true
                }
                R.id.dashboard_fragment -> {
                    replaceFragment(ContactUs())
                    true
                }
                R.id.settings_fragment -> {
                    replaceFragment(Configuration())
                    true
                }
                R.id.notifications_fragment -> {
                    startActivity(Intent(this, SignInOrLogInActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
