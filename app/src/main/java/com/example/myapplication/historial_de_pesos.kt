package com.example.myapplication

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import java.io.File

class historial_de_pesos : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var etFiltre: EditText
    lateinit var ivFiltre: ImageView
    lateinit var etNewPes: EditText
    lateinit var btnIntroducir: MaterialButton
    lateinit var btnAtras: ImageView
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_de_pesos)

        // Inicializar archivo en almacenamiento interno si no existe
        val file = getFile()
        if (!file.exists()) {
            try {
                assets.open("data.txt").use { inputStream ->
                    file.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        etFiltre = findViewById(R.id.etFiltre)
        ivFiltre = findViewById(R.id.ivFiltre)
        etNewPes = findViewById(R.id.etNewPes)
        btnIntroducir = findViewById(R.id.btnIntroducir)
        btnAtras = findViewById(R.id.btnAtras)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        // --------------------------------------- //

        // Inicializamos el recycler view donde Recycler = al layout de recycler
        recycler = findViewById(R.id.rvPesos)

        // Inicializamos el adapter donde CustomAdapter(getItems())
        // devuelve un array de Strings con los datos del archivo data.txt
        recycler.adapter = CustomAdapter(getItems())

        // LinearLayoutManager(this) define que la lista se muestre de forma vertical
        // (uno debajo del otro). Le pasas this (la Activity) porque el gestor necesita acceso
        // a los recursos de la pantalla, como dimensiones y densidad, para saber cómo dibujar
        // y calcular el tamaño de los elementos correctamente.
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun initListeners() {
        btnAtras.setOnClickListener {
            intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // Boton para introducir nuevo peso y actualizar la vista añadiendo otro
        // elemento de recycler view.
        btnIntroducir.setOnClickListener {
            // Obtenemos el texto de EditText
            val text = etNewPes.text.toString()

            if (!text.isEmpty() || !text.isBlank()) {
                // Accedemos a la funcion saveData(text) para guardar el nuevo peso
                // y fecha en el archivo data.txt
                saveData(text)
            }
        }

        // Boton para filtrar la lista de pesos y fechas.
        ivFiltre.setOnClickListener {
            // Obtenemos el texto de EditText
            val text = etFiltre.text.toString()

            // Devolvemos la misma lista sin modificar en caso que la entrada
            // del filtro este vacia.
            if (text.isEmpty() || text.isBlank()) getItems()

            // En caso de recibir una entrada valida la actualizamos
            // la vista con CustomAdapter(getItems(text))
            val adapter = CustomAdapter(getItems(text))
            recycler.adapter = adapter
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_fragment -> {
                    startActivity(Intent(this, MainMenu::class.java))
                    true
                }
                R.id.dashboard_fragment -> {
                    replaceFragment(ContactUs())
                    true
                }
                R.id.settings_fragment -> {
                    replaceFragment(Configuration())
                    true
                }
                R.id.notifications_fragment -> {
                    startActivity(Intent(this, SignInOrLogInActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    private fun saveData(pes: String) {
        // DD-MM-YYYY_______________***KG
        val cal = Calendar.getInstance()
        val dia = cal.get(Calendar.DAY_OF_MONTH)
        val mes = cal.get(Calendar.MONTH) + 1 // + 1 porque enero es 0
        val anio = cal.get(Calendar.YEAR)
        val fecha = String.format("%02d-%02d-%04d", dia, mes, anio)
        val data = "\n" + fecha + "________________" + pes + "KG"

        try {
            // Try catch por si el archivo no existe
            // Obtenemos el archivo data.txt y lo guardamos en una variable
            getFile().appendText(data)
            // Actualizar la lista despues de guardar
            // La actualizamos con CustomAdapter(getItems()) que getItems()
            // devuelve un array de Strings con los datos del archivo data.txt
            // y se lo pasa por parametro al CustomAdapter que se encarga
            // de mostrar los datos en el recycler
            val adapter = CustomAdapter(getItems())
            recycler.adapter = adapter

            // Limpiamos la entrada de texto
            etNewPes.text.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Get file te devuelve el archivo de la carpeta de almacenamiento
    // interno de la app mientras se ejecuta ya que en assets no me
    // deja modificarla en ejecucion, solo leerla.
    private fun getFile(): File {
        return File(filesDir, "data.txt")
    }

    // getItems(String) a diferencia de getItems() retorna una llista de objectes
    // pero nomes si la linia conte el filtre:String dins.
    private fun getItems(filtre: String): Array<Peso> {
        // Inicialitzem un ArrayList
        val pesoYFecha = ArrayList<Peso>()

        // Comprovem que l'arxiu existeix.
        if (getFile().exists()) {
            // Per cada linia de l'arxiu feim una cerca amb el .contains,
            // y si la linia conte el text de filtre, s'afegira dintre el
            // ArrayList<Peso>()
            getFile().forEachLine { line ->
                if (line.contains(filtre) && !line.isEmpty() && !line.isBlank()) {
                    val parts = line.split("________________")
                    if (parts.size == 2) {
                        pesoYFecha.add(Peso(parts[0], parts[1]))
                    }
                }
            }
        }

        // Tornam un Array, ja que recordem que el customAdapter rep un array no
        // un array list.
        return pesoYFecha.toTypedArray()
    }

    // Retorna una llista amb tot els elements de dades.txt.
    private fun getItems(): Array<Peso> {
        val pesoYFecha = ArrayList<Peso>()
        if (getFile().exists()) {
            getFile().forEachLine { line ->
                if (!line.isEmpty() && !line.isBlank()) {
                    val parts = line.split("________________")
                    // En caso de que no queden 2 elementos despues del split no se añade
                    if (parts.size == 2) {
                        pesoYFecha.add(Peso(parts[0], parts[1]))
                    }
                }
            }
        }
        return pesoYFecha.toTypedArray()
    }
}

// Peso(data class) esta puesto porque en la teoria sale pero nunca lo utilizamos.
// Preguntar a david
data class Peso(val fecha: String, val peso: String)
