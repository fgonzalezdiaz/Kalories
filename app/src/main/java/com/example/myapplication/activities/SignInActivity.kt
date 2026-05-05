package com.example.myapplication.activities

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
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.R
import com.example.myapplication.RegistrationData
import com.example.myapplication.viewmodel.SignInControl
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Comandos para tests
 * ./gradlew test
 * ./gradlew connectedAndroidTest
 * */

class SignInActivity : TrackedAppCompatActivity() {
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
            RegistrationData.email = tietEmail.text.toString()
            RegistrationData.contrasena = tietPassword.text.toString()
            
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
        mbRegister.setOnClickListener {
            // Validar antes de mostrar el diálogo
            viewModel.checkEmail()
            viewModel.checkPassword()
            viewModel.checkPassword2()

            if (viewModel.errorEmail.value == null &&
                viewModel.errorPassword.value == null &&
                viewModel.errorPassword2.value == null) {
                dialog.show()
            } else {
                Toast.makeText(this, "Por favor, corrige los errores antes de continuar", Toast.LENGTH_SHORT).show()
            }
        }

        tietEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.actualizaEmail(text.toString()) }

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
