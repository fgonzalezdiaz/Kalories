package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CustomAdapter
import com.example.myapplication.HistorialViewModel
import com.example.myapplication.R
import com.example.myapplication.UserSession
import com.example.myapplication.fragments.Configuration
import com.example.myapplication.fragments.ContactUs
import com.example.myapplication.model.HistorialPeso
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class historial_de_pesos : TrackedAppCompatActivity() {

    private val viewModel: HistorialViewModel by viewModels()
    private lateinit var adapter: CustomAdapter
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

        initComponents()
        initListeners()
        observaDatos()

        viewModel.obtenerHistorial()
    }

    private fun initComponents() {
        etFiltre = findViewById(R.id.etFiltre)
        ivFiltre = findViewById(R.id.ivFiltre)
        etNewPes = findViewById(R.id.etNewPes)
        btnIntroducir = findViewById(R.id.btnIntroducir)
        btnAtras = findViewById(R.id.btnAtras)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        recycler = findViewById(R.id.rvPesos)
        // - --------------------- - //

        adapter = CustomAdapter(emptyList()) { item ->
            mostrarOpcionesPeso(item)
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    public fun observaDatos() {
        viewModel.listaHistorial.observe(this) { lista ->
            adapter.actualizarDatos(lista)
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun initListeners() {
        btnAtras.setOnClickListener {
            intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }


        // Boton para introducir nuevo peso y actualizar la vista añadiendo otro
        // elemento de recycler view.
        btnIntroducir.setOnClickListener {
            val text = etNewPes.text.toString()

            if (text.isNotBlank()) {
                // Generamos la fecha con el formato que requiere tu API: yyyy-MM-dd'T'HH:mm:ss
                // O el formato que use el service de Retrofit. 
                // Segun HistorialPesoAPI usa "yyyy-MM-dd'T'HH:mm:ss"
                val cal = Calendar.getInstance()
                val fecha = String.format(
                    "%04d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )

                // Convertimos el texto a número
                val pesoInt = text.toIntOrNull() ?: 0

                // Enviamos los datos al ViewModel para que los guarde en la BBDD
                // Usamos el ID del usuario logueado en la sesión
                viewModel.crearNuevoPeso(fecha, pesoInt, UserSession.userId)

                etNewPes.text.clear()
            }
        }

        // Boton para filtrar la lista de pesos y fechas.
        ivFiltre.setOnClickListener {
            val filtro = etFiltre.text.toString()
            viewModel.filtrarLista(filtro)
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

    private fun mostrarOpcionesPeso(item: HistorialPeso) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Opciones del registro")
        builder.setMessage("¿Qué desea hacer con el registro de ${item.fecha} (${item.peso}kg)?")
        
        builder.setPositiveButton("Modificar") { _, _ ->
            mostrarDialogoModificar(item)
        }
        builder.setNegativeButton("Eliminar") { _, _ ->
            confirmarEliminar(item)
        }
        builder.setNeutralButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun mostrarDialogoModificar(item: HistorialPeso) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Modificar peso")
        
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        input.setText(item.peso.toString())
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoPeso = input.text.toString().toIntOrNull()
            if (nuevoPeso != null) {
                viewModel.modificarPeso(item.id, item.fecha, nuevoPeso)
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun confirmarEliminar(item: HistorialPeso) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
        builder.setMessage("¿Estás seguro de que quieres eliminar este registro?")
        
        builder.setPositiveButton("Eliminar") { _, _ ->
            viewModel.eliminarPeso(item)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}


// Peso(data class) esta puesto porque en la teoria sale pero nunca lo utilizamos.
// Preguntar a david
