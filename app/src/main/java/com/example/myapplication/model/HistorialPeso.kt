package com.example.myapplication.model

data class HistorialPeso(
    public var id : Long,
    public var fecha : String,
    public var peso : Int,
    public var fk_user : Long = 1L
)
