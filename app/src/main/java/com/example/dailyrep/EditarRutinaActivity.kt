package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.DailyRepApp.Companion.NOMBRE_FICHERO_SHARED_PREFERENCES
import com.example.dailyrep.RutinasActivity.Companion.ID_RUTINA
import com.example.dailyrep.RutinasActivity.Companion.ID_USUARIO
import com.example.dailyrep.RutinasActivity.Companion.NOMBRE_RUTINA
import com.example.dailyrep.RutinasActivity.Companion.ULTIMA_RUTINA_ID
import com.example.dailyrep.RutinasActivity.Companion.ULTIMA_RUTINA_NOMBRE
import com.example.dailyrep.adapters.EditarRutinaAdapter
import com.example.dailyrep.adapters.EjercicioEntrenamientoAdapter
import com.example.dailyrep.adapters.RutinaAdapter
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.databinding.ActivityEditarRutinaBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada
import com.example.dailyrep.dataclases.itemEntrenamiento
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditarRutinaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarRutinaBinding
    val context: Context =this
    private lateinit var usuarioActualId:String
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var myApp: DailyRepApp
    private var nombreRutina: String? = null
    private lateinit var rutinaDao: RutinaDao
    private lateinit var serieDao: SerieDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var ejercicioDao: EjercicioDao
    private lateinit var relacionEjeRutDao: RelacionEjeRutDao
    private var rutinaId: Long= 0
    private lateinit var auth: FirebaseAuth
    companion object{
        const val EN_ENTRENAMIENTO="rutina_en_entrenamiento"
    }

    private val editarRutinaAdapter: EditarRutinaAdapter by lazy {
        EditarRutinaAdapter(
            { itemEntr ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val idRelacion = itemEntr.relacion.id
                        val ultimo = serieDao.obtenerUltimoNumeroSerie(idRelacion) ?: 0
                        val siguienteNumeroSerie = ultimo + 1
                        val nuevaSerie = SeriePlanificada(
                            id = 0L,
                            relacionId = idRelacion,
                            numeroSerie = siguienteNumeroSerie,
                            repeticiones = 10,
                            peso = 50
                        )
                        serieDao.insert(nuevaSerie)
                        val seriesActualizadas = serieDao.obtenerSeries(idRelacion)
                        itemEntr.series.clear()
                        itemEntr.series.addAll(seriesActualizadas)
                    }
                    editarRutinaAdapter.notifyDataSetChanged()
                }
            }, { itemEntr ->
                itemEntr.series.lastOrNull()?.let { ultimaSerie ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            serieDao.eliminarSeriePorId(ultimaSerie.id)
                            itemEntr.series.remove(ultimaSerie)
                        }
                        editarRutinaAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditarRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth= Firebase.auth
        val currentUser=auth.currentUser
        if(currentUser!=null){
            usuarioActualId=currentUser.uid
        }else{
            val intentLogin:Intent=Intent(context, LoginActivity::class.java)
            startActivity(intentLogin)
            finish()
            return
        }
        myApp=applicationContext as DailyRepApp
        rutinaDao=myApp.rutinaDao
        ejercicioDao=myApp.ejercicioDao
        serieDao=myApp.serieDao
        usuarioDao=myApp.usuarioDao
        relacionEjeRutDao=myApp.relacionEjeRutDao
        sharedPreferences=getSharedPreferences(NOMBRE_FICHERO_SHARED_PREFERENCES,MODE_PRIVATE)
        usuarioActualId = intent.getStringExtra(ID_USUARIO).toString()
        nombreRutina = intent.getStringExtra(NOMBRE_RUTINA)
        rutinaId = intent.getLongExtra(ID_RUTINA, 0)

        binding.recyclerRutinas.layoutManager = LinearLayoutManager(context)
        binding.recyclerRutinas.adapter = editarRutinaAdapter

        binding.nombreRutina.setText(nombreRutina)
        cambiarApartados()
        ponerEjercicios()
    }

    private fun ponerEjercicios() {
        val listaItems:MutableList<itemEntrenamiento> = mutableListOf()
        lifecycleScope.launch {
            var listaEjercicios:List<Ejercicio> =listOf()
            withContext(Dispatchers.IO) {
                listaEjercicios=rutinaDao.obtenerEjerciciosRutina(rutinaId)
                listaEjercicios.forEach {
                    val ejercicioId=it.id
                    val idRelacion=relacionEjeRutDao.obtenerIdRelacion(ejercicioId,rutinaId)
                    val relacion=relacionEjeRutDao.obtenerRelacion(idRelacion)
                    val series=serieDao.obtenerSeries(idRelacion)
                    val itemEntrenamiento: itemEntrenamiento = itemEntrenamiento(relacion,it,series)
                    listaItems.add(itemEntrenamiento)
                }
            }
            editarRutinaAdapter.ponerListaEjercicios(listaItems)
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
