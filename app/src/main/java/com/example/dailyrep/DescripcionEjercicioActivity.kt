package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.EjerciciosActivity.Companion.DESCRIPCION_EJERCICIO
import com.example.dailyrep.EjerciciosActivity.Companion.IMAGEN_EJERCICIO
import com.example.dailyrep.EjerciciosActivity.Companion.NOMBRE_EJERCICIO
import com.example.dailyrep.EjerciciosActivity.Companion.PARTE_CUERPO_EJERCICIO
import com.example.dailyrep.EjerciciosActivity.Companion.TIPO_EJERCICIO
import com.example.dailyrep.databinding.ActivityDescripcionEjercicioBinding

class DescripcionEjercicioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDescripcionEjercicioBinding
    val context: Context =this
    private lateinit var nombreEjercicio: String
    private lateinit var descripcion: String
    private lateinit var imagen: String
    private lateinit var tipo: String
    private lateinit var parteCuerpo: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityDescripcionEjercicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recibirDatos()
        botones()
        binding.nombreEjercicio.setText(nombreEjercicio)
        binding.descripcion.setText(descripcion)
        if (imagen.isNotEmpty()) {
            val resourceId = resources.getIdentifier(
                imagen,
                "drawable",
                packageName
            )
            if (resourceId != 0) {
                binding.imagenEjercicio.setImageResource(resourceId)
            }
        }

    }

    private fun recibirDatos() {
        nombreEjercicio=intent.getStringExtra(NOMBRE_EJERCICIO)?:""
        descripcion=intent.getStringExtra(DESCRIPCION_EJERCICIO)?:""
        imagen=intent.getStringExtra(IMAGEN_EJERCICIO)?:""
        tipo=intent.getStringExtra(PARTE_CUERPO_EJERCICIO)?:""
        parteCuerpo=intent.getStringExtra(TIPO_EJERCICIO)?:""
    }

    private fun botones(){
        binding.volverAtras.setOnClickListener {
            val volverAAtrasIntent: Intent =Intent(context, EjerciciosActivity::class.java)
            startActivity(volverAAtrasIntent)
        }
    }
}