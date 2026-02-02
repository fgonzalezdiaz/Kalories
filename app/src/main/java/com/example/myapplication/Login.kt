package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LoginControl : ViewModel(){
    private val _usuari = MutableLiveData(String())
    val usuari : LiveData<String> = _usuari
    private val _contrasena = MutableLiveData(String())
    val contrasena : LiveData<String> = _contrasena

    fun usuariLoginChek (usuari: String): Boolean{

        if (usuari.length <8){
            return false
        } else {
            return true
        }
    }
    fun emailLoginChek (email : String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

class Login : AppCompatActivity() {
    private lateinit var btLogIn : Button
    private lateinit var btnBack : ImageView
    private lateinit var emailLoginBox : EditText
    private lateinit var passwordLoginBox : EditText
    private val viewModel : LoginControl by viewModels()
    private lateinit var loginControl: LoginControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        initComponents()
        initListeners()
    }
    private fun initComponents(){
        btnBack = findViewById(R.id.btnBack)
        btLogIn = findViewById(R.id.btLogIn)
        emailLoginBox = findViewById(R.id.etEmail)
        passwordLoginBox = findViewById(R.id.etContraseña)


    }

    private fun initListeners(){
        btnBack.setOnClickListener {
            val intent = Intent(this, SignInOrLogInActivity::class.java)
            startActivity(intent)
        }

        btLogIn.setOnClickListener {

            val email = emailLoginBox.text.toString()
            val password = passwordLoginBox.text.toString()

            val emailOk = viewModel.emailLoginChek(email)
            val passwordOk = viewModel.usuariLoginChek(password)


            if (emailOk && passwordOk) {
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            } else {

                if (!emailOk) {
                    emailLoginBox.error = "Email incorrecto"
                }
                if (!passwordOk) {
                    passwordLoginBox.error = "La contraseña no tiene 8 caracteres"
                }
            }
        }
    }
}