package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyrep.adapters.RutinaAdapter
import com.example.dailyrep.dao.RutinaDao
import com.example.dailyrep.databinding.ActivityRutinasBinding
import com.example.dailyrep.databinding.MenuCrearRutinaBinding
import com.example.dailyrep.databinding.MenuParteCuerpoBinding
import com.example.dailyrep.dataclases.Ejercicio
import com.example.dailyrep.dataclases.RelacionEjeRut
import com.example.dailyrep.dataclases.Rutina
import com.example.dailyrep.dataclases.SeriePlanificada
import com.google.android.material.chip.Chip
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RutinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinasBinding
    private var listaRutinas: MutableList<Rutina> = mutableListOf<Rutina>()
    val rutinas = mutableSetOf<String>()
    private lateinit var rutinaDao: RutinaDao
    private lateinit var auth: FirebaseAuth
    companion object{
        const val NOMBRE_RUTINA="nombre_rutina"
        const val ID_RUTINA="id_rutina"
        const val ID_USUARIO="id_usuario"
    }
    private val rutinasAdapter: RutinaAdapter by lazy{ RutinaAdapter(){
        rutina ->
        val intentComenzarEntrenamiento:Intent=Intent(context, EntrenamientoActivity::class.java)
        intentComenzarEntrenamiento.apply{
            intentComenzarEntrenamiento.putExtra(NOMBRE_RUTINA,rutina.nombreRutina)
            intentComenzarEntrenamiento.putExtra(ID_RUTINA, rutina.id)
            intentComenzarEntrenamiento.putExtra(ID_USUARIO,rutina.creadorId)
        }
        startActivity(intentComenzarEntrenamiento)
        //TODO poner true de enEtrenamiento en sharedPreferences
    }
    }
    val context: Context = this
    private lateinit var myApp:DailyRepApp
    private lateinit var usuarioActualId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRutinasBinding.inflate(layoutInflater)
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
        cambiarApartados()
        binding.recyclerRutinas.layoutManager= LinearLayoutManager(context)
        binding.recyclerRutinas.adapter = rutinasAdapter
        myApp = applicationContext as DailyRepApp
        rutinaDao = myApp.rutinaDao
        ponerRutinas()
        crearRutina()
        // --- INICIO BLOQUE DE PRUEBA (PEGAR AL FINAL DE ONCREATE) ---
        lifecycleScope.launch(Dispatchers.IO) {
            // Esperamos un poco para asegurar que la UI cargó
            delay(1000)

            val userId = auth.currentUser?.uid

            if (userId != null) {
                // 1. Verificar si ya existe la rutina de prueba para no duplicar
                // Nota: Asegúrate de que el usuario exista en la tabla 'Usuario' local,
                // si no Room lanzará error de ForeignKey.

                val nombrePrueba = "Rutina TEST Completa"
                // Hacemos una consulta rápida para ver si existe (puedes omitir el if si quieres crearla siempre)
                val rutinas = myApp.rutinaDao.getAll(userId)
                val existe = rutinas.any { it.nombreRutina == nombrePrueba }

                if (!existe) {
                    Log.d("SEMILLA", "Iniciando creación de datos de prueba...")

                    // A) CREAR RUTINA
                    val nuevaRutina = Rutina(
                        id = 0, // 0 para autogenerar
                        nombreRutina = nombrePrueba,
                        creadorId = userId
                    )
                    // Aquí usamos el nuevo método que devuelve Long
                    val rutinaId = myApp.rutinaDao.insert(nuevaRutina)
                    Log.d("SEMILLA", "Rutina creada ID: $rutinaId")

                    // B) DEFINIR EJERCICIOS A INSERTAR
                    // Triple: (Nombre, Tipo, Lista de Series(Peso, Reps))
                    val datosPrueba = listOf(
                        Triple("Press Banca Test", "Pecho", listOf(100 to "10", 100 to "8")),
                        Triple("Sentadilla Test", "Pierna", listOf(120 to "5", 130 to "5")),
                        Triple("Curl Biceps Test", "Brazo", listOf(30 to "12", 30 to "10"))
                    )

                    // C) INSERTAR EJERCICIOS, RELACIONES Y SERIES
                    for ((nombreEj, parteCuerpo, seriesData) in datosPrueba) {

                        // 1. Insertar Ejercicio
                        val nuevoEjercicio = Ejercicio(
                            id = 0,
                            nombre = nombreEj,
                            tipo = "Pesas", // Valor por defecto
                            parteCuerpo = parteCuerpo,
                            descripcion = "Ejercicio de prueba generado automáticamente",
                            creadorId = userId,
                            esPredeterminado = false
                        )
                        val ejercicioId = myApp.ejercicioDao.insert(nuevoEjercicio)

                        // 2. Insertar RELACIÓN (El puente vital)
                        val nuevaRelacion = RelacionEjeRut(
                            id = 0,
                            rutinaId = rutinaId,
                            ejercicioId = ejercicioId,
                            notas = "Nota de prueba para $nombreEj"
                        )
                        // Asumiendo que agregaste el DAO de Relacion a tu database
                        val relacionId = myApp.database.relacionEjeRutDao().insert(nuevaRelacion)

                        // 3. Insertar SERIES (Apuntando a la Relación)
                        var contadorSerie = 1
                        for ((peso, reps) in seriesData) {
                            val nuevaSerie = SeriePlanificada(
                                id = 0,
                                relacionId = relacionId, // <--- CLAVE: Apunta a la relación
                                numeroSerie = contadorSerie++,
                                peso = peso,
                                repeticiones = reps.toInt()// En tu entidad es String
                            )
                            myApp.serieDao.insert(nuevaSerie)
                        }
                    }

                    Log.d("SEMILLA", "¡Datos insertados correctamente! Actualizando UI...")

                    // Recargamos la lista en el hilo principal para verlo al instante
                    withContext(Dispatchers.Main) {
                        ponerRutinas()
                        Toast.makeText(context, "Rutina de prueba creada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("SEMILLA", "La rutina de prueba ya existe.")
                }
            }
        }
// --- FIN BLOQUE DE PRUEBA ---
    }

    private fun crearRutina() {
        binding.agregarRutina.setOnClickListener {
            mostrarCrearRutina(binding.agregarRutina)
        }
    }

    private fun ponerRutinas() {
        lifecycleScope.launch {
            val listaNueva = withContext(Dispatchers.IO) {
                val rutinas = myApp.rutinaDao.getAll(usuarioActualId)
                rutinas
            }
            rutinasAdapter.ponerListaRutinas(listaNueva)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    fun mostrarCrearRutina(ancla: View){
        val bindingMenu = MenuCrearRutinaBinding.inflate(layoutInflater)
        val ancho = dpToPx(250)
        val altura = dpToPx(150)
        val popupWindow = PopupWindow(
            bindingMenu.root,
            ancho,
            altura,
            true
        )
        val xoff = ancla.width - ancho
        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        popupWindow.showAsDropDown(ancla, xoff, 0)
        bindingMenu.buttonCrearRutinaPP.setOnClickListener {
            val nombreRutinaET: String = bindingMenu.etNombreRutina.text.toString()
            popupWindow.dismiss()
                lifecycleScope.launch(Dispatchers.IO) {
                    val rutinaEjemplo = Rutina(
                        id = 0,
                        nombreRutina = nombreRutinaET,
                        creadorId = usuarioActualId
                    )
                    withContext(Dispatchers.IO) {
                        rutinaDao.insertAll(rutinaEjemplo)
                    }
                //TODO llevar al apartado de crear rutina, nueva activity
            }
            ponerRutinas()
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