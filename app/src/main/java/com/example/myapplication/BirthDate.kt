package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class BirthDate : AppCompatActivity() {
    private lateinit var btnContinuar : MaterialButton
    private lateinit var btnBack : ImageView
    private lateinit var ibCalendar : ImageButton
    private lateinit var etFechaNacimiento : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_birth_date)
        initComponents()
        initListeners()
        initResultListener()
    }
    private fun initComponents(){
        btnContinuar = findViewById(R.id.btnContinuar)
        btnBack = findViewById(R.id.btnBack)
        ibCalendar = findViewById(R.id.ibCalendar)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
    }

    private fun initListeners(){
        btnContinuar.setOnClickListener {
            val intent = Intent(this, HeightAndWeight::class.java)
            startActivity(intent)
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
