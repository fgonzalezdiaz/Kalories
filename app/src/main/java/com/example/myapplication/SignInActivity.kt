package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class SignInControl : ViewModel(){
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email
    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _email
    private val _password2 = MutableLiveData<String>()
    val password2 : LiveData<String> = _password2

    fun checkData(email: String, password: String, password2: String) : String{
        if(!checkEmail(email)){
            return "Formato de email incorrecto"
        }
        if(!checkPassword(password)){
            return "La contraseña debe tener al menos 8 caracteres"
        }
        if(!checkPassword(password, password2)){
            return "Las contraseñas no coinciden"
        }
        return ""
    }
    fun checkEmail(email: String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun checkPassword(password: String) : Boolean{
        if(password.length < 8){
            return false
        }
        return true
    }
    fun checkPassword(password: String, password2: String) : Boolean{
        if(password != password2){
            return false
        }
        return true
    }
}
class SignInActivity : AppCompatActivity() {
    private lateinit var mbRegister : MaterialButton
    private lateinit var btnBack : ImageView
    private lateinit var tietEmail : TextInputEditText
    private lateinit var tietPassword : TextInputEditText
    private lateinit var tietPassword2 : TextInputEditText
    private lateinit var tvInfo : TextView
    private val viewModel : SignInControl by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        initComponents()
        initListeners()

    }



    private fun initComponents(){
        mbRegister = findViewById(R.id.mbRegister)
        btnBack = findViewById(R.id.btnBack)
        tietEmail = findViewById(R.id.tietEmail)
        tietPassword = findViewById(R.id.tietPassword)
        tietPassword2 = findViewById(R.id.tietPassword2)
        tvInfo = findViewById(R.id.tvInfo)
    }

    private fun initListeners(){
        btnBack.setOnClickListener {
            val intent = Intent(this, SignInOrLogInActivity::class.java)
            startActivity(intent)
        }
        mbRegister.setOnClickListener{
            val checkedData = viewModel.checkData(tietEmail.text.toString(), tietPassword.text.toString(), tietPassword2.text.toString())
            //tvInfo muestra el mensaje de checkedData para no crear otro TV
            if(checkedData != ""){
                tvInfo.text = checkedData
            } else {
                val intent = Intent(this, Gender::class.java)
                startActivity(intent)
            }
        }
    }
}