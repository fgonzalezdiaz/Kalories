package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define la clase CustomAdapter que hereda de RecyclerView.Adapter.
// Recibe dataSet (un array de Strings) como parámetro principal.
// Define la clase CustomAdapter que hereda de RecyclerView.Adapter.
// Recibe dataSet (un array de Pesos) como parámetro principal.
class CustomAdapter(private val dataSet: Array<Peso>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // Codigo base cogido de https://developer.android.com/guide/topics/ui/layout/recyclerview

    // Define la clase interna ViewHolder, que representa cada elemento visual.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var pesoYFechaLabel: TextView

        init {
            // Busca el TextView con el ID label_fecha_peso y lo asigna a la variable
            pesoYFechaLabel = view.findViewById(R.id.label_fecha_peso)
        }
    }

    // Crea nuevas vistas (invocado por el layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Crea una nueva vista, definiendo la UI del elemento de la lista
        val view =
                LayoutInflater.from(viewGroup.context)
                    // AQUI INFLA LA VISTA DEL XML, NO CONFUNDIRME CON LABEL_FECHA_PESO
                        .inflate(R.layout.peso_y_fecha, viewGroup, false)

        return ViewHolder(view)
    }

    // Reemplaza el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Obtiene el elemento del dataset en esta posición y reemplaza el contenido de la vista
        // Reconstruimos el string tal como estaba: fecha________________peso
        val item = dataSet[position]
        val textDisplay = "${item.fecha}________________${item.peso}"
        viewHolder.pesoYFechaLabel.text = textDisplay
    }

    // Devuelve el tamaño del dataset (invocado por el layout manager)
    override fun getItemCount() = dataSet.size
}
