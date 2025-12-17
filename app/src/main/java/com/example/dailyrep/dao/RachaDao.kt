package com.example.dailyrep.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dailyrep.dataclases.DiasObjetivoUsuario

@Dao
interface RachaDao {
    @Insert
    fun insertarDiasObjetivo(dias: List<DiasObjetivoUsuario>)
    @Query("SELECT * FROM DiasObjetivoUsuario WHERE usuarioId=:usuarioId")
    fun consultarDiasObjetivo(usuarioId:String): List<DiasObjetivoUsuario>

    @Delete
    fun borrarDiaObjetivo(diasObjetivoUsuario: DiasObjetivoUsuario)

    @Insert
    fun insertarDiaObjetivo(diasObjetivoUsuario: DiasObjetivoUsuario)
}