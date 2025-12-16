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
import com.example.dailyrep.RutinasActivity.Companion.ID_RUTINA
import com.example.dailyrep.RutinasActivity.Companion.ID_USUARIO
import com.example.dailyrep.RutinasActivity.Companion.NOMBRE_RUTINA
import com.example.dailyrep.adapters.EjercicioEntrenamientoAdapter
import com.example.dailyrep.dao.EjercicioDao
import com.example.dailyrep.dao.RelacionEjeRutDao
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.databinding.ActivityEntrenamientoBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.itemEntrenamiento
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntrenamientoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntrenamientoBinding
    val context: Context =this
    private lateinit var usuarioActualId:String
    private lateinit var myApp: DailyRepApp
    private var nombreRutina: String? = null
    private lateinit var rutinaDao: RutinaDao
    private lateinit var serieDao: SerieDao
    private lateinit var ejercicioDao: EjercicioDao
    private lateinit var relacionEjeRutDao: RelacionEjeRutDao
    private var rutinaId: Long= 0
    private lateinit var auth: FirebaseAuth
    private val ejercicioEntrenamientoAdapter: EjercicioEntrenamientoAdapter by lazy{ EjercicioEntrenamientoAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityEntrenamientoBinding.inflate(layoutInflater)
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
        relacionEjeRutDao=myApp.relacionEjeRutDao
        usuarioActualId = intent.getStringExtra(ID_USUARIO).toString()
        nombreRutina = intent.getStringExtra(NOMBRE_RUTINA)
        rutinaId = intent.getLongExtra(ID_RUTINA, 0)
        binding.recyclerEjerciciosEntrenamiento.layoutManager = LinearLayoutManager(context)
        binding.recyclerEjerciciosEntrenamiento.adapter = ejercicioEntrenamientoAdapter
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
            ejercicioEntrenamientoAdapter.ponerListaEjercicios(listaItems)
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