package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class SignInOrLogInActivity : AppCompatActivity() {
    private lateinit var loginButton : MaterialButton
    private lateinit var registerButton : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in_or_log_in)
        initComponents()
        initListeners()
    }
    private fun initComponents(){
        loginButton = findViewById(R.id.mbLogin)
        registerButton = findViewById(R.id.mbRegister)

    }

    private fun initListeners(){
        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}