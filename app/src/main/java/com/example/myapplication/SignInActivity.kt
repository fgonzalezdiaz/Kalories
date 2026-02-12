package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignInControl : ViewModel() {

    private val _tvInfo = MutableLiveData<TextView>()
    val tvInfo: LiveData<TextView> = _tvInfo
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email //
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _email //
    private val _password2 = MutableLiveData<String>()
    val password2: LiveData<String> = _password2 //
    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?> = _errorEmail
    private val _errorPassword = MutableLiveData<String?>()
    val errorPassword: LiveData<String?> = _errorPassword
    private val _errorPassword2 = MutableLiveData<String?>()
    val errorPassword2: LiveData<String?> = _errorPassword2

    fun actualizaEmail(email: String) {
        _email.value = email
        checkEmail()
    }

    fun actualizaPassword(password: String) {
        _password.value = password
        checkPassword()
    }

    fun actualizaPassword2(password2: String) {
        _password2.value = password2
        checkPassword2()
    }

    fun checkEmail() {
        val email = _email.value
        if (email == null || email.isEmpty()) {
            _errorEmail.value = "El email no puede estar vacío"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorEmail.value = "El email no es válido"
        } else {
            _errorEmail.value = null
        }
    }
    fun checkPassword() {
        val password = _password.value
        if (password == null || password.isEmpty()) {
            _errorPassword.value = "La contraseña no puede estar vacía"
        } else if (password.length < 6) {
            _errorPassword.value = "La contraseña debe tener al menos 6 caracteres"
        } else {
            _errorPassword.value = null
        }
    }
    fun checkPassword2() {
        val password2 = _password2.value
        if (password2 == null || password2.isEmpty()) {
            _errorPassword2.value = "La contraseña no puede estar vacía"
        } else if (password2 != _password.value) {
            _errorPassword2.value = "Las contraseñas no coinciden"
        } else {
            _errorPassword2.value = null
        }
    }
}

class SignInActivity : AppCompatActivity() {
    private lateinit var mbRegister: MaterialButton
    private lateinit var btnBack: ImageView
    private lateinit var tietEmail: TextInputEditText
    private lateinit var tietPassword: TextInputEditText
    private lateinit var tietPassword2: TextInputEditText
    private lateinit var tvInfo: TextView
    private val viewModel: SignInControl by viewModels()

    // Declaramos el builder y el Dialog como una Lateinit
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        mbRegister = findViewById(R.id.mbRegister)
        btnBack = findViewById(R.id.btnBack)
        tietEmail = findViewById(R.id.tietEmail)
        tietPassword = findViewById(R.id.tietPassword)
        tietPassword2 = findViewById(R.id.tietPassword2)
        tvInfo = findViewById(R.id.tvInfo)

        // INICIALIZAMOS EL CONSTRUCTOR YA DECLARADO
        builder = AlertDialog.Builder(this)
        builder.setTitle("RECUERDA TU CONTRASEÑA")
        builder.setMessage("¿Estás seguro de que quieres continuar?")

        // Botón de confirmación
        builder.setPositiveButton("Sí") { dialog, which ->
            // Toast para mostrar mensaje de confirmacion y a continuacion logica de codigo
            Toast.makeText(this, "Continuamos", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Gender::class.java)
            startActivity(intent)
        }
        // Botón de cancelación y vuelta atras
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss() // Cierra el diálogo
        }
        // Creamos el dialog una vez creado el builder
        dialog = builder.create()
    }

    private fun initListeners() {
        btnBack.setOnClickListener {
            val intent = Intent(this, SignInOrLogInActivity::class.java)
            startActivity(intent)
        }
        mbRegister.setOnClickListener { dialog.show() }

        tietEmail.doOnTextChanged { text, _, _, _ -> viewModel.actualizaEmail(text.toString()) }
        tietPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.actualizaPassword(text.toString())
        }
        tietPassword2.doOnTextChanged { text, _, _, _ ->
            viewModel.actualizaPassword2(text.toString())
        }

        viewModel.errorEmail.observe(this) { error ->
            tvInfo.text = error
            tvInfo.setTextColor(Color.RED)
        }

        viewModel.errorPassword.observe(this) { error ->
            tvInfo.text = error
            tvInfo.setTextColor(Color.RED)
        }
        viewModel.errorPassword2.observe(this) { error ->
            tvInfo.text = error
            tvInfo.setTextColor(Color.RED)
        }
    }
}
