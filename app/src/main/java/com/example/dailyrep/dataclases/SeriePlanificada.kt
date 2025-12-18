package com.example.dailyrep.dataclases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Ignore

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RelacionEjeRut::class,
            parentColumns = ["id"],
            childColumns = ["relacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SeriePlanificada(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name="relacionId") val relacionId: Long,
    @ColumnInfo(name="numeroSerie")val numeroSerie: Int,
    @ColumnInfo(name="repeticiones")var repeticiones: Int,
    @ColumnInfo(name="peso") var peso:Int,
    @ColumnInfo(name="completado") var completado:Boolean
)
