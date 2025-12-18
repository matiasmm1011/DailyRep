package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.CrearEjercicioActivity1.Companion.TIPO_EJERCICIO
import com.example.dailyrep.CrearEjercicioActivity2.Companion.PARTE_CUERPO
import com.example.dailyrep.EjerciciosActivity.Companion.ID_USUARIO
import com.example.dailyrep.EjerciciosActivity.Companion.NOMBRE_EJERCICIO
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.databinding.ActivityCrearEjercicio1Binding
import com.example.dailyrep.databinding.ActivityCrearEjercicio3Binding
import com.example.dailyrep.dataclases.Ejercicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearEjercicioActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEjercicio3Binding
    private lateinit var nombreEjercicio: String
    private lateinit var usuarioActualId: String
    private lateinit var tipoEjercicio: String
    private lateinit var parte_cuerpo:String

    private lateinit var ejercicioDao: EjercicioDao
    private lateinit var myApp: DailyRepApp

    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearEjercicio3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        myApp=applicationContext as DailyRepApp
        usuarioActualId = intent.getStringExtra(ID_USUARIO) ?: ""
        nombreEjercicio = intent.getStringExtra(NOMBRE_EJERCICIO) ?: ""
        tipoEjercicio = intent.getStringExtra(TIPO_EJERCICIO) ?: ""
        parte_cuerpo=intent.getStringExtra(PARTE_CUERPO)?:""
        ejercicioDao=myApp.ejercicioDao
        binding.crearEjercicio.setOnClickListener {
            val descripcion=binding.descripcion.text.toString()
            if(descripcion!=""){
                lifecycleScope.launch{
                    withContext(Dispatchers.IO){
                        val esCardio=if(parte_cuerpo=="Cardio"){true}else{false}
                        val ejercicioNuevo= Ejercicio(id=0L ,nombreEjercicio,tipoEjercicio, parte_cuerpo,descripcion,false,usuarioActualId,null,esCardio)
                        ejercicioDao.insert(ejercicioNuevo)
                    }
                    val intentEjercicios:Intent= Intent(context, EjerciciosActivity::class.java)
                    startActivity(intentEjercicios)
                }
            }else{
                Toast.makeText(
                    baseContext,"Introduce una descripcion",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}