package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityPerfilBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    val context: Context =this
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cambiarApartados()
        binding.logOut.setOnClickListener {
            logOut()
        }
    }

    private fun cambiarApartados() {
        binding.apartadoEjercicios.setOnClickListener {
            val cambiarAEjerciciosIntent: Intent =Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
    }
    private fun logOut(){
        auth.signOut()
        val volverALoginIntent:Intent=Intent(context, LoginActivity::class.java)
        startActivity(volverALoginIntent)
    }
}