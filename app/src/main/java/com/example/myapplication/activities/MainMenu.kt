package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.fragments.Configuration
import com.example.myapplication.fragments.ContactUs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MainMenu : TrackedAppCompatActivity() {
    private lateinit var mbRegistroDiario: Button
    private lateinit var mbWeightHistory : MaterialButton
    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        initComponents()
        initListeners()

    }

    private fun initComponents(){
        bottomNavigation = findViewById(R.id.bottom_navigation)
        mbRegistroDiario = findViewById(R.id.mbRegistroDiario)
        mbWeightHistory = findViewById(R.id.mbWeightHistory)
    }


    private fun initListeners() {

        mbRegistroDiario.setOnClickListener {
            val intent = Intent(this, DailyReports::class.java)
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
        mbWeightHistory.setOnClickListener {
            val intent = Intent(this, historial_de_pesos::class.java)
            startActivity(intent)
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    /*
    Dentro de Fragment, en el onViewCreated
        binding.btnCerrar.setOnClickListener {
    Esto simula que el usuario presionó el botón "Atrás" del móvil
            parentFragmentManager.popBackStack()
        }
    */
}
