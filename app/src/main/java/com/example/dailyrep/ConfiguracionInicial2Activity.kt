package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityConfiguracionInicial2Binding
import com.example.dailyrep.databinding.ActivityConfiguracionInicial3Binding

class ConfiguracionInicial2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionInicial2Binding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityConfiguracionInicial2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonContinuar2.setOnClickListener {
            val intentCambioConfig3: Intent = Intent(context, ConfiguracionInicial3Activity::class.java)
            startActivity(intentCambioConfig3)
        }

        var lunes = false
        binding.lunes.setOnClickListener {
            lunes = !lunes
            if (lunes) {
                binding.lunes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.lunes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var martes = false
        binding.martes.setOnClickListener {
            martes = !martes
            if (martes) {
                binding.martes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.martes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var miercoles = false
        binding.miercoles.setOnClickListener {
            miercoles = !miercoles
            if (miercoles) {
                binding.miercoles.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.miercoles.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var jueves = false
        binding.jueves.setOnClickListener {
            jueves = !jueves
            if (jueves) {
                binding.jueves.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.jueves.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var viernes = false
        binding.viernes.setOnClickListener {
            viernes = !viernes
            if (viernes) {
                binding.viernes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.viernes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var sabado = false
        binding.sabado.setOnClickListener {
            sabado = !sabado
            if (sabado) {
                binding.sabado.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.sabado.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var domingo = false
        binding.domingo.setOnClickListener {
            domingo = !domingo
            if (domingo) {
                binding.domingo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.domingo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }
    }
}