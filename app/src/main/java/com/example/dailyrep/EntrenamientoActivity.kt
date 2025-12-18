package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.DailyRepApp.Companion.NOMBRE_FICHERO_SHARED_PREFERENCES
import com.example.dailyrep.RutinasActivity.Companion.ID_RUTINA
import com.example.dailyrep.RutinasActivity.Companion.ID_USUARIO
import com.example.dailyrep.RutinasActivity.Companion.NOMBRE_RUTINA
import com.example.dailyrep.adapters.EjercicioEntrenamientoAdapter
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.HistorialDao
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.databinding.ActivityEntrenamientoBinding
import com.example.dailyrep.databinding.MenuEditarRutinaBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.HistorialEntrenamiento
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.itemEntrenamiento
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class EntrenamientoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntrenamientoBinding
    val context: Context = this
    private lateinit var usuarioActualId: String
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var myApp: DailyRepApp
    private var nombreRutina: String? = null
    private lateinit var rutinaDao: RutinaDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var ejercicioDao: EjercicioDao
    private lateinit var relacionEjeRutDao: RelacionEjeRutDao
    private lateinit var historialDao: HistorialDao
    private lateinit var serieDao: SerieDao
    private var rutinaId: Long = 0
    private lateinit var auth: FirebaseAuth

    companion object {
        const val EN_ENTRENAMIENTO = "rutina_en_entrenamiento"
    }

    private val ejercicioEntrenamientoAdapter: EjercicioEntrenamientoAdapter by lazy {
        EjercicioEntrenamientoAdapter(
            { view, item ->
                mostrarPPtresPuntos(view, item)
            }, { serie, peso, reps ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val completado = serie.completado
                        serie.peso = peso
                        serie.repeticiones = reps
                        serie.completado = !completado
                        serieDao.actualizarSerie(serie)
                    }
                    ejercicioEntrenamientoAdapter.notifyDataSetChanged()
                }
            },{itemEntr, notaNueva ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val relacionEjeRut = itemEntr.relacion
                        relacionEjeRut.notas = notaNueva
                        relacionEjeRutDao.actualizarNotas(relacionEjeRut)
                    }
                    ponerEjercicios()
                }
            },{itemEntr->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val relacionEjeRut = itemEntr.relacion
                        relacionEjeRut.notas = null
                        relacionEjeRutDao.actualizarNotas(relacionEjeRut)
                    }
                    ponerEjercicios()
                }
            },{

            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEntrenamientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            usuarioActualId = currentUser.uid
        } else {
            val intentLogin: Intent = Intent(context, LoginActivity::class.java)
            startActivity(intentLogin)
            finish()
            return
        }
        myApp = applicationContext as DailyRepApp
        rutinaDao = myApp.rutinaDao
        ejercicioDao = myApp.ejercicioDao
        serieDao = myApp.serieDao
        usuarioDao = myApp.usuarioDao
        historialDao = myApp.historialDao
        relacionEjeRutDao = myApp.relacionEjeRutDao
        sharedPreferences = getSharedPreferences(NOMBRE_FICHERO_SHARED_PREFERENCES, MODE_PRIVATE)
        usuarioActualId = intent.getStringExtra(ID_USUARIO).toString()
        nombreRutina = intent.getStringExtra(NOMBRE_RUTINA)
        rutinaId = intent.getLongExtra(ID_RUTINA, 0)
        binding.recyclerEjerciciosEntrenamiento.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjerciciosEntrenamiento.adapter = ejercicioEntrenamientoAdapter
        binding.nombreRutina.setText(nombreRutina)
        cambiarApartados()
        ponerEjercicios()
        terminarEntrenamiento()
    }

    private fun aumentarDiasCompletados() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuario = usuarioDao.getUsuarioPorId(usuarioActualId)
            if (usuario != null) {
                usuario.diasEntrenados++
                usuarioDao.updateUsuario(usuario)
            }

        }
    }

    private fun terminarEntrenamiento() {
        binding.finalizarSesion.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val usuario = usuarioDao.getUsuarioPorId(usuarioActualId)

                    if (usuario != null) {
                        val hoy = System.currentTimeMillis()
                        val ultimoDiaEntrenado = usuario.ultimoDiaEntrenamientoFecha ?: 0
                        if (!esMismoDia(ultimoDiaEntrenado, hoy)) {
                            usuario.rachaActual += 1
                            usuario.diasEntrenados += 1
                            usuario.ultimoDiaEntrenamientoFecha = hoy

                            usuarioDao.updateUsuario(usuario)
                        }
                    }
                    val historial = HistorialEntrenamiento(
                        usuarioId = usuarioActualId,
                        fecha = System.currentTimeMillis()
                    )
                    historialDao.registrarEntrenamiento(historial)
                    val ejercicios = rutinaDao.obtenerEjerciciosRutina(rutinaId)
                    ejercicios.forEach {
                        val relacionId = relacionEjeRutDao.obtenerIdRelacion(it.id, rutinaId)
                        val series = serieDao.obtenerSeries(relacionId)
                        series.forEach {
                            it.completado = false
                            serieDao.actualizarSerie(it)
                        }
                    }
                }
                sharedPreferences.edit().putBoolean(EN_ENTRENAMIENTO, false).apply()
                aumentarDiasCompletados()
                val intentVolverARutinas = Intent(context, RutinasActivity::class.java)
                startActivity(intentVolverARutinas)
            }
        }
    }

    private fun esMismoDia(dia1: Long, dia2: Long): Boolean {
        val calendario1 = Calendar.getInstance().apply { timeInMillis = dia1 }
        val calendario2 = Calendar.getInstance().apply { timeInMillis = dia2 }
        return calendario1.get(Calendar.DAY_OF_YEAR) == calendario2.get(Calendar.DAY_OF_YEAR) &&
                calendario1.get(Calendar.YEAR) == calendario2.get(Calendar.YEAR)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun mostrarPPtresPuntos(ancla: View, item: itemEntrenamiento) {
        val bindingMenu = MenuEditarRutinaBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            bindingMenu.root,
            dpToPx(250),
            dpToPx(325),
            true
        )
        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        val xoff = ancla.width - popupWindow.width
        popupWindow.showAsDropDown(ancla, xoff, 0)
        bindingMenu.botonAgregarSeriePP.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val idRelacion = item.relacion.id
                    val ultimo = serieDao.obtenerUltimoNumeroSerie(idRelacion) ?: 0
                    val siguienteNumeroSerie = ultimo + 1
                    val nuevaSerie = SeriePlanificada(
                        id = 0L,
                        relacionId = idRelacion,
                        numeroSerie = siguienteNumeroSerie,
                        repeticiones = 10,
                        peso = 50, false
                    )
                    serieDao.insert(nuevaSerie)
                    val seriesActualizadas = serieDao.obtenerSeries(idRelacion)
                    item.series.clear()
                    item.series.addAll(seriesActualizadas)
                }
                ejercicioEntrenamientoAdapter.notifyDataSetChanged()
                popupWindow.dismiss()
            }
        }
        bindingMenu.botonQuitarSeriePP.setOnClickListener {
            item.series.lastOrNull()?.let { ultimaSerie ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        serieDao.eliminarSeriePorId(ultimaSerie.id)
                        item.series.remove(ultimaSerie)
                    }
                    ponerEjercicios()
                }
            }
            popupWindow.dismiss()
        }
        bindingMenu.botonEliminarEjercicioPP.setOnClickListener {
            val relacion=item.relacion
            lifecycleScope.launch{
                withContext(Dispatchers.IO){
                    relacionEjeRutDao.delete(relacion)
                }
                ponerEjercicios()
                ejercicioEntrenamientoAdapter.notifyDataSetChanged()
            }
            popupWindow.dismiss()
        }
        bindingMenu.botonAgregarEjercicioPP.setOnClickListener {
            val intentCambioACreadorR: Intent =
                Intent(context, CrearRutinaActivity::class.java)
            intentCambioACreadorR.apply {
                intentCambioACreadorR.putExtra(ID_RUTINA, rutinaId)
            }
            startActivity(intentCambioACreadorR)
            popupWindow.dismiss()
        }
        bindingMenu.botonAgregarNotasPP.setOnClickListener {
            if (item.relacion.notas == null) {
                item.relacion.notas = ""
            }
            ejercicioEntrenamientoAdapter.notifyDataSetChanged()
            popupWindow.dismiss()
        }
    }


    private fun ponerEjercicios() {
        val listaItems: MutableList<itemEntrenamiento> = mutableListOf()
        lifecycleScope.launch {
            var listaEjercicios: List<Ejercicio> = listOf()
            withContext(Dispatchers.IO) {
                listaEjercicios = rutinaDao.obtenerEjerciciosRutina(rutinaId)
                listaEjercicios.forEach {
                    val ejercicioId = it.id
                    val idRelacion = relacionEjeRutDao.obtenerIdRelacion(ejercicioId, rutinaId)
                    val relacion = relacionEjeRutDao.obtenerRelacion(idRelacion)
                    val series = serieDao.obtenerSeries(idRelacion)
                    val itemEntrenamiento: itemEntrenamiento =
                        itemEntrenamiento(relacion, it, series)
                    listaItems.add(itemEntrenamiento)
                }
            }
            ejercicioEntrenamientoAdapter.ponerListaEjercicios(listaItems)
        }
    }

    private fun cambiarApartados() {
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent = Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }
        binding.apartadoEjercicios.setOnClickListener {
            val cambiarAEjerciciosIntent: Intent = Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
        binding.apartadoProgreso.setOnClickListener {
            val cambiarAProgresoIntent: Intent = Intent(context, ProgresoActivity::class.java)
            startActivity(cambiarAProgresoIntent)
        }
    }
}