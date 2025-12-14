package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.RutinaAdapter
import com.example.dailyrep.databinding.ActivityRutinasBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Rutina
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RutinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinasBinding
    private val rutinasAdapter: RutinaAdapter by lazy{ RutinaAdapter() }
    val context: Context = this
    private lateinit var myApp:DailyRepApp
    private lateinit var usuarioActualId:String

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
        cambiarApartados()
        binding.recyclerRutinas.layoutManager= LinearLayoutManager(context)
        binding.recyclerRutinas.adapter = rutinasAdapter
        myApp=applicationContext as DailyRepApp
        usuarioActualId=myApp.usuarioActualId
        ponerRutinas()
        crearRutina()
    }

    private fun crearRutina() {
        binding.agregarRutina.setOnClickListener {

        }
    }

    private fun ponerRutinas() {
        lifecycleScope.launch {
            val listaRutinas=myApp.rutinaDao.getAll()
            withContext(Dispatchers.IO){
                rutinasAdapter.ponerListaRutinas(listaRutinas)
            }
        }
    }

    private fun cambiarApartados() {
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent =Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }
        binding.apartadoEjercicios.setOnClickListener {
            val cambiarAEjerciciosIntent:Intent=Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
        binding.apartadoProgreso.setOnClickListener{
            val cambiarAProgresoIntent: Intent =Intent(context, ProgresoActivity::class.java)
            startActivity(cambiarAProgresoIntent)
        }
    }
}