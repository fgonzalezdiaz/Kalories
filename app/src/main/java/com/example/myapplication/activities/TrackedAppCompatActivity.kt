package com.example.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.helpers.SumadorTiempoUso

// Clase base para controlar el tiempo de uso en cada pantalla
open class TrackedAppCompatActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        SumadorTiempoUso.onActivityResumed(this)
    }

    override fun onPause() {
        SumadorTiempoUso.onActivityPaused(this)
        super.onPause()
    }
}
