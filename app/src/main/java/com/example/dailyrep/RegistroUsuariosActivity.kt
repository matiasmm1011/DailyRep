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
    companion object{
        const val USER_ID="usuario_id"
        const val USER_NAME="nombre_usuario"
        const val USER_CORREO="user_correo"
    }

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
            val nombre=binding.nombreUsuario.text.toString()
            if (correo.isEmpty()) {
                Toast.makeText(this, "Por favor, escribe un correo electrónico.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio.", Toast.LENGTH_SHORT).show()
                binding.nombreUsuario.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            crearUsuario(correo,password,nombre)
        }

    }

    fun crearUsuario(correo: String, password: String,nombre:String){
        auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener {
                task->
            if(task.isSuccessful){
                val firebaseUser = auth.currentUser
                val nuevoUid = firebaseUser?.uid
                val intentConfig1: Intent = Intent(context, ConfiguracionInicial1Activity::class.java)
                intentConfig1.apply {
                    intentConfig1.putExtra(USER_ID,nuevoUid)
                    intentConfig1.putExtra(USER_NAME,nombre)
                    intentConfig1.putExtra(USER_CORREO,correo)
                }
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