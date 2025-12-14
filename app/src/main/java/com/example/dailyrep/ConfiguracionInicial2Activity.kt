package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_ALTURA1
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_CORREO1
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_EDAD1
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_GENERO1
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_ID1
import com.example.dailyrep.ConfiguracionInicial1Activity.Companion.USER_NAME1
import com.example.dailyrep.databinding.ActivityConfiguracionInicial2Binding

class ConfiguracionInicial2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionInicial2Binding
    val context: Context = this
    companion object{
        const val USER_ID12="usuario_id2"
        const val USER_NAME2="nombre_usuario2"
        const val USER_CORREO2="user_correo2"
        const val USER_EDAD2="user_edad2"
        const val USER_PESO2="user_peso2"
        const val USER_ALTURA2="user_altura2"
        const val USER_GENERO2="user_genero2"
    }
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
        val correoRecibido=intent.getStringExtra(USER_CORREO1)
        val nombreRecibido=intent.getStringExtra(USER_NAME1)
        val idRecibido=intent.getStringExtra(USER_ID1)
        val edadRecibida=intent.getStringExtra(USER_EDAD1)
        val alturaRecibida=intent.getStringExtra(USER_ALTURA1)
        val generoMasculinoRecibido=intent.getStringExtra(USER_GENERO1)
        val listaDias=mutableSetOf<Int>()
        binding.buttonContinuar2.setOnClickListener {
            val intentCambioConfig3: Intent = Intent(context, ConfiguracionInicial3Activity::class.java)
            intentCambioConfig3.apply{
                intentCambioConfig3.putExtra(USER_ID1,idRecibido)
                intentCambioConfig3.putExtra(USER_NAME1,nombreRecibido)
                intentCambioConfig3.putExtra(USER_CORREO1,correoRecibido)
                intentCambioConfig3.putExtra(USER_EDAD1,edadRecibida)
                intentCambioConfig3.putExtra(USER_ALTURA1,alturaRecibida)
                intentCambioConfig3.putExtra(USER_GENERO1,generoMasculinoRecibido)
            }
            startActivity(intentCambioConfig3)
        }

        var lunes = false
        binding.lunes.setOnClickListener {
            lunes = !lunes
            if (lunes) {
                listaDias.add(0)
                binding.lunes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(0)
                binding.lunes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var martes = false
        binding.martes.setOnClickListener {
            martes = !martes
            if (martes) {
                listaDias.add(1)
                binding.martes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(1)
                binding.martes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var miercoles = false
        binding.miercoles.setOnClickListener {
            miercoles = !miercoles
            if (miercoles) {
                listaDias.add(2)
                binding.miercoles.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(2)
                binding.miercoles.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var jueves = false
        binding.jueves.setOnClickListener {
            jueves = !jueves
            if (jueves) {
                listaDias.add(3)
                binding.jueves.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(3)
                binding.jueves.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var viernes = false
        binding.viernes.setOnClickListener {
            viernes = !viernes
            if (viernes) {
                listaDias.add(4)
                binding.viernes.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(4)
                binding.viernes.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var sabado = false
        binding.sabado.setOnClickListener {
            sabado = !sabado
            if (sabado) {
                listaDias.add(5)
                binding.sabado.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(5)
                binding.sabado.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }

        var domingo = false
        binding.domingo.setOnClickListener {
            domingo = !domingo
            if (domingo) {
                listaDias.add(6)
                binding.domingo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                listaDias.remove(6)
                binding.domingo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
        }
    }
}