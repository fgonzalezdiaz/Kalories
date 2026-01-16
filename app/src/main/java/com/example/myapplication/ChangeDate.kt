package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import java.util.Calendar
import java.util.Locale

class ChangeDate : DialogFragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var tvDateDisplay: TextView
    private lateinit var btnOk: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnBack: ImageView

    private var selectedDay: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_date, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        tvDateDisplay = view.findViewById(R.id.tvDateDisplay)
        btnOk = view.findViewById(R.id.btnOk)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnBack = view.findViewById(R.id.btnBack)

        // Initialize with current date or passed arguments
        val calendar = Calendar.getInstance()
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
        selectedMonth = calendar.get(Calendar.MONTH)
        selectedYear = calendar.get(Calendar.YEAR)

        updateDateDisplay()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedYear = year
            selectedMonth = month
            selectedDay = dayOfMonth
            updateDateDisplay()
        }

        btnOk.setOnClickListener {
            val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
            
            val result = Bundle().apply {
                putString("SELECTED_DATE", formattedDate)
            }
            parentFragmentManager.setFragmentResult("REQUEST_DATE", result)
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun updateDateDisplay() {
        val months = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        tvDateDisplay.text = String.format(Locale.getDefault(), "%d de %s %d", selectedDay, months[selectedMonth], selectedYear)
    }

    companion object {
        fun newInstance() = ChangeDate()
    }
}