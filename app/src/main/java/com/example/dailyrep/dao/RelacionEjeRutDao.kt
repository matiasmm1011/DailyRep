package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dailyrep.dataclases.RelacionEjeRut

@Dao
interface RelacionEjeRutDao {
    @Insert
    suspend fun insert(relacion: RelacionEjeRut): Long

    @Insert
    suspend fun insertAll(vararg relacion: RelacionEjeRut)
    @Query("SELECT id FROM relacionejerut AS r WHERE r.rutinaId=:rutinaId AND r.ejercicioId=:ejercicioId")
    fun obtenerIdRelacion(ejercicioId:Long,rutinaId:Long):Long
    @Query("SELECT * FROM relacionejerut WHERE id=:idRelacion")
    fun obtenerRelacion(idRelacion:Long): RelacionEjeRut
}