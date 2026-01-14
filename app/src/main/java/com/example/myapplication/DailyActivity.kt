package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DailyActivity : AppCompatActivity() {
    private lateinit var btnContinuar : Button
    private lateinit var btnBack : ImageView

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
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            val intent = Intent(this, Goal::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, HeightAndWeight::class.java)
            startActivity(intent)
        }
    }
}