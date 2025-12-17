package com.example.dailyrep.dataclases

data class itemEntrenamiento(
    val relacion: RelacionEjeRut,
    val ejercicio: Ejercicio,
    val series: MutableList<SeriePlanificada>
)