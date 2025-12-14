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
    fun getEjerciciosGeneralesConBusqueda(usuarioId: String, searchText: String?): Flow<List<Ejercicio>>

    @Query(" SELECT E.* FROM Ejercicio AS E INNER JOIN EjercicioFavorito AS EF ON E.id = EF.ejercicioId WHERE EF.usuarioId = :usuarioId AND (:searchText IS NULL OR LOWER(E.nombre) LIKE '%' || LOWER(:searchText) || '%') ORDER BY E.nombre ASC")
    fun getEjerciciosFavoritosConBusqueda(usuarioId: String, searchText: String?): Flow<List<Ejercicio>>
    @Insert
    fun insertAll(vararg ejercicio: Ejercicio)
}