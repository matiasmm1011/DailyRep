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
import com.example.dailyrep.CrearEjercicioActivity1.Companion.TIPO_EJERCICIO
import com.example.dailyrep.EjerciciosActivity.Companion.ID_USUARIO
import com.example.dailyrep.EjerciciosActivity.Companion.NOMBRE_EJERCICIO

import com.example.dailyrep.databinding.ActivityCrearEjercicio2Binding

class CrearEjercicioActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityCrearEjercicio2Binding

    private lateinit var nombreEjercicio: String
    private lateinit var usuarioActualId: String
    private lateinit var tipoEjercicio: String

    val context: Context = this
    var selBrazos = false
    var selEspalda = false
    var selPecho = false
    var selPierna = false
    var selHombros = false
    var selAbdominales = false

    companion object {
        const val PARTE_CUERPO = "parte_cuerpo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearEjercicio2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usuarioActualId = intent.getStringExtra(ID_USUARIO) ?: ""
        nombreEjercicio = intent.getStringExtra(NOMBRE_EJERCICIO) ?: ""
        tipoEjercicio = intent.getStringExtra(TIPO_EJERCICIO) ?: ""

        seleccionarParteCuerpo()

        binding.buttonContinuar.setOnClickListener {
            val parteSeleccionada = obtenerParteSeleccionada()

            if (parteSeleccionada.isEmpty()) {
                Toast.makeText(this, "Por favor, selecciona una parte del cuerpo.", Toast.LENGTH_SHORT).show()
            } else {
                val intentSiguiente = Intent(context, CrearEjercicioActivity3::class.java)
                intentSiguiente.putExtra(ID_USUARIO, usuarioActualId)
                intentSiguiente.putExtra(NOMBRE_EJERCICIO, nombreEjercicio)
                intentSiguiente.putExtra(TIPO_EJERCICIO, tipoEjercicio)
                intentSiguiente.putExtra(PARTE_CUERPO, parteSeleccionada)

                startActivity(intentSiguiente)
            }
        }
    }

    private fun obtenerParteSeleccionada(): String {
        return when {
            selBrazos -> "Brazos"
            selEspalda -> "Espalda"
            selPecho -> "Pecho"
            selPierna -> "Pierna"
            selHombros -> "Hombros"
            selAbdominales -> "Abdominales"
            else -> ""
        }
    }

    private fun seleccionarParteCuerpo() {
        val colorNaranja = ContextCompat.getColor(this, R.color.naranja)
        val colorPlomo = ContextCompat.getColor(this, R.color.plomo_oscuro)

        binding.brazos.setOnClickListener {
            selBrazos = true
            selEspalda = false
            selPecho = false
            selPierna = false
            selHombros = false
            selAbdominales = false
            binding.brazos.setBackgroundColor(colorNaranja)
            binding.espalda.setBackgroundColor(colorPlomo)
            binding.pecho.setBackgroundColor(colorPlomo)
            binding.pierna.setBackgroundColor(colorPlomo)
            binding.hombros.setBackgroundColor(colorPlomo)
            binding.abdominales.setBackgroundColor(colorPlomo)
        }

        binding.espalda.setOnClickListener {
            selEspalda = true
            selBrazos = false
            selPecho = false
            selPierna = false
            selHombros = false
            selAbdominales = false
            binding.espalda.setBackgroundColor(colorNaranja)
            binding.brazos.setBackgroundColor(colorPlomo)
            binding.pecho.setBackgroundColor(colorPlomo)
            binding.pierna.setBackgroundColor(colorPlomo)
            binding.hombros.setBackgroundColor(colorPlomo)
            binding.abdominales.setBackgroundColor(colorPlomo)
        }

        binding.pecho.setOnClickListener {
            selPecho = true
            selBrazos = false
            selEspalda = false
            selPierna = false
            selHombros = false
            selAbdominales = false
            binding.pecho.setBackgroundColor(colorNaranja)
            binding.brazos.setBackgroundColor(colorPlomo)
            binding.espalda.setBackgroundColor(colorPlomo)
            binding.pierna.setBackgroundColor(colorPlomo)
            binding.hombros.setBackgroundColor(colorPlomo)
            binding.abdominales.setBackgroundColor(colorPlomo)
        }

        binding.pierna.setOnClickListener {
            selPierna = true
            selBrazos = false
            selEspalda = false
            selPecho = false
            selHombros = false
            selAbdominales = false
            binding.pierna.setBackgroundColor(colorNaranja)
            binding.brazos.setBackgroundColor(colorPlomo)
            binding.espalda.setBackgroundColor(colorPlomo)
            binding.pecho.setBackgroundColor(colorPlomo)
            binding.hombros.setBackgroundColor(colorPlomo)
            binding.abdominales.setBackgroundColor(colorPlomo)
        }

        binding.hombros.setOnClickListener {
            selHombros = true
            selBrazos = false
            selEspalda = false
            selPecho = false
            selPierna = false
            selAbdominales = false
            binding.hombros.setBackgroundColor(colorNaranja)
            binding.brazos.setBackgroundColor(colorPlomo)
            binding.espalda.setBackgroundColor(colorPlomo)
            binding.pecho.setBackgroundColor(colorPlomo)
            binding.pierna.setBackgroundColor(colorPlomo)
            binding.abdominales.setBackgroundColor(colorPlomo)
        }

        binding.abdominales.setOnClickListener {
            selAbdominales = true
            selBrazos = false
            selEspalda = false
            selPecho = false
            selPierna = false
            selHombros = false
            binding.abdominales.setBackgroundColor(colorNaranja)
            binding.brazos.setBackgroundColor(colorPlomo)
            binding.espalda.setBackgroundColor(colorPlomo)
            binding.pecho.setBackgroundColor(colorPlomo)
            binding.pierna.setBackgroundColor(colorPlomo)
            binding.hombros.setBackgroundColor(colorPlomo)
        }
    }
}