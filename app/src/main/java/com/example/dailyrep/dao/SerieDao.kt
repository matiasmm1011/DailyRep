package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.SeriePlanificada

@Dao
interface SerieDao {
    @Query("SELECT * FROM SeriePlanificada WHERE relacionId=:idRelacion ORDER BY numeroSerie ASC")
    fun obtenerSeries(idRelacion:Long):MutableList<SeriePlanificada>

    @Query("SELECT MAX(numeroSerie) FROM SeriePlanificada WHERE relacionId = :idRelacion")
    fun obtenerUltimoNumeroSerie(idRelacion: Long): Int?

    @Query("DELETE FROM SeriePlanificada WHERE id = :serieId")
    fun eliminarSeriePorId(serieId: Long)

    @Insert
    fun insert(serie: SeriePlanificada):Long

}