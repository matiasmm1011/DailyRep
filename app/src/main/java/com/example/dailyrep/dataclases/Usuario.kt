package com.example.dailyrep.dataclases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey val id: String,
    @ColumnInfo(name="nombre")val nombre:String,
    @ColumnInfo(name="correo")var correo:String,
    @ColumnInfo(name="edad")var edad:Int,
    @ColumnInfo(name="generoMasculino")val generoMasculino:Boolean,
    @ColumnInfo(name="peso")var peso:Int,
    @ColumnInfo(name="altura")var altura:Int,
    @ColumnInfo(name = "rachaActual") val rachaActual: Int = 0,
    @ColumnInfo(name = "ultimoDiaRachaFecha")val ultimoDiaRachaFecha: Long? = null,
    @ColumnInfo(name = "nivelActividadId") val nivelActividadId: Int
)
