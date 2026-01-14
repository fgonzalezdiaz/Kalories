package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class historial_de_pesos : AppCompatActivity() {
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_de_pesos)

        recycler  = findViewById(R.id.rvPesos)
        val layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(getItems())

        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
    }

    private fun getItems(): Array<String> {
        lateinit var pesoYFecha : ArrayList<String>

        while(true){
            val archivo = File("../../sampledata/data.txt")

            archivo.forEachLine { linea ->
                pesoYFecha.add(linea)
            }
        }

    }
}

data class Peso(val fecha: String, val peso: String)