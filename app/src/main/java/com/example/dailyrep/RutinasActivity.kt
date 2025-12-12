package com.example.dailyrep

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.RutinaAdapter
import com.example.dailyrep.databinding.ActivityRutinasBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Rutina

class RutinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinasBinding
    private val rutinasAdapter: RutinaAdapter by lazy{ RutinaAdapter() }
    val context: Context = this

    private val listaDePruebaRutinas = mutableListOf<Rutina>(
        Rutina("UPPER A"),
        Rutina("LOWER A"),
        Rutina("UPPER B"),
        Rutina("LOWER B"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRutinasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerRutinas.layoutManager= LinearLayoutManager(context)
        binding.recyclerRutinas.adapter = rutinasAdapter
        rutinasAdapter.ponerListaEjercicios(listaDePruebaRutinas)

    }
}