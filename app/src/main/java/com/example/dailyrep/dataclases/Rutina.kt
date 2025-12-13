package com.example.dailyrep.dataclases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["creadorId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Rutina(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name="nombreRutina")val nombreRutina: String,
    @ColumnInfo(name="creadorId") val creadorId: String
)
