package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InitialScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial_screen)

        val logo = findViewById<android.widget.ImageView>(R.id.ivLogo)
        
        // escala inicial de el logo de imagen
        logo.alpha = 0f
        logo.scaleX = 0.5f
        logo.scaleY = 0.5f
        //inicia la animacion
        logo.animate()
            //opacidad
            .alpha(1f)
            //tamaño en x
            .scaleX(1f)
            //tamaño en y
            .scaleY(1f)
            // cuanto dura la animacion
            .setDuration(2000)
            //como dice el nombre al terminar la animacion se ejecuta la acticity SignInOrLogInActivity
            .withEndAction {
                val intent = android.content.Intent(this, SignInOrLogInActivity::class.java)
                startActivity(intent)
                finish()
            }
            .start()
    }
}