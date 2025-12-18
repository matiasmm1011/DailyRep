package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dailyrep.dataclases.SeriePlanificada

@Dao
interface SerieDao {
    @Query("SELECT * FROM SeriePlanificada WHERE relacionId=:idRelacion ORDER BY numeroSerie ASC")
    fun obtenerSeries(idRelacion:Long):MutableList<SeriePlanificada>

    @Query("SELECT MAX(numeroSerie) FROM SeriePlanificada WHERE relacionId = :idRelacion")
    fun obtenerUltimoNumeroSerie(idRelacion: Long): Int?

    @Query("DELETE FROM SeriePlanificada WHERE id = :serieId")
    fun eliminarSeriePorId(serieId: Long)

    @Update
    fun actualizarSerie(serie: SeriePlanificada)

    @Insert
    fun insert(serie: SeriePlanificada):Long

    @Query("""
        SELECT MAX(s.peso) 
        FROM SeriePlanificada s
        INNER JOIN RelacionEjeRut rel ON s.relacionId = rel.id
        INNER JOIN Rutina r ON rel.rutinaId = r.id
        INNER JOIN Ejercicio e ON rel.ejercicioId = e.id
        WHERE r.creadorId = :usuarioId 
        AND e.nombre= :nombreEjercicio
    """)
    suspend fun obtenerPRMaximo(usuarioId: String, nombreEjercicio: String): Int?

}