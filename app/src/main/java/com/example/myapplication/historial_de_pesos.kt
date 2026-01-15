package com.example.myapplication

import android.content.Intent
import android.icu.util.Calendar
import android.media.Image
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class historial_de_pesos : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var etFiltre : EditText
    lateinit var ivFiltre : ImageView
    lateinit var etNewPes : EditText
    lateinit var btnIntroducir : MaterialButton
    lateinit var btnAtras : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_de_pesos)

        // Inicializar archivo en almacenamiento interno si no existe
        val file = getFile()
        if (!file.exists()) {
            try {
                assets.open("data.txt").use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        initComponents()
        initListeners()

        recycler  = findViewById(R.id.rvPesos)
        val layoutManager = LinearLayoutManager(this)
        val adapter = CustomAdapter(getItems())


        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
    }

    private fun initComponents(){
        etFiltre = findViewById(R.id.etFiltre)
        ivFiltre = findViewById(R.id.ivFiltre)
        etNewPes = findViewById(R.id.etNewPes)
        btnIntroducir = findViewById(R.id.btnIntroducir)
        btnAtras = findViewById(R.id.btnAtras)
    }

    private fun initListeners(){
        btnAtras.setOnClickListener {
            intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        btnIntroducir.setOnClickListener {
            val text = etNewPes.text.toString()
            if(!text.isEmpty() || !text.isBlank()){
                saveData(text)
            }
        }

        ivFiltre.setOnClickListener {
            val text = etFiltre.text.toString()
            if(text.isEmpty() || text.isBlank()) getItems()
            val adapter = CustomAdapter(getItems(text))
            recycler.adapter = adapter
        }
    }

    private fun saveData (pes : String){
        // DD-MM-YYYY_______________***KG
        val cal = Calendar.getInstance()
        val dia = cal.get(Calendar.DAY_OF_MONTH)
        val mes = cal.get(Calendar.MONTH) + 1 // + 1 porque enero es 0
        val anio = cal.get(Calendar.YEAR)
        val fecha = String.format("%02d-%02d-%04d", dia, mes, anio)
        val data = "\n" + fecha + "________________" + pes + "KG"
        
        try {
            // Get file te devuelve el archivo de la carpeta de almacenamiento interno de la app mientras se ejecuta
            // ya que en assets no me deja modificarla en ejecucion, solo leerla.
            // Try catch por si el archivo no existe
            getFile().appendText(data)
            // Actualizar la lista despues de guardar
            val adapter = CustomAdapter(getItems())
            recycler.adapter = adapter
            etNewPes.text.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFile(): File {
        return File(filesDir, "data.txt")
    }

    private fun getItems(filtre : String): Array<String> {
        val pesoYFecha = ArrayList<String>()
        if (getFile().exists()) {
            getFile().forEachLine { line ->
                if(line.contains(filtre)) {
                    pesoYFecha.add(line)
                }
            }
        }
        return pesoYFecha.toTypedArray()
    }

    private fun getItems(): Array<String> {
        val pesoYFecha = ArrayList<String>()
        if (getFile().exists()) {
            getFile().forEachLine { line ->
                pesoYFecha.add(line)
            }
        }
        return pesoYFecha.toTypedArray()
    }
}

data class Peso(val fecha: String, val peso: String)