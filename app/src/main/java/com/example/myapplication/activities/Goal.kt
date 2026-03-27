package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.RegistrationData
import com.example.myapplication.itemapi.UserAPI
import kotlinx.coroutines.launch

class Goal : AppCompatActivity() {
    private lateinit var btnContinuar : Button
    private lateinit var btnBack: ImageView
    private lateinit var spObjectiu: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goal)
        initComponents()
        initListeners()

    }
    private fun initComponents(){
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
        spObjectiu = findViewById(R.id.spObjectiu)
        
        val options = listOf("Perder peso", "Mantener peso", "Ganar músculo")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spObjectiu.adapter = adapter
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            RegistrationData.objetivo = spObjectiu.selectedItemPosition
            btnContinuar.isEnabled = false

            lifecycleScope.launch {
                try {
                    val user = RegistrationData.toUser()
                    android.util.Log.d("REGISTRO", "Enviando: $user")
                    val response = UserAPI.API().create(user)
                    if (response.isSuccessful) {
                        Toast.makeText(this@Goal, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        RegistrationData.clear()
                        val intent = Intent(this@Goal, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Sin detalle"
                        android.util.Log.e("REGISTRO", "Error ${response.code()}: $errorBody")
                        Toast.makeText(this@Goal, "Error ${response.code()}: $errorBody", Toast.LENGTH_LONG).show()
                        btnContinuar.isEnabled = true
                    }
                } catch (e: Exception) {
                    android.util.Log.e("REGISTRO", "Excepción: ${e.message}", e)
                    Toast.makeText(this@Goal, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                    btnContinuar.isEnabled = true
                }
            }
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }
    }
}
