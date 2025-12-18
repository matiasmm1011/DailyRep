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
import android.widget.PopupWindow
import com.example.dailyrep.databinding.MenuParteCuerpoBinding
import com.example.dailyrep.databinding.MenuTipoEjercicioBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.google.android.material.chip.Chip
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.RutinasActivity.Companion.ID_RUTINA
import com.example.dailyrep.adapters.EjercicioCreadorRutinaAdapter
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.databinding.ActivityCrearRutinaBinding
import com.example.dailyrep.dataclases.RelacionEjeRut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearRutinaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearRutinaBinding
    private val usuarioActualId = "usuario_prueba_1"
    private var rutinaId: Long = 0

    private lateinit var relacionEjeRutDao: RelacionEjeRutDao
    private lateinit var rutinaDao: RutinaDao

    var ejerciciosNuevosSeleccionados = mutableListOf<Ejercicio>()
    var ejerciciosSeleccionadosAnteriormente=mutableSetOf<Ejercicio>()

    private lateinit var myApp: DailyRepApp
    private val ejercicioCreadorRutinaAdapter: EjercicioCreadorRutinaAdapter by lazy{ EjercicioCreadorRutinaAdapter(){
            ejercicio, seleccionado ->
        if (seleccionado) {
            ejercicio.seleccionado = true
            ejerciciosNuevosSeleccionados.add(ejercicio)
        } else {
            ejercicio.seleccionado = false
            ejerciciosNuevosSeleccionados.remove(ejercicio)
        }
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

        binding= ActivityCrearRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rutinaId = intent.getLongExtra(ID_RUTINA, 0)
        binding.recyclerEjerciciosCR.layoutManager= LinearLayoutManager(context)
        binding.recyclerEjerciciosCR.adapter=ejercicioCreadorRutinaAdapter
        myApp=applicationContext as DailyRepApp
        relacionEjeRutDao=myApp.relacionEjeRutDao
        rutinaDao=myApp.rutinaDao
        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                ejerciciosSeleccionadosAnteriormente=rutinaDao.obtenerEjerciciosRutina(rutinaId).toMutableSet()

            }
            actualizarLista()
        }

        ponerFiltros()
        buscador()
        crearRutina()
    }

    fun crearRutina(){
        binding.botonCrearRutina.setOnClickListener {
            val listaEjeRut: MutableList<RelacionEjeRut> = mutableListOf()
            ejerciciosNuevosSeleccionados.forEach { ejercicio ->
                val ejercicioId = ejercicio.id
                val relacionRutinaNueva = RelacionEjeRut(0L,rutinaId,ejercicioId,null)
                listaEjeRut.add(relacionRutinaNueva)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO){
                    relacionEjeRutDao.insertAll(*listaEjeRut.toTypedArray())
                }
            }
            val intentCambioRutinas: Intent = Intent(context, RutinasActivity::class.java)
            startActivity(intentCambioRutinas)
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
            val idsEjsSelec = ejerciciosSeleccionadosAnteriormente.map { it.id }.toSet()
            val listaFiltrada = listaNueva.filter { !idsEjsSelec.contains(it.id) }
            var listaFinalFiltrada = listaFiltrada

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
            ejercicioCreadorRutinaAdapter.ponerListaEjercicios(listaFinalFiltrada)
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