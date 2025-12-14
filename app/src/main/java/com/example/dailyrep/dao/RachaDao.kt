package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.dailyrep.dataclases.DiasObjetivoUsuario

@Dao
interface RachaDao {
    @Insert
    fun insertarDiasObjetivo(dias: List<DiasObjetivoUsuario>)
}