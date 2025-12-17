package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.dao.RachaDao
import com.example.dailyrep.dao.SerieDao
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.databinding.ActivityProgresoBinding
import com.example.dailyrep.dataclases.DiasObjetivoUsuario
import com.example.dailyrep.dataclases.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgresoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgresoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var rachaDao: RachaDao
    private lateinit var usuarioActualId:String
    private lateinit var myApp: DailyRepApp
    var lunes=false
    var martes=false
    var miercoles=false
    var jueves=false
    var viernes=false
    var sabado=false
    var domingo=false

    var listaDias=mutableSetOf<Int>()
    private var editandoDias=false
    val context: Context =this
    private lateinit var serieDao: SerieDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityProgresoBinding.inflate(layoutInflater)
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
        usuarioDao=myApp.usuarioDao
        rachaDao=myApp.rachaDao
        serieDao=myApp.serieDao
        ponerEntrenamientosCompletados()
        ponerDiasSeleccionados()
        cambiarApartados()
        setupDiaButton(binding.L, 0)
        setupDiaButton(binding.Ma, 1)
        setupDiaButton(binding.Mi, 2)
        setupDiaButton(binding.J, 3)
        setupDiaButton(binding.V, 4)
        setupDiaButton(binding.S, 5)
        setupDiaButton(binding.D, 6)
        editarDiasSeleccionados()
        ponerPRs()
    }

    private fun ponerPRs() {
        lifecycleScope.launch{
            var prPressBanca:Int?=null
            var prSentadilla:Int?=null
            var prPesoMuerto:Int?=null
            withContext(Dispatchers.IO){
                prSentadilla=serieDao.obtenerPRMaximo(usuarioActualId, "Sentadilla Libre")
                prPesoMuerto=serieDao.obtenerPRMaximo(usuarioActualId,"Peso Muerto Convencional")
                prPressBanca=serieDao.obtenerPRMaximo(usuarioActualId, "Press de Banca con Barra")
            }
            if(prPressBanca!=null){
                val textoPr="$prPressBanca Kg"
                binding.pesoPressBanca.setText(textoPr)
            }
            if(prSentadilla!=null){
                val textoPr="$prSentadilla Kg"
                binding.pesoSentadilla.setText(textoPr)
            }
            if(prPesoMuerto!=null){
                val textoPr="$prPesoMuerto Kg"
                binding.pesoPesoMuerto.setText(textoPr)
            }
        }
    }

    private fun setupDiaButton(boton: View, indiceDia:Int) {
        boton.setOnClickListener {
            if (editandoDias) {
                val activado=when(indiceDia){
                    0->{
                        lunes=!lunes
                        lunes}
                    1->{martes=!martes
                        martes}
                    2->{
                    miercoles=!miercoles
                        miercoles}
                    3->{jueves=!jueves
                        jueves}
                    4->{
                        viernes=!viernes
                        viernes}
                    5-> {
                        sabado=!sabado
                        sabado}
                    6->{
                        domingo=!domingo
                        domingo}

                    else -> {false}
                }
                if (activado) {
                    boton.setBackgroundColor(getColor(R.color.naranja))
                    listaDias.add(indiceDia)
                } else {
                    boton.setBackgroundColor(getColor(R.color.plomo_oscuro))
                    listaDias.remove(indiceDia)
                }
            }
        }
    }

    private fun editarDiasSeleccionados() {
        binding.editarDias.setOnClickListener {
            editandoDias=!editandoDias
            if(editandoDias){
                Toast.makeText(
                    baseContext,"Selecciona los dias que entrenaras",
                    Toast.LENGTH_LONG
                ).show()
                val guardarCambios="GUARDAR CAMBIOS"
                binding.editarDias.setText(guardarCambios)
            }else{
                val edit="EDITAR"
                binding.editarDias.setText(edit)
                guardarCambiosenBD()
            }

        }
    }
    private fun guardarCambiosenBD(){
        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                val listaDiasObjetivoUsuario:List<DiasObjetivoUsuario> =
                    rachaDao.consultarDiasObjetivo(usuarioActualId)
                val listaIndices=listaDiasObjetivoUsuario.map{ diaObjetivo->
                    diaObjetivo.diaSemanaIndice
                }
                for(i in 0..6){
                    val diaObjetivo: DiasObjetivoUsuario= DiasObjetivoUsuario(usuarioActualId, i)
                    if(listaIndices.contains(i) && !listaDias.contains(i) ){
                        rachaDao.borrarDiaObjetivo(diaObjetivo)
                    }else if(!listaIndices.contains(i) && listaDias.contains(i)){
                        rachaDao.insertarDiaObjetivo(diaObjetivo)
                    }
                }
            }
            Toast.makeText(
                baseContext,"Cambios guardados correctamente!",
                Toast.LENGTH_LONG
            ).show()
            ponerDiasSeleccionados()
        }
    }

    private fun ponerDiasSeleccionados() {
        lifecycleScope.launch{
            val listaDiasObjetivoUsuario:List<DiasObjetivoUsuario> =withContext(Dispatchers.IO){
                rachaDao.consultarDiasObjetivo(usuarioActualId)
            }
            val listaIndices=listaDiasObjetivoUsuario.map{ diaObjetivo->
                diaObjetivo.diaSemanaIndice
            }
            resetearBotonesVisualmente() // Función auxiliar abajo
            lunes=false; martes=false; miercoles=false; jueves=false;
            viernes=false; sabado=false; domingo=false
           listaIndices.forEach {
               when(it){
                   0->{binding.L.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                   lunes=true
                   listaDias.add(0)}
                   1->{binding.Ma.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja) )
                           martes=true
                   listaDias.add(1)
                   }
                   2->{binding.Mi.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                       miercoles=true
                   listaDias.add(2)}
                   3->{binding.J.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                       jueves=true
                       listaDias.add(3)
                   }
                   4->{binding.V.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                   viernes=true
                   listaDias.add(4)}
                   5-> {binding.S.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                   sabado=true
                   listaDias.add(5)}
                   6->{binding.D.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                   domingo=true
                   listaDias.add(6)}
           }
        }
    } }

    private fun ponerEntrenamientosCompletados() {
        var usuario: Usuario?=null
        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                usuario=usuarioDao.getUsuarioPorId(usuarioActualId)
            }
            val diasEntrenados=usuario?.diasEntrenados?:0
            binding.entrenamientosCompletados.setText(diasEntrenados.toString())

        }
    }
    private fun resetearBotonesVisualmente(){
            val colorGris = ContextCompat.getColor(context, R.color.plomo_oscuro)
            binding.L.setBackgroundColor(colorGris)
            binding.Ma.setBackgroundColor(colorGris)
            binding.Mi.setBackgroundColor(colorGris)
            binding.J.setBackgroundColor(colorGris)
            binding.V.setBackgroundColor(colorGris)
            binding.S.setBackgroundColor(colorGris)
            binding.D.setBackgroundColor(colorGris)

    }
    private fun cambiarApartados() {
        binding.apartadoEjercicios.setOnClickListener {
            val cambiarAEjerciciosIntent: Intent =Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
        binding.apartadoRutinas.setOnClickListener {
            val cambiarARutinasIntent: Intent =Intent(context, RutinasActivity::class.java)
            startActivity(cambiarARutinasIntent)
        }
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent =Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }

    }
}