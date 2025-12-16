package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.dao.UsuarioDao
import com.example.dailyrep.databinding.ActivityProgresoBinding
import com.example.dailyrep.dataclases.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgresoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgresoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var usuarioActualId:String
    private lateinit var myApp: DailyRepApp
    val context: Context =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityProgresoBinding.inflate(layoutInflater)
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
        myApp=applicationContext as DailyRepApp
        usuarioDao=myApp.usuarioDao
        ponerEntrenamientosCompletados()
        cambiarApartados()
    }

    private fun ponerEntrenamientosCompletados() {
        var usuario: Usuario?=null
        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                usuario=usuarioDao.getUsuarioPorId(usuarioActualId)
            }
            val diasEntrenados=usuario?.diasEntrenados?:0
            binding.entrenamientosCompletados.setText(diasEntrenados.toString())

        }
    }

    private fun cambiarApartados() {
        binding.apartadoEjercicios.setOnClickListener {
            val cambiarAEjerciciosIntent: Intent =Intent(context, EjerciciosActivity::class.java)
            startActivity(cambiarAEjerciciosIntent)
        }
        binding.apartadoRutinas.setOnClickListener {
            val cambiarARutinasIntent: Intent =Intent(context, RutinasActivity::class.java)
            startActivity(cambiarARutinasIntent)
        }
        binding.apartadoPerfil.setOnClickListener {
            val cambiarAPerfilIntent: Intent =Intent(context, PerfilActivity::class.java)
            startActivity(cambiarAPerfilIntent)
        }

    }
}