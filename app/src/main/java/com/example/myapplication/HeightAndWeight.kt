package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HeightAndWeight : AppCompatActivity() {
    private lateinit var btnContinuar : Button
    private lateinit var btnBack : ImageView
    private lateinit var npHeight: android.widget.NumberPicker
    private lateinit var npWeight: android.widget.NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_height_and_weight)
        initComponents()
        initListeners()
    }

    private fun initComponents(){
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
        npHeight = findViewById(R.id.npHeight)
        npWeight = findViewById(R.id.npWeight)
        
        npHeight.minValue = 100
        npHeight.maxValue = 250
        npHeight.value = 170
        
        npWeight.minValue = 30
        npWeight.maxValue = 200
        npWeight.value = 70
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            RegistrationData.altura = npHeight.value
            RegistrationData.peso = npWeight.value.toDouble()
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, BirthDate::class.java)
            startActivity(intent)
        }
    }
}