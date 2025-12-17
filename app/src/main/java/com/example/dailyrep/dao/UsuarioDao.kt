package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dailyrep.dataclases.Usuario

@Dao
interface UsuarioDao {
    @Insert
    fun insertAll(vararg user: Usuario)
    @Query("SELECT * FROM Usuario WHERE id = :userId LIMIT 1")
    suspend fun getUsuarioPorId(userId: String): Usuario?

    @Update
    suspend fun updateUsuario(usuario: Usuario)




}