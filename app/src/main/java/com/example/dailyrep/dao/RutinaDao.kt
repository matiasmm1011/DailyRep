package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.RelacionEjeRut
import com.example.dailyrep.dataclases.Rutina

@Dao
interface RutinaDao {
    @Query("SELECT * FROM rutina AS R WHERE R.creadorId=:usuarioId")
    fun getAll(usuarioId:String): List<Rutina>

    @Insert
    fun insert(rutina: Rutina):Long

    @Delete
    fun delete(rutina: Rutina)

    @Query("SElECT ej.* FROM relacionejerut as r INNER JOIN EJERCICIO AS ej ON r.ejercicioId=ej.id WHERE r.rutinaId=:rutinaID")
    fun obtenerEjerciciosRutina(rutinaID:Long):List<Ejercicio>

    @Insert
    fun insertAll(vararg rutinaEjemplo: Rutina)

}