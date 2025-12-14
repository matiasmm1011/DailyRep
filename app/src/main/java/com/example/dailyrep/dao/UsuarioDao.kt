package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.Usuario

@Dao
interface UsuarioDao {
    @Insert
    fun insertAll(vararg user: Usuario)
}