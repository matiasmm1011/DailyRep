package com.example.dailyrep

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.CrearEjercicioActivity1.Companion.TIPO_EJERCICIO
import com.example.dailyrep.CrearEjercicioActivity2.Companion.PARTE_CUERPO
import com.example.dailyrep.EjerciciosActivity.Companion.ID_USUARIO
import com.example.dailyrep.EjerciciosActivity.Companion.NOMBRE_EJERCICIO
import com.example.dailyrep.databinding.ActivityCrearEjercicio1Binding

class CrearEjercicioActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEjercicio1Binding
    private lateinit var nombreEjercicio: String
    private lateinit var usuarioActualId: String
    private lateinit var tipoEjercicio: String
    private lateinit var parte_cuerpo:String

    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearEjercicio1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        usuarioActualId = intent.getStringExtra(ID_USUARIO) ?: ""
        nombreEjercicio = intent.getStringExtra(NOMBRE_EJERCICIO) ?: ""
        tipoEjercicio = intent.getStringExtra(TIPO_EJERCICIO) ?: ""
        parte_cuerpo=intent.getStringExtra(PARTE_CUERPO)?:""
    }
}