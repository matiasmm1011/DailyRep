
package com.example.dailyrep
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.EjerciciosActivity.Companion.ID_USUARIO
import com.example.dailyrep.EjerciciosActivity.Companion.NOMBRE_EJERCICIO

import com.example.dailyrep.databinding.ActivityCrearEjercicio1Binding

class CrearEjercicioActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEjercicio1Binding
    private lateinit var nombreEjercicio: String
    private lateinit var usuarioActualId: String
    val context: Context = this
    var selMancuernas = false
    var selPolea = false
    var selMaquina = false
    var selBarra = false
    var selPesoCorporal = false
    var selCardio = false

    companion object {
        const val TIPO_EJERCICIO = "tipo_ejercicio"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearEjercicio1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usuarioActualId = intent.getStringExtra(ID_USUARIO) ?: ""
        nombreEjercicio = intent.getStringExtra(NOMBRE_EJERCICIO) ?: ""
        seleccionarTipoEjercicio()
        cambiarApartados()
        binding.buttonContinuar.setOnClickListener {
            val tipoSeleccionado = obtenerTipoSeleccionado()

            if (tipoSeleccionado.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona un tipo de ejercicio.", Toast.LENGTH_SHORT).show()
            } else {
                val intentSiguiente = Intent(context, CrearEjercicioActivity2::class.java)
                intentSiguiente.putExtra(ID_USUARIO, usuarioActualId)
                intentSiguiente.putExtra(NOMBRE_EJERCICIO, nombreEjercicio)
                intentSiguiente.putExtra(TIPO_EJERCICIO, tipoSeleccionado)
                startActivity(intentSiguiente)
            }
        }
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


    private fun obtenerTipoSeleccionado(): String {
        return when {
            selMancuernas -> "Mancuernas"
            selBarra -> "Barra"
            selPolea -> "Polea"
            selMaquina -> "Máquina"
            selPesoCorporal -> "Peso Corporal"
            selCardio -> "Cardio"
            else -> ""
        }
    }

    private fun seleccionarTipoEjercicio() {
        val colorNaranja = ContextCompat.getColor(this, R.color.naranja)
        val colorPlomo = ContextCompat.getColor(this, R.color.plomo_oscuro)

        binding.mancuernas.setOnClickListener {
            selMancuernas = true
            selPolea = false
            selMaquina = false
            selBarra = false
            selPesoCorporal = false
            selCardio = false

            binding.mancuernas.setBackgroundColor(colorNaranja)
            binding.polea.setBackgroundColor(colorPlomo)
            binding.maquina.setBackgroundColor(colorPlomo)
            binding.barra.setBackgroundColor(colorPlomo)
            binding.pesoCorporal.setBackgroundColor(colorPlomo)
            binding.cardio.setBackgroundColor(colorPlomo)
        }

        binding.polea.setOnClickListener {
            selPolea = true
            selMancuernas = false
            selMaquina = false
            selBarra = false
            selPesoCorporal = false
            selCardio = false
            binding.polea.setBackgroundColor(colorNaranja)
            binding.mancuernas.setBackgroundColor(colorPlomo)
            binding.maquina.setBackgroundColor(colorPlomo)
            binding.barra.setBackgroundColor(colorPlomo)
            binding.pesoCorporal.setBackgroundColor(colorPlomo)
            binding.cardio.setBackgroundColor(colorPlomo)
        }

        binding.maquina.setOnClickListener {
            selMaquina = true
            selMancuernas = false
            selPolea = false
            selBarra = false
            selPesoCorporal = false
            selCardio = false
            binding.maquina.setBackgroundColor(colorNaranja)
            binding.mancuernas.setBackgroundColor(colorPlomo)
            binding.polea.setBackgroundColor(colorPlomo)
            binding.barra.setBackgroundColor(colorPlomo)
            binding.pesoCorporal.setBackgroundColor(colorPlomo)
            binding.cardio.setBackgroundColor(colorPlomo)
        }

        binding.barra.setOnClickListener {
            selBarra = true
            selMancuernas = false
            selPolea = false
            selMaquina = false
            selPesoCorporal = false
            selCardio = false
            binding.barra.setBackgroundColor(colorNaranja)
            binding.mancuernas.setBackgroundColor(colorPlomo)
            binding.polea.setBackgroundColor(colorPlomo)
            binding.maquina.setBackgroundColor(colorPlomo)
            binding.pesoCorporal.setBackgroundColor(colorPlomo)
            binding.cardio.setBackgroundColor(colorPlomo)
        }

        binding.pesoCorporal.setOnClickListener {
            selPesoCorporal = true
            selMancuernas = false
            selPolea = false
            selMaquina = false
            selBarra = false
            selCardio = false
            binding.pesoCorporal.setBackgroundColor(colorNaranja)
            binding.mancuernas.setBackgroundColor(colorPlomo)
            binding.polea.setBackgroundColor(colorPlomo)
            binding.maquina.setBackgroundColor(colorPlomo)
            binding.barra.setBackgroundColor(colorPlomo)
            binding.cardio.setBackgroundColor(colorPlomo)
        }

        binding.cardio.setOnClickListener {
            selCardio = true
            selMancuernas = false
            selPolea = false
            selMaquina = false
            selBarra = false
            selPesoCorporal = false
            binding.cardio.setBackgroundColor(colorNaranja)
            binding.mancuernas.setBackgroundColor(colorPlomo)
            binding.polea.setBackgroundColor(colorPlomo)
            binding.maquina.setBackgroundColor(colorPlomo)
            binding.barra.setBackgroundColor(colorPlomo)
            binding.pesoCorporal.setBackgroundColor(colorPlomo)
        }
    }
}