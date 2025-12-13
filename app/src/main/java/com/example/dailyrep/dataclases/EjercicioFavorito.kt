package com.example.dailyrep.dataclases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["usuarioId", "ejercicioId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Ejercicio::class, parentColumns = ["id"], childColumns = ["ejercicioId"], onDelete = ForeignKey.CASCADE)
    ])
data class EjercicioFavorito(
    @ColumnInfo(name="usuarioId") val usuarioId: String,
    @ColumnInfo(name="ejercicioId") val ejercicioId: Long
)
