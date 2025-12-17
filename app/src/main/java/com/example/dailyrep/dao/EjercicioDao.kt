package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface EjercicioDao {
    @Query("SELECT * FROM ejercicio")
    fun getAll(): List<Ejercicio>
    @Query(" SELECT * FROM Ejercicio WHERE (esPredeterminado = 1 OR creadorId = :usuarioId) AND (:searchText IS NULL OR LOWER(nombre) LIKE '%' || LOWER(:searchText) || '%') ORDER BY nombre ASC")
    suspend fun getEjerciciosGeneralesConBusqueda(usuarioId: String, searchText: String?): List<Ejercicio>

    @Query(" SELECT E.* FROM Ejercicio AS E INNER JOIN EjercicioFavorito AS EF ON E.id = EF.ejercicioId WHERE EF.usuarioId = :usuarioId AND (:searchText IS NULL OR LOWER(E.nombre) LIKE '%' || LOWER(:searchText) || '%') ORDER BY E.nombre ASC")
    suspend fun getEjerciciosFavoritosConBusqueda(usuarioId: String, searchText: String?): List<Ejercicio>
    @Insert
    suspend fun insert(ejercicio: Ejercicio):Long
    @Query("SELECT id FROM EjercicioFavorito AS EF INNER JOIN Ejercicio AS E ON E.id=EF.ejercicioId WHERE EF.usuarioId = :usuarioId ")
    suspend fun obtenerIdsFavoritos(usuarioId:String):List<Long>
    @Insert
    fun insertAll(vararg ejercicio:Ejercicio)



}