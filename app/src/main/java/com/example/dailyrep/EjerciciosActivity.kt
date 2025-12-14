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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class EjerciciosActivity : AppCompatActivity() {
   private lateinit var binding: ActivityEjerciciosBinding
   private lateinit var listaEjercicios:List<Ejercicio>
   private val usuarioActualId = "usuario_prueba_1"
    private lateinit var myApp: DailyRepApp
   private val ejercicioAdapter: EjercicioAdapter by lazy{ EjercicioAdapter(){ejercicioClickeado ->
       val intent = Intent(this, DescripcionEjercicioActivity::class.java)
       //TODO pasarle los datos del ejercicio que mostrara
       startActivity(intent)
   }
   }
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
        myApp=applicationContext as DailyRepApp
        actualizarLista()
        ponerFiltros()
        buscador()
        cambiarApartados()
    }

    private fun cambiarApartados() {
        val intentCambioRutinas: Intent = Intent(context, RutinasActivity::class.java)
        binding.apartadoRutinas.setOnClickListener {
            startActivity(intentCambioRutinas)
        }
        val intentCambioProgreso: Intent = Intent(context, ProgresoActivity::class.java)
        binding.apartadoProgreso.setOnClickListener {
            startActivity(intentCambioProgreso)
        }
        val cambiarAPerfilIntent: Intent =Intent(context, PerfilActivity::class.java)
        binding.apartadoPerfil.setOnClickListener{
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
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarEspalda.setOnClickListener {
            filtrosParteCuerpo.add("Espalda")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarPiernas.setOnClickListener {
            filtrosParteCuerpo.add("Pierna")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarAmdominales.setOnClickListener {
            filtrosParteCuerpo.add("Abdominales")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarHombros.setOnClickListener {
            filtrosParteCuerpo.add("Hombro")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarPecho.setOnClickListener {
            filtrosParteCuerpo.add("Pecho")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
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
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarMancuernas.setOnClickListener {
            filtrosTipoEjercicio.add("Mancuernas")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }

        }
        bindingMenu.botonFiltrarPolea.setOnClickListener {
            filtrosTipoEjercicio.add("Polea")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }
        }
        bindingMenu.botonFiltrarCardio.setOnClickListener {
            filtrosTipoEjercicio.add("Cardio")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }

        }
        bindingMenu.botonFiltrarMaquina.setOnClickListener {
            filtrosTipoEjercicio.add("Maquina")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }

        }
        bindingMenu.botonFiltrarPesoCorporal.setOnClickListener {
            filtrosTipoEjercicio.add("Peso Corporal")
            popupWindow.dismiss()
            ancla.post {
                actualizarLista()
            }

        }
        val xoff = ancla.width - ancho
        popupWindow.showAsDropDown(ancla, xoff, 0)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    fun ponerFiltros(){
        binding.botonFavoritos.setOnClickListener {
            favorito=!favorito
            if(favorito){
                binding.imagenCorazon.setImageResource(R.drawable.heart)
            }else{
                binding.imagenCorazon.setImageResource(R.drawable.corazon_vacio)
            }
            actualizarLista()
        }
        binding.botonParteCuerpo.setOnClickListener {
            mostrarMenuParteCuerpo(binding.botonParteCuerpo)
        }
        binding.botonTipoEjercicio.setOnClickListener{
            mostrarMenuTipoEjercicio(binding.botonTipoEjercicio)
        }
    }


    fun actualizarLista() {
        dibujarEtiquetas()
        lifecycleScope.launch {
            val (listaNueva, setIdsFavoritos) = withContext(Dispatchers.IO) {
                val ejercicios = if (favorito) {
                    myApp.ejercicioDao.getEjerciciosFavoritosConBusqueda(usuarioActualId, textoEnBuscador)
                } else {
                    myApp.ejercicioDao.getEjerciciosGeneralesConBusqueda(usuarioActualId, textoEnBuscador)
                }
                val listaIds = myApp.ejercicioDao.obtenerIdsFavoritos(usuarioActualId)
                Pair(ejercicios, listaIds.toSet())
            }
            listaNueva.forEach { ejercicio ->
                ejercicio.esFavorito = setIdsFavoritos.contains(ejercicio.id)
            }
            var listaFinalFiltrada = listaNueva

            if (filtrosTipoEjercicio.isNotEmpty()) {
                listaFinalFiltrada = listaFinalFiltrada.filter { ejercicio ->
                    filtrosTipoEjercicio.contains(ejercicio.tipo)
                }
            }

            if (filtrosParteCuerpo.isNotEmpty()) {
                listaFinalFiltrada = listaFinalFiltrada.filter { ejercicio ->
                    filtrosParteCuerpo.contains(ejercicio.parteCuerpo)
                }
            }
            ejercicioAdapter.ponerListaEjercicios(listaFinalFiltrada)
        }
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
            actualizarLista()
        }
        return chip
    }
    fun buscador(){
        binding.buscador.setOnEditorActionListener {
            vista, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val textoBuscado = binding.buscador.text.toString()
                textoEnBuscador=textoBuscado
                actualizarLista()
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
            actualizarLista()
        }
    }
}