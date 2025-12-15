package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.Rutina

@Dao
interface RutinaDao {
    @Query("SELECT * FROM rutina AS R WHERE R.creadorId=:usuarioId")
    fun getAll(usuarioId:String): List<Rutina>

    @Insert
    fun insertAll(vararg rutina: Rutina)

    @Delete
    fun delete(rutina: Rutina)
}