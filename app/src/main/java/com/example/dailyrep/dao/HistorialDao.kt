package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.HistorialEntrenamiento

@Dao
interface HistorialDao {
    @Insert
    suspend fun registrarEntrenamiento(historial: HistorialEntrenamiento)

    @Query("SELECT COUNT(*) FROM HistorialEntrenamiento WHERE usuarioId = :usuarioId AND fecha BETWEEN :inicio AND :fin")
    suspend fun contarDiasEntrenadosEnRango(usuarioId: String, inicio: Long, fin: Long): Int

    @Query("SELECT fecha FROM HistorialEntrenamiento WHERE usuarioId = :usuarioId AND fecha BETWEEN :inicio AND :fin")
    suspend fun obtenerFechasSemana(usuarioId: String, inicio: Long, fin: Long): List<Long>
}