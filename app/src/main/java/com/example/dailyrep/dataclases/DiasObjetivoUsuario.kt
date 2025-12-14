package com.example.dailyrep.dataclases

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["usuarioId", "diaSemanaIndice"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class DiasObjetivoUsuario(
    val usuarioId: String,
    val diaSemanaIndice: Int
)
