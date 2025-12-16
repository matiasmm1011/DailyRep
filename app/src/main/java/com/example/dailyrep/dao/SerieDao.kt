package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.SeriePlanificada

@Dao
interface SerieDao {
    @Query("SELECT * FROM SeriePlanificada WHERE relacionId=:idRelacion ORDER BY numeroSerie ASC")
    fun obtenerSeries(idRelacion:Long):List<SeriePlanificada>
    @Insert
    fun insert(serie: SeriePlanificada):Long
}