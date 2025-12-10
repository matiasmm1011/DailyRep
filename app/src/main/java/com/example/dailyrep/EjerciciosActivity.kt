package com.example.dailyrep

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.EjercicioAdapter
import android.widget.PopupWindow
import com.example.dailyrep.databinding.ActivityEjerciciosBinding
import com.example.dailyrep.databinding.MenuParteCuerpoBinding
import com.example.dailyrep.databinding.MenuTipoEjercicioBinding
import com.example.dailyrep.dataclases.Ejercicio

class EjerciciosActivity : AppCompatActivity() {
   private lateinit var binding: ActivityEjerciciosBinding
   private val ejercicioAdapter: EjercicioAdapter by lazy{ EjercicioAdapter() }
    val context: Context =this
    val filtrosParteCuerpo=mutableSetOf<String>()
    val filtrosTipoEjercicio=mutableSetOf<String>()
    var favorito=false
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

    fun mostrarMenuParteCuerpo(ancla: View){
        val bindingMenu= MenuParteCuerpoBinding.inflate(layoutInflater)
        val ancho = dpToPx(250)
        val altura = dpToPx(325)
        val popupWindow = PopupWindow(
            bindingMenu.root,
            ancho,
            altura,
            true
        )
        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindingMenu.botonFiltrarBrazos.setOnClickListener {
            filtrosParteCuerpo.add("Brazos")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarEspalda.setOnClickListener {
            filtrosParteCuerpo.add("Espalda")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPiernas.setOnClickListener {
            filtrosParteCuerpo.add("Pierna")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarAmdominales.setOnClickListener {
            filtrosParteCuerpo.add("Abdominales")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarHombros.setOnClickListener {
            filtrosParteCuerpo.add("Hombro")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPecho.setOnClickListener {
            filtrosParteCuerpo.add("Pecho")
            popupWindow.dismiss()
        }
        popupWindow.showAsDropDown(ancla, 0, 0)
    }
    fun mostrarMenuTipoEjercicio(ancla: View){
        val bindingMenu= MenuTipoEjercicioBinding.inflate(layoutInflater)
        val ancho = dpToPx(250)
        val altura = dpToPx(325)
        val popupWindow = PopupWindow(
            bindingMenu.root,
            ancho,
            altura,
            true
        )
        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindingMenu.botonFiltrarBarra.setOnClickListener {
            filtrosTipoEjercicio.add("Barra")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarMancuernas.setOnClickListener {
            filtrosTipoEjercicio.add("Mancuernas")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPolea.setOnClickListener {
            filtrosTipoEjercicio.add("Polea")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarCardio.setOnClickListener {
            filtrosTipoEjercicio.add("Cardio")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarMaquina.setOnClickListener {
            filtrosTipoEjercicio.add("Maquina")
            popupWindow.dismiss()
        }
        bindingMenu.botonFiltrarPesoCorporal.setOnClickListener {
            filtrosTipoEjercicio.add("Peso Corporal")
            popupWindow.dismiss()
        }
        val xoff = ancla.width - ancho
        popupWindow.showAsDropDown(ancla, xoff, 0)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    fun ponerFiltros(listaCompleta: MutableList<Ejercicio>){
        binding.botonFavoritos.setOnClickListener {
            favorito=!favorito
            if(favorito){
                binding.imagenCorazon.setImageResource(R.drawable.heart)
            }else{
                binding.imagenCorazon.setImageResource(R.drawable.corazon_vacio)
            }
            actualizarLista(listaCompleta)
        }
        binding.botonParteCuerpo.setOnClickListener {
            mostrarMenuParteCuerpo(binding.botonParteCuerpo)
            actualizarLista(listaCompleta)
        }
        binding.botonTipoEjercicio.setOnClickListener{
            mostrarMenuTipoEjercicio(binding.botonTipoEjercicio)
            actualizarLista(listaCompleta)
        }
    }


    fun actualizarLista(listaCompleta: MutableList<Ejercicio>) {
        var resultado: List<Ejercicio> = listaCompleta

        if (favorito) {
            resultado = resultado.filter { ejercicio -> ejercicio.favorito }
        }
        if (filtrosTipoEjercicio.isNotEmpty()) {
            resultado = resultado.filter { ejercicio ->
                filtrosTipoEjercicio.contains(ejercicio.tipo)
            }
        }

        if (filtrosParteCuerpo.isNotEmpty()) {
            resultado = resultado.filter { ejercicio ->
                filtrosParteCuerpo.contains(ejercicio.parteCuerpo)
            }
        }

        ejercicioAdapter.ponerListaEjercicios(resultado)
        // TODO poner chips para actualizar sets
    }
}