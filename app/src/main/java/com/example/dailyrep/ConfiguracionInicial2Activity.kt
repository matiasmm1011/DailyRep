package com.example.dailyrep

import android.content.Context
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

        setContentView(R.layout.activity_configuracion_inicial2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.lunes.setOnClickListener {
//            binding.lunes.background.
//        }

    }
}