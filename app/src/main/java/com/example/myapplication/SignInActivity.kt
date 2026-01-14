package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class SignInActivity : AppCompatActivity() {
    private lateinit var mbRegister : MaterialButton
    private lateinit var btnBack : ImageView

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

    }

    private fun initListeners(){
        mbRegister.setOnClickListener {
            val intent = Intent(this, Gender::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, SignInOrLogInActivity::class.java)
            startActivity(intent)
        }
    }
}