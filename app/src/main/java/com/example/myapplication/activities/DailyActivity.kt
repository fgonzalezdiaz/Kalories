package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import com.example.myapplication.RegistrationData

class DailyActivity : TrackedAppCompatActivity() {
    private lateinit var btnContinuar : Button
    private lateinit var btnBack : ImageView
    private lateinit var spActivityLevel: android.widget.Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.daily_activity)
        initComponents()
        initListeners()
    }
    private fun initComponents(){
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
        spActivityLevel = findViewById(R.id.spActivityLevel)
        
        val options = listOf("Sedentario", "Ligero", "Moderado", "Intenso")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spActivityLevel.adapter = adapter
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            RegistrationData.nivelActividad = spActivityLevel.selectedItemPosition
            val intent = Intent(this, Goal::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, HeightAndWeight::class.java)
            startActivity(intent)
        }
    }
}
