package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    val context: Context =this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityLoginBinding.inflate(layoutInflater)
        auth= Firebase.auth
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.botonInicioSesion.setOnClickListener {
            val correo=binding.correo.text.toString()
            val password=binding.password.text.toString()
            loginUsuario(correo,password)
        }

        val currentUser = auth.currentUser

        if(currentUser!=null){
            val intentUsuarioLogueado=Intent(context, EjerciciosActivity::class.java)
            startActivity(intentUsuarioLogueado)
        }

        binding.buttonCrearUsuario.setOnClickListener {
            val intentCambioRegistro: Intent = Intent(context, RegistroUsuariosActivity::class.java)
            startActivity(intentCambioRegistro)
        }

    }

    fun loginUsuario(correo:String, password:String){
        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener {
            task->
                if(task.isSuccessful){
                    val intentLogueado:Intent = Intent(context, EjerciciosActivity::class.java)
                    startActivity(intentLogueado)
                }else{
                    Toast.makeText(
                        baseContext,"El usuario no pudo loguearse correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}