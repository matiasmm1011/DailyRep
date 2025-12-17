package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.RegistroUsuariosActivity.Companion.USER_CORREO
import com.example.dailyrep.RegistroUsuariosActivity.Companion.USER_ID
import com.example.dailyrep.RegistroUsuariosActivity.Companion.USER_NAME
import com.example.dailyrep.databinding.ActivityConfiguracionInicial1Binding

class ConfiguracionInicial1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionInicial1Binding
    val context: Context = this
    var generoMasculino:Boolean?=null
    companion object{
        const val USER_ID1="usuario_id1"
        const val USER_NAME1="nombre_usuario1"
        const val USER_CORREO1="user_correo1"
        const val USER_EDAD1="user_edad1"
        const val USER_PESO1="user_peso1"
        const val USER_ALTURA1="user_altura1"
        const val USER_GENERO1="user_genero1"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityConfiguracionInicial1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.masculino.setOnClickListener {
            generoMasculino=true
            binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
            binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))
        }
        binding.femenino.setOnClickListener {
            generoMasculino=false
            binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))
            binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))

        }
        val correoRecibido=intent.getStringExtra(USER_CORREO)
        val nombreRecibido=intent.getStringExtra(USER_NAME)
        val idRecibido=intent.getStringExtra(USER_ID)
        binding.buttonContinuar1.setOnClickListener {
            val edad=binding.edad.text.toString().toIntOrNull()?:0
            val altura=binding.altura.text.toString().toIntOrNull()?:0
            val pesoActual=binding.pesoActual.text.toString().toIntOrNull()?:0
            //que peuda recibir double para el peso
            if(edad<=0 || altura<=0 ||pesoActual<=0|| generoMasculino==null){
                Toast.makeText(this, "Por favor, introduce un genero, edad, peso y altura válidos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intentCambioConfig2: Intent = Intent(context, ConfiguracionInicial2Activity::class.java)
            intentCambioConfig2.apply{
                intentCambioConfig2.putExtra(USER_ID1,idRecibido)
                intentCambioConfig2.putExtra(USER_NAME1,nombreRecibido)
                intentCambioConfig2.putExtra(USER_CORREO1,correoRecibido)
                intentCambioConfig2.putExtra(USER_EDAD1,edad)
                intentCambioConfig2.putExtra(USER_ALTURA1,altura)
                intentCambioConfig2.putExtra(USER_PESO1,pesoActual)
                intentCambioConfig2.putExtra(USER_GENERO1,generoMasculino)
            }
            startActivity(intentCambioConfig2)
        }
    }


}