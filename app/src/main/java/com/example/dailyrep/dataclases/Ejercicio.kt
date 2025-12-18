package com.example.dailyrep.dataclases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Ejercicio(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name="nombre") val nombre:String,
    @ColumnInfo(name="tipoEjercicio")val tipo:String,
    @ColumnInfo(name="parteCuerpo")val parteCuerpo:String,
    @ColumnInfo(name="descripcion")var descripcion:String,
    @ColumnInfo(name="esPredeterminado")val esPredeterminado: Boolean = true,
    @ColumnInfo(name="creadorId")val creadorId: String? = null,
    @ColumnInfo(name="nombreImagen")val nombreImagen: String? = null,
    @ColumnInfo(name="esCardio")val esCardio: Boolean

){
    @Ignore
    var esFavorito: Boolean = false

    @Ignore
    var seleccionado: Boolean = false
}
