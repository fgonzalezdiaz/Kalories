package com.example.myapplication

import android.app.Application
import com.example.myapplication.helpers.SumadorTiempoUso

class KaloriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SumadorTiempoUso.init(this)
    }
}
