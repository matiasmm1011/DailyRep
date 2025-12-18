package com.example.dailyrep.dataclases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistorialEntrenamiento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: String,
    val fecha: Long
)