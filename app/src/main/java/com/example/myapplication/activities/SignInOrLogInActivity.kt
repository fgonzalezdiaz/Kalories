package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignInOrLogInBinding

class SignInOrLogInActivity : AppCompatActivity() {

    // Iniciamos el Binding para poder asi utilizar las variables de dentro del activity
    // como en este caso el boton Login y Registrarse, sin necesidad del FindViewById()
    private lateinit var binding: ActivitySignInOrLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializacion del Binding
        binding = ActivitySignInOrLogInBinding.inflate(layoutInflater)
        // Seleccionamos el activity raiz para que sepa que tiene que ser
        // este activity el que usa.
        setContentView(binding.root)

        enableEdgeToEdge()
        initComponents()
        initListeners()
    }
    private fun initComponents() {}

    private fun initListeners() {

        // Ejemplo de uso para un click listener
        binding.mbLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.mbRegister.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
