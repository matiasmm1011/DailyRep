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
import com.example.dailyrep.dao.HistorialDao
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
import java.util.Calendar

class ProgresoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgresoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var rachaDao: RachaDao
    private lateinit var usuarioActualId: String
    private lateinit var myApp: DailyRepApp
    private lateinit var historialDao: HistorialDao
    var lunes = false
    var martes = false
    var miercoles = false
    var jueves = false
    var viernes = false
    var sabado = false
    var domingo = false

    var listaDias = mutableSetOf<Int>()
    private var editandoDias = false
    val context: Context = this
    private lateinit var serieDao: SerieDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProgresoBinding.inflate(layoutInflater)
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
        usuarioDao = myApp.usuarioDao
        rachaDao = myApp.rachaDao
        serieDao = myApp.serieDao
        historialDao=myApp.historialDao
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
        verificarRacha()
        actualizarBarraDeProgresoSemanal()
        ponerPRs()
    }

    private fun actualizarBarraDeProgresoSemanal() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val diasObjetivo = rachaDao.consultarDiasObjetivo(usuarioActualId)
                val diasObjetivoSet=diasObjetivo.map{
                    dia->
                    dia.diaSemanaIndice
                }.toSet()
                val metaSemanal = diasObjetivo.size

                val calendario = Calendar.getInstance()

                calendario.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                calendario.set(Calendar.HOUR_OF_DAY, 0)
                calendario.set(Calendar.MINUTE, 0)
                calendario.set(Calendar.SECOND, 0)
                val inicioSemanaMillis = calendario.timeInMillis

                calendario.add(Calendar.DAY_OF_YEAR, 7)
                val finSemanaMillis = calendario.timeInMillis

                val historialSemanalFechas= historialDao.obtenerFechasSemana(usuarioActualId,inicioSemanaMillis,finSemanaMillis)

                val diasCumplidosUnicos = mutableSetOf<Int>()
                historialSemanalFechas.forEach { fechaMillis ->
                    val calendarioTemp = Calendar.getInstance()
                    calendarioTemp.timeInMillis = fechaMillis
                    val diaSemanaIndice = convertirCalendarAMiIndice(calendarioTemp.get(Calendar.DAY_OF_WEEK))
                    if (diasObjetivoSet.contains(diaSemanaIndice)) {
                        diasCumplidosUnicos.add(diaSemanaIndice)
                    }
                }
                val diasRealesEntrenados = diasCumplidosUnicos.size

                val porcentaje = ((diasRealesEntrenados.toFloat() / metaSemanal) * 100).toInt()
                withContext(Dispatchers.Main) {
                    binding.indicadorDiasCompletados.text = "$diasRealesEntrenados/$metaSemanal días completados"
                    binding.barraProgresoSemanal.setProgress(porcentaje, true)
                }

            }


        }
    }

    private fun verificarRacha() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val usuarioActual = usuarioDao.getUsuarioPorId(usuarioActualId)
                if (usuarioActual?.ultimoDiaEntrenamientoFecha == null) {
                    return@withContext
                } else {
                    val diasObjetivo = rachaDao.consultarDiasObjetivo(usuarioActualId)
                    val setDiasObjetivo = diasObjetivo.map { it.diaSemanaIndice }.toSet()
                    val calendario = Calendar.getInstance() // Hoy
                    val hoyMillis = calendario.timeInMillis
                    calendario.timeInMillis = usuarioActual.ultimoDiaEntrenamientoFecha ?: 0
                    calendario.add(Calendar.DAY_OF_YEAR, 1)

                    var rachaRota = false
                    while (calendario.timeInMillis < hoyMillis) {
                        val diaSemana =
                            convertirCalendarAMiIndice(calendario.get(Calendar.DAY_OF_WEEK))

                        if (!esMismoDia(calendario.timeInMillis, hoyMillis)) {
                            if (setDiasObjetivo.contains(diaSemana)) {
                                rachaRota = true
                                break
                            }
                        }
                        calendario.add(Calendar.DAY_OF_YEAR, 1)

                    }
                    if (rachaRota && usuarioActual.rachaActual > 0) {
                        usuarioActual.rachaActual = 0
                        usuarioDao.updateUsuario(usuarioActual)
                        withContext(Dispatchers.Main) {
                            binding.diasRacha.text = "0 días"
                            binding.iconoFuego.setColorFilter(getColor(R.color.plomo))
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.diasRacha.text = "${usuarioActual.rachaActual} días"
                            binding.iconoFuego.setColorFilter(getColor(R.color.naranja))
                        }
                    }


                }
            }
        }

    }
    private fun esMismoDia(dia1:Long, dia2:Long):Boolean{
        val calendario1 = Calendar.getInstance().apply { timeInMillis = dia1 }
        val calendario2 = Calendar.getInstance().apply { timeInMillis = dia2 }
        return calendario1.get(Calendar.DAY_OF_YEAR) == calendario2.get(Calendar.DAY_OF_YEAR) &&
                calendario1.get(Calendar.YEAR) == calendario2.get(Calendar.YEAR)
    }
    private fun convertirCalendarAMiIndice(diaCalendario:Int):Int {
        return when (diaCalendario) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }
    }


    private fun ponerPRs() {
        lifecycleScope.launch {
            var prPressBanca: Int? = null
            var prSentadilla: Int? = null
            var prPesoMuerto: Int? = null
            withContext(Dispatchers.IO) {
                prSentadilla = serieDao.obtenerPRMaximo(usuarioActualId, "Sentadilla Libre")
                prPesoMuerto = serieDao.obtenerPRMaximo(usuarioActualId, "Peso Muerto Convencional")
                prPressBanca = serieDao.obtenerPRMaximo(usuarioActualId, "Press de Banca con Barra")
            }
            if (prPressBanca != null) {
                val textoPr = "$prPressBanca Kg"
                binding.pesoPressBanca.setText(textoPr)
            }
            if (prSentadilla != null) {
                val textoPr = "$prSentadilla Kg"
                binding.pesoSentadilla.setText(textoPr)
            }
            if (prPesoMuerto != null) {
                val textoPr = "$prPesoMuerto Kg"
                binding.pesoPesoMuerto.setText(textoPr)
            }
        }
    }

    private fun setupDiaButton(boton: View, indiceDia: Int) {
        boton.setOnClickListener {
            if (editandoDias) {
                val activado = when (indiceDia) {
                    0 -> {
                        lunes = !lunes
                        lunes
                    }

                    1 -> {
                        martes = !martes
                        martes
                    }

                    2 -> {
                        miercoles = !miercoles
                        miercoles
                    }

                    3 -> {
                        jueves = !jueves
                        jueves
                    }

                    4 -> {
                        viernes = !viernes
                        viernes
                    }

                    5 -> {
                        sabado = !sabado
                        sabado
                    }

                    6 -> {
                        domingo = !domingo
                        domingo
                    }

                    else -> {
                        false
                    }
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
            editandoDias = !editandoDias
            if (editandoDias) {
                Toast.makeText(
                    baseContext, "Selecciona los dias que entrenaras",
                    Toast.LENGTH_LONG
                ).show()
                val guardarCambios = "GUARDAR CAMBIOS"
                binding.editarDias.setText(guardarCambios)
            } else {
                val edit = "EDITAR"
                binding.editarDias.setText(edit)
                guardarCambiosenBD()
            }

        }
    }

    private fun guardarCambiosenBD() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val listaDiasObjetivoUsuario: List<DiasObjetivoUsuario> =
                    rachaDao.consultarDiasObjetivo(usuarioActualId)
                val listaIndices = listaDiasObjetivoUsuario.map { diaObjetivo ->
                    diaObjetivo.diaSemanaIndice
                }
                for (i in 0..6) {
                    val diaObjetivo: DiasObjetivoUsuario = DiasObjetivoUsuario(usuarioActualId, i)
                    if (listaIndices.contains(i) && !listaDias.contains(i)) {
                        rachaDao.borrarDiaObjetivo(diaObjetivo)
                    } else if (!listaIndices.contains(i) && listaDias.contains(i)) {
                        rachaDao.insertarDiaObjetivo(diaObjetivo)
                    }
                }
            }
            Toast.makeText(
                baseContext, "Cambios guardados correctamente!",
                Toast.LENGTH_LONG
            ).show()
            ponerDiasSeleccionados()
        }
    }

    private fun ponerDiasSeleccionados() {
        lifecycleScope.launch {
            val listaDiasObjetivoUsuario: List<DiasObjetivoUsuario> = withContext(Dispatchers.IO) {
                rachaDao.consultarDiasObjetivo(usuarioActualId)
            }
            val listaIndices = listaDiasObjetivoUsuario.map { diaObjetivo ->
                diaObjetivo.diaSemanaIndice
            }
            resetearBotonesVisualmente() // Función auxiliar abajo
            lunes = false; martes = false; miercoles = false; jueves = false;
            viernes = false; sabado = false; domingo = false
            listaIndices.forEach {
                when (it) {
                    0 -> {
                        binding.L.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        lunes = true
                        listaDias.add(0)
                    }

                    1 -> {
                        binding.Ma.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        martes = true
                        listaDias.add(1)
                    }

                    2 -> {
                        binding.Mi.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        miercoles = true
                        listaDias.add(2)
                    }

                    3 -> {
                        binding.J.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        jueves = true
                        listaDias.add(3)
                    }

                    4 -> {
                        binding.V.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        viernes = true
                        listaDias.add(4)
                    }

                    5 -> {
                        binding.S.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        sabado = true
                        listaDias.add(5)
                    }

                    6 -> {
                        binding.D.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.naranja
                            )
                        )
                        domingo = true
                        listaDias.add(6)
                    }
                }
            }
        }
    }

    private fun ponerEntrenamientosCompletados() {
        var usuario: Usuario? = null
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                usuario = usuarioDao.getUsuarioPorId(usuarioActualId)
            }
            val diasEntrenados = usuario?.diasEntrenados ?: 0
            binding.entrenamientosCompletados.setText(diasEntrenados.toString())

        }
    }

    private fun resetearBotonesVisualmente() {
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
            val cambiarAEjerciciosIntent: Intent = Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
        binding.apartadoRutinas.setOnClickListener {
            val cambiarARutinasIntent: Intent = Intent(context, RutinasActivity::class.java)
            startActivity(cambiarARutinasIntent)
        }
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent = Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }

    }
}