package com.example.dailyrep

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.dataclases.Ejercicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
val ejerciciosPredeterminados=listOf<Ejercicio>(

)
class DailyRepApp: Application() {
    companion object{
        const val NOMBRE_FICHERO_SHARED_PREFERENCES:String="Datos basicos"
        const val KEY_MODO_OSCURO = "modo_oscuro_activado"
        const val NOMBRE_BASE_DE_DATOS="nombre_base_de_datos"
        const val DATOS_INTRODUCIDOS="datos_introducidos"
    }
    val context: Context =this
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ejercicioDao: EjercicioDao
    lateinit var rutinaDao: RutinaDao
    lateinit var usuarioDao: UsuarioDao
    lateinit var usuarioActualId:String
    lateinit var database: DataBase
    override fun onCreate(){
        super.onCreate()
        sharedPreferences = getSharedPreferences(NOMBRE_FICHERO_SHARED_PREFERENCES,MODE_PRIVATE)
        val modoOscuro=sharedPreferences.getBoolean(KEY_MODO_OSCURO, true)
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        database=Room.databaseBuilder(context, DataBase::class.java, NOMBRE_BASE_DE_DATOS).build()
        ejercicioDao=database.ejercicioDao()
        rutinaDao=database.rutinaDao()
        usuarioDao=database.usuarioDao()
        verificarEInsertarDatosIniciales()
    }
    private fun verificarEInsertarDatosIniciales() {
        val yaInicializada = sharedPreferences.getBoolean(DATOS_INTRODUCIDOS, false)

        if (!yaInicializada) {
            CoroutineScope(Dispatchers.IO).launch {
                ejercicioDao.insertAll(*ejerciciosPredeterminados.toTypedArray())
                sharedPreferences.edit().putBoolean(DATOS_INTRODUCIDOS, true).apply()
            }
        }
    }
}