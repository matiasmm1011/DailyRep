package com.example.dailyrep.dataclases

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RelacionEjeRut::class,
            parentColumns = ["id"],
            childColumns = ["refId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SeriePlanificada(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name="refId") val refId: Long,
    @ColumnInfo(name="numeroSerie")val numeroSerie: Int,
    @ColumnInfo(name="repeticiones")val repeticiones: String,
)
