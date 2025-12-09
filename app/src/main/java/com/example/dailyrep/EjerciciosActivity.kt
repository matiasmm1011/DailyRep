package com.example.dailyrep

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.EjercicioAdapter
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.dailyrep.databinding.ActivityEjerciciosBinding
import com.example.dailyrep.databinding.MenuParteCuerpoBinding
import com.example.dailyrep.dataclases.Ejercicio

class EjerciciosActivity : AppCompatActivity() {
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
        val listaDePrueba = mutableListOf<Ejercicio>(
            // --- PECHO ---
            Ejercicio("Press de Banca Plano", "Barra", "Pecho", true),
            Ejercicio("Aperturas Inclinadas", "Mancuernas", "Pecho", false),
            Ejercicio("Flexiones (Push-ups)", "Peso Corporal", "Pecho", true),
            Ejercicio("Cruce de Poleas", "Polea", "Pecho", false),

            // --- ESPALDA ---
            Ejercicio("Dominadas", "Peso Corporal", "Espalda", true),
            Ejercicio("Remo con Barra T", "Barra", "Espalda", true),
            Ejercicio("Jalón al Pecho", "Polea", "Espalda", false),
            Ejercicio("Remo en Máquina Sentado", "Maquina", "Espalda", false),

            // --- PIERNA ---
            Ejercicio("Sentadilla Trasera", "Barra", "Pierna", true),
            Ejercicio("Prensa Inclinada", "Maquina", "Pierna", true),
            Ejercicio("Zancadas con Mancuernas", "Mancuernas", "Pierna", false),
            Ejercicio("Sentadilla Búlgara", "Peso Corporal", "Pierna", true),

            // --- HOMBRO ---
            Ejercicio("Press Militar", "Barra", "Hombro", true),
            Ejercicio("Elevaciones Laterales", "Mancuernas", "Hombro", true),
            Ejercicio("Pájaros en Polea", "Polea", "Hombro", false),

            // --- BRAZOS ---
            Ejercicio("Curl de Bíceps con Barra Z", "Barra", "Brazos", true),
            Ejercicio("Curl Martillo", "Mancuernas", "Brazos", false),
            Ejercicio("Extensiones de Tríceps", "Polea", "Brazos", true),
            Ejercicio("Fondos entre Bancos", "Peso Corporal", "Brazos", false),

            // --- ABDOMINALES ---
            Ejercicio("Plancha Abdominal", "Peso Corporal", "Abdominales", true),
            Ejercicio("Crunch en Polea Alta", "Polea", "Abdominales", true),
            Ejercicio("Encogimientos en Máquina", "Maquina", "Abdominales", false),

            // --- CARDIO (Ejemplo extra por si lo usas) ---
            Ejercicio("Cinta de Correr", "Cardio", "Pierna", false),
            Ejercicio("Remo en Ergonómetro", "Cardio", "Espalda", false)
        )
        ejercicioAdapter.ponerListaEjercicios(listaDePrueba)
        ponerFiltros(listaDePrueba)

    }
    fun ponerFiltros(listaCompleta: MutableList<Ejercicio>){
        var favoritos:Boolean=false;
        binding.botonFavoritos.setOnClickListener {
            favoritos=!favoritos
            if(favoritos){
                binding.imagenCorazon.setImageResource(R.drawable.heart)
                val listaFavoritos=mutableListOf<Ejercicio>()
                listaCompleta.forEach {
                    if(it.favorito){
                        listaFavoritos.add(it)
                    }
                }
                ejercicioAdapter.ponerListaEjercicios(listaFavoritos)
            }else{
                binding.imagenCorazon.setImageResource(R.drawable.corazon_vacio)
                ejercicioAdapter.ponerListaEjercicios(listaCompleta)
            }
        }
        binding.botonParteCuerpo.setOnClickListener{
            mostrarMenu(listaCompleta,binding.botonParteCuerpo)
        }
    }
    fun mostrarMenu(listaCompleta: MutableList<Ejercicio>,ancla: View){
        val bindingMenu= MenuParteCuerpoBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            bindingMenu.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindingMenu.botonFiltrarBrazos.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Brazos")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarEspalda.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Espalda")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPiernas.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Pierna")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarAmdominales.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Abdominales")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarHombros.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Hombro")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPecho.setOnClickListener {
            ponerListaNuevaParteCuerpo(listaCompleta,"Pecho")
            popupWindow.dismiss()
        }
        popupWindow.showAsDropDown(ancla, 0, 0)
    }
    fun ponerListaNuevaParteCuerpo(listaAnterior:MutableList<Ejercicio>, categoria:String){
        val nuevaLista=mutableListOf<Ejercicio>()
        listaAnterior.forEach {
            if(it.parteCuerpo==categoria){
                nuevaLista.add(it)
            }
        }
        ejercicioAdapter.ponerListaEjercicios(nuevaLista)
    }
}