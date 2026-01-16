package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlin.toString

class Gender : AppCompatActivity() {
    private val opciones = listOf("Hombre", "Mujer")

    private lateinit var mbContinuar : MaterialButton
    private lateinit var btnBack : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.gender)
        val adapter = ArrayAdapter(this, R.layout.list_item, opciones)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.tvSexSelector)
        autoCompleteTextView.setAdapter(adapter)
        initComponents()
        initListeners()
    }

    private fun initComponents(){
        mbContinuar = findViewById(R.id.mbContinuar)
        btnBack = findViewById(R.id.btnBack)
    }


    private fun initListeners(){


        mbContinuar.setOnClickListener {
            val intent = Intent(this, BirthDate::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent( this, SignInActivity::class.java)
            startActivity(intent)
        }


    }
}