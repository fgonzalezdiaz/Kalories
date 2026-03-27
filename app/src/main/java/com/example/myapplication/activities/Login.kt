package com.example.myapplication.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.UserSession
import com.example.myapplication.itemapi.UserAPI
import kotlinx.coroutines.launch
import android.widget.Toast


class LoginControl : ViewModel(){
    private val _email = MutableLiveData(String())
    val email : LiveData<String> = _email
    private val _contrasena = MutableLiveData(String())
    val contrasena : LiveData<String> = _contrasena

    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?> = _errorEmail
    private val _errorPassword = MutableLiveData<String?>()
    val errorPassword: LiveData<String?> = _errorPassword

    fun actualitzaEmail(email:String){
        _email.value = email
        emailLoginCheck(email)
    }

    fun actualitzaPassword(password : String){
        _contrasena.value = password
        passwordCheck(password)
    }

    fun passwordCheck(password : String){
        if (password.length < 8){
            _errorPassword.value = "Format contrasenya incorrecte"
        } else {
            _errorPassword.value = null
        }
    }
    fun emailLoginCheck (email : String){
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorEmail.value = "Format email incorrecte"
        } else {
            _errorEmail.value = null
        }
    }
}

class Login : AppCompatActivity() {
    private lateinit var btLogIn : Button
    private lateinit var btnBack : ImageView
    private lateinit var emailLoginBox : EditText
    private lateinit var passwordLoginBox : EditText
    private lateinit var tvBenvingut : TextView
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
        tvBenvingut = findViewById(R.id.tvBenvingut)

    }

    private fun initListeners(){
        btnBack.setOnClickListener {
            val intent = Intent(this, SignInOrLogInActivity::class.java)
            startActivity(intent)
        }

        emailLoginBox.doOnTextChanged { text, _, _ , _ ->
            viewModel.actualitzaEmail(text.toString())
        }
        passwordLoginBox.doOnTextChanged { text, _, _ , _ ->
            viewModel.actualitzaPassword(text.toString())
        }

        viewModel.errorEmail.observe(this){error ->
            tvBenvingut.text = error
            tvBenvingut.setTextColor(Color.RED)
        }

        viewModel.errorPassword.observe(this){error ->
            tvBenvingut.text = error
            tvBenvingut.setTextColor(Color.RED)
        }

        btLogIn.setOnClickListener {
            val email = emailLoginBox.text.toString()
            val pass = passwordLoginBox.text.toString()
            
            lifecycleScope.launch {
                try {
                    val response = UserAPI.API().login(email, pass)
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        UserSession.userId = user.id ?: 1L
                        UserSession.email = user.email
                        
                        Toast.makeText(this@Login, "Bienvenido ${user.email}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainMenu::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        tvBenvingut.text = "Error: Credenciales incorrectas"
                        tvBenvingut.setTextColor(Color.RED)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@Login, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
