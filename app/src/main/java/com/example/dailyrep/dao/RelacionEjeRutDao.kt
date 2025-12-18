package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.RelacionEjeRut
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada

@Dao
interface RelacionEjeRutDao {
    @Insert
    suspend fun insert(relacion: RelacionEjeRut): Long

    @Insert
    suspend fun insertAll(vararg relacion: RelacionEjeRut)

    @Update
    fun actualizarNotas(relacion: RelacionEjeRut)
    @Delete
    fun delete(relacion: RelacionEjeRut)
    @Query("SELECT id FROM relacionejerut AS r WHERE r.rutinaId=:rutinaId AND r.ejercicioId=:ejercicioId")
    fun obtenerIdRelacion(ejercicioId:Long,rutinaId:Long):Long
    @Query("SELECT * FROM relacionejerut WHERE id=:idRelacion")
    fun obtenerRelacion(idRelacion:Long): RelacionEjeRut

    @Query("SELECT rutinaId FROM relacionejerut WHERE id=:idRelacion")
    fun obtenerRutina(idRelacion:Long): Long
}