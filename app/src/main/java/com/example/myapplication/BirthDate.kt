package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class BirthDate : AppCompatActivity() {
    private lateinit var btnContinuar : MaterialButton
    private lateinit var btnBack : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_birth_date)
        initComponents()
        initListeners()
    }
    private fun initComponents(){
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            val intent = Intent(this, HeightAndWeight::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, Gender::class.java)
            startActivity(intent)
        }
    }
}