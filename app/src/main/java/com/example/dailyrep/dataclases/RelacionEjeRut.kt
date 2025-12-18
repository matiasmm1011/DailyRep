package com.example.dailyrep.dataclases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Rutina::class,
            parentColumns = ["id"],
            childColumns = ["rutinaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Ejercicio::class, parentColumns = ["id"], childColumns = ["ejercicioId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class RelacionEjeRut(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name="rutinaId") val rutinaId: Long,
    @ColumnInfo(name="ejercicioId") val ejercicioId: Long,
    @ColumnInfo(name="notas")var notas: String? = null
)