package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityRegistroUsuariosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegistroUsuariosActivity : AppCompatActivity() {

    val context: Context = this
    private lateinit var binding: ActivityRegistroUsuariosBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistroUsuariosBinding.inflate(layoutInflater)
        auth= Firebase.auth
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.botonRegistrarme.setOnClickListener {
            val correo = binding.correoNuevo.text.toString()
            val password = binding.passwordNuevo.text.toString()
            crearUsuario(correo,password)
        }

    }

    fun crearUsuario(correo: String, password: String){
        auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener {
                task->
            if(task.isSuccessful){
                //Creado correctamente
                val intentConfig1: Intent = Intent(context, ConfiguracionInicial1Activity::class.java)
                startActivity(intentConfig1)
            }else{
                Toast.makeText(
                    baseContext,"No se pudo crear el usuario, intentalo nuevamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}