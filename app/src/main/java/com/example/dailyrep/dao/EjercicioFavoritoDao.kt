package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.EjercicioFavorito

@Dao
interface EjercicioFavoritoDao {
    @Query("SELECT EXISTS (SELECT 1 FROM EjercicioFavorito WHERE usuarioId = :usuarioId AND ejercicioId = :ejercicioId)")
    suspend fun esFavorito(usuarioId: String, ejercicioId: Long): Boolean
    @Insert
    fun insertarEjercicioAFavoritos(ejercicioFavorito: EjercicioFavorito)

    @Delete
    fun borrarEjercicioDeFavoritos(ejercicioFavorito: EjercicioFavorito)
}