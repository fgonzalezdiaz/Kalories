package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import com.example.myapplication.RegistrationData
import com.google.android.material.button.MaterialButton

class Gender : TrackedAppCompatActivity() {
    private val opciones = listOf("Hombre", "Mujer")

    private lateinit var mbContinuar : MaterialButton
    private lateinit var btnBack : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.gender)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opciones)
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
            val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.tvSexSelector)
            val genero = autoCompleteTextView.text.toString()
            if (genero.isNotEmpty()) {
                RegistrationData.genero = genero
                val intent = Intent(this, BirthDate::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecciona tu género", Toast.LENGTH_SHORT).show()
            }
        }
        btnBack.setOnClickListener {
            val intent = Intent( this, SignInActivity::class.java)
            startActivity(intent)
        }


    }
}
