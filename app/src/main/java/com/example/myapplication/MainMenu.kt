package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {
    private lateinit var mbRegistroDiario: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        initComponents()
        initListeners()

    }

    private fun initComponents(){
        mbRegistroDiario = findViewById(R.id.mbRegistroDiario)
    }

    private fun initListeners() {
        mbRegistroDiario.setOnClickListener {
            val intent = Intent(this, historial_de_pesos::class.java)
            startActivity(intent)
        }
    }
}