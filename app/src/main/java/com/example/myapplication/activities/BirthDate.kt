package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.BirthDateViewModel
import com.example.myapplication.R
import com.example.myapplication.RegistrationData
import com.example.myapplication.fragments.ChangeDate
import com.google.android.material.button.MaterialButton
class BirthDate : TrackedAppCompatActivity() {

    val viewModel : BirthDateViewModel by viewModels()
    private lateinit var btnContinuar: MaterialButton
    private lateinit var btnBack: ImageView
    private lateinit var ibCalendar: ImageButton
    private lateinit var etFechaNacimiento: EditText
    private lateinit var tvErrors : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_birth_date)
        initComponents()
        initListeners()
        initResultListener()
    }
    private fun initComponents() {
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
        ibCalendar = findViewById(R.id.ibCalendar)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        tvErrors = findViewById(R.id.tvErrors)
    }

    private fun initListeners() {

        etFechaNacimiento.doOnTextChanged { text, _, _, _ -> viewModel.actualizaFecha(text.toString())}

        viewModel.error.observe(this){error ->
            tvErrors.text = error
        }
        btnContinuar.setOnClickListener {
            val fecha = etFechaNacimiento.text.toString()
            if (fecha.isNotEmpty()) {
                RegistrationData.fechaNacimiento = fecha
                val intent = Intent(this, HeightAndWeight::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecciona tu fecha de nacimiento", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, Gender::class.java)
            startActivity(intent)
        }

        ibCalendar.setOnClickListener {
            val changeDateDialog = ChangeDate.newInstance()
            changeDateDialog.show(supportFragmentManager, "ChangeDate")
        }
    }

    private fun initResultListener() {
        supportFragmentManager.setFragmentResultListener("REQUEST_DATE", this) { _, bundle ->
            val result = bundle.getString("SELECTED_DATE")
            etFechaNacimiento.setText(result)
        }
    }
}
