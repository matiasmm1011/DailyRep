package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityConfiguracionInicial2Binding

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
            val intentCambioLoginActivity: Intent = Intent(context, LoginActivity::class.java)
            startActivity(intentCambioLoginActivity)
        }

        binding.lunes.isSelected = false
        binding.martes.isSelected = false
        binding.miercoles.isSelected = false
        binding.jueves.isSelected = false
        binding.viernes.isSelected = false
        binding.sabado.isSelected = false
        binding.domingo.isSelected = false

        binding.lunes.setOnClickListener {
            binding.lunes.isSelected = !binding.lunes.isSelected
        }

        binding.martes.setOnClickListener {
            binding.martes.isSelected = !binding.martes.isSelected
        }

        binding.miercoles.setOnClickListener {
            binding.miercoles.isSelected = !binding.miercoles.isSelected
        }

        binding.jueves.setOnClickListener {
            binding.jueves.isSelected = !binding.jueves.isSelected
        }

        binding.viernes.setOnClickListener {
            binding.viernes.isSelected = !binding.viernes.isSelected
        }

        binding.sabado.setOnClickListener {
            binding.sabado.isSelected = !binding.sabado.isSelected
        }

        binding.domingo.setOnClickListener {
            binding.domingo.isSelected = !binding.domingo.isSelected
        }


    }
}