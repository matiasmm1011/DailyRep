package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.chip.Chip
import androidx.core.graphics.drawable.toDrawable
import java.util.Locale

class EjerciciosActivity : AppCompatActivity() {
   private lateinit var binding: ActivityEjerciciosBinding
    private val listaDePrueba = mutableListOf<Ejercicio>(
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
   private val ejercicioAdapter: EjercicioAdapter by lazy{ EjercicioAdapter() }
    val context: Context =this
    val filtrosParteCuerpo=mutableSetOf<String>()
    val filtrosTipoEjercicio=mutableSetOf<String>()
    var favorito=false
    var textoEnBuscador:String?=null
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
        ejercicioAdapter.ponerListaEjercicios(listaDePrueba)
        ponerFiltros(listaDePrueba)

        val intentCambioRutinas: Intent = Intent(context, RutinasActivity::class.java)
        binding.apartadoRutinas.setOnClickListener {
            startActivity(intentCambioRutinas)
        }
        buscador()
        cambiarApartados()
    }

    private fun cambiarApartados() {
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent =Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }
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
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        bindingMenu.botonFiltrarBrazos.setOnClickListener {
            filtrosParteCuerpo.add("Brazos")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarEspalda.setOnClickListener {
            filtrosParteCuerpo.add("Espalda")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarPiernas.setOnClickListener {
            filtrosParteCuerpo.add("Pierna")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarAmdominales.setOnClickListener {
            filtrosParteCuerpo.add("Abdominales")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarHombros.setOnClickListener {
            filtrosParteCuerpo.add("Hombro")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarPecho.setOnClickListener {
            filtrosParteCuerpo.add("Pecho")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
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
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        bindingMenu.botonFiltrarBarra.setOnClickListener {
            filtrosTipoEjercicio.add("Barra")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarMancuernas.setOnClickListener {
            filtrosTipoEjercicio.add("Mancuernas")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }

        }
        bindingMenu.botonFiltrarPolea.setOnClickListener {
            filtrosTipoEjercicio.add("Polea")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }
        }
        bindingMenu.botonFiltrarCardio.setOnClickListener {
            filtrosTipoEjercicio.add("Cardio")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }

        }
        bindingMenu.botonFiltrarMaquina.setOnClickListener {
            filtrosTipoEjercicio.add("Maquina")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }

        }
        bindingMenu.botonFiltrarPesoCorporal.setOnClickListener {
            filtrosTipoEjercicio.add("Peso Corporal")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista(listaDePrueba)
            }

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
        }
        binding.botonTipoEjercicio.setOnClickListener{
            mostrarMenuTipoEjercicio(binding.botonTipoEjercicio)
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
        if(textoEnBuscador!=null){
            val texto=textoEnBuscador.toString().lowercase()
            resultado=resultado.filter{
                ejercicio->  ejercicio.nombre.contains(texto)
            }
        }
        dibujarEtiquetas()
        ejercicioAdapter.ponerListaEjercicios(resultado)

    }
    fun dibujarEtiquetas(){
        val controladorChips=binding.grupoChips
        controladorChips.removeAllViews()
        filtrosParteCuerpo.forEach {filtro->
            val chip: Chip =crearChipDeFiltro(filtro,true)
            controladorChips.addView(chip)
        }
        filtrosTipoEjercicio.forEach { filtro->
            val chip: Chip =crearChipDeFiltro(filtro,false)
            controladorChips.addView(chip)
        }
        val vistaEtiquetas=filtrosParteCuerpo.isNotEmpty() || filtrosTipoEjercicio.isNotEmpty()
        if(vistaEtiquetas){
            binding.chips.visibility=View.VISIBLE
        }else{
            binding.chips.visibility=View.GONE
        }

    }
    private fun crearChipDeFiltro(texto: String, esFiltroParteCuerpo:Boolean): Chip {
        val chip = Chip(context)
        chip.text = texto
        chip.isCloseIconVisible = true
        chip.setChipBackgroundColorResource(R.color.cuadros)
        chip.setTextColor(getColor(R.color.textos))
        chip.setCloseIconTintResource(R.color.naranja)
        chip.setOnCloseIconClickListener {
            if(esFiltroParteCuerpo){
            filtrosParteCuerpo.remove(texto)
            }else{
                filtrosTipoEjercicio.remove(texto)

            }
            actualizarLista(listaDePrueba)
        }
        return chip
    }
    fun buscador(){
        binding.buscador.setOnEditorActionListener {
            vista, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val textoBuscado = binding.buscador.text.toString()
                textoEnBuscador=textoBuscado
                actualizarLista(listaDePrueba)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(vista.windowToken, 0)
                vista.clearFocus()
                true
            }else{
            false
            }
        }
        binding.quitarTextoBuscador.setOnClickListener {
            binding.buscador.text=null
            textoEnBuscador=null
            actualizarLista(listaDePrueba)
        }
    }
}