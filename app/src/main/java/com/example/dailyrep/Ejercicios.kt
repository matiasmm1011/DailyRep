package com.example.dailyrep

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.EjercicioAdapter
import com.example.dailyrep.databinding.ActivityEjerciciosBinding
import com.example.dailyrep.dataclases.Ejercicio

class Ejercicios : AppCompatActivity() {
   private lateinit var binding: ActivityEjerciciosBinding
   private val ejercicioAdapter: EjercicioAdapter by lazy{ EjercicioAdapter() }
    val context: Context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityEjerciciosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.recyclerEjercicios.layoutManager= LinearLayoutManager(context)
        binding.recyclerEjercicios.adapter=ejercicioAdapter
        val listaDePrueba = listOf<Ejercicio>(
            Ejercicio("Press de Banca Plano", "Barra", "Pecho", true),
            Ejercicio("Sentadilla Trasera", "Barra", "Piernas", true),
            Ejercicio("Dominadas", "Peso Corporal", "Espalda", true),
            Ejercicio("Flexiones (Push-ups)", "Peso Corporal", "Pecho", false),
            Ejercicio("Press Militar", "Barra", "Hombros", true),
            Ejercicio("Remo con Mancuerna", "Mancuerna", "Espalda", false),
            Ejercicio("Curl de Bíceps con Barra Z", "Barra", "Bíceps", false),
            Ejercicio("Extensiones de Tríceps en Polea", "Máquina", "Tríceps", false),
            Ejercicio("Prensa de Piernas", "Máquina", "Piernas", false),
            Ejercicio("Elevaciones Laterales", "Mancuernas", "Hombros", true),
            Ejercicio("Peso Muerto Rumano", "Barra", "Isquiotibiales", true),
            Ejercicio("Zancadas (Lunges)", "Mancuernas", "Piernas", false),
            Ejercicio("Jalón al Pecho", "Máquina", "Espalda", false),
            Ejercicio("Fondos en Paralelas", "Peso Corporal", "Tríceps", true),
            Ejercicio("Plancha Abdominal", "Peso Corporal", "Abdomen", true)
        )
        ejercicioAdapter.ponerListaEjercicios(listaDePrueba)
    }
}