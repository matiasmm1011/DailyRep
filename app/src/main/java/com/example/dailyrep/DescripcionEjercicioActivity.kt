package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityDescripcionEjercicioBinding

class DescripcionEjercicioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDescripcionEjercicioBinding
    val context: Context =this
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
        botones()

    }
    private fun botones(){
        binding.volverAtras.setOnClickListener {
            val volverAAtrasIntent: Intent =Intent(context, EjerciciosActivity::class.java)
            startActivity(volverAAtrasIntent)
        }
    }
}