package com.example.dailyrep

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class DailyRepApp: Application() {
    companion object{
        const val NOMBRE_FICHERO_SHARED_PREFERENCES:String="Datos basicos"
        const val KEY_MODO_OSCURO = "modo_oscuro_activado"
    }
        val context: Context =this
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(){
        super.onCreate()
        val sharedPreferences = getSharedPreferences(NOMBRE_FICHERO_SHARED_PREFERENCES,MODE_PRIVATE)
        val modoOscuro=sharedPreferences.getBoolean(KEY_MODO_OSCURO, true)
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}