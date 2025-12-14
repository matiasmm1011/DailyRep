package com.example.dailyrep

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.EjercicioFavorito
import com.example.dailyrep.dataclases.RelacionEjeRut
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.Usuario

@Database(entities = arrayOf(Ejercicio::class, EjercicioFavorito::class, RelacionEjeRut::class,
    Rutina::class, SeriePlanificada::class, Usuario::class),version = 1)
abstract class DataBase: RoomDatabase(){
    abstract fun EjercicioDao(): EjercicioDao
    abstract fun RutinaDao(): RutinaDao
    abstract fun UsuarioDao(): UsuarioDao
}