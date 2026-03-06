package com.example.myapplication.model

data class HistorialPeso(
    private var id : Long,
    private var fecha : String,
    private var peso : Int,
    private var fk_user : Long
)
