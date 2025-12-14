package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.databinding.ActivityPerfilBinding
import com.example.dailyrep.dataclases.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    val context: Context =this
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var myApp: DailyRepApp
    private lateinit var usuarioActualId:String
    private lateinit var usuarioActual: Usuario
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
        myApp=(applicationContext as DailyRepApp)
        sharedPreferences=getSharedPreferences(DailyRepApp.NOMBRE_FICHERO_SHARED_PREFERENCES, MODE_PRIVATE)
        val idGlobal = myApp.usuarioActualId
        if (idGlobal.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            usuarioActualId = idGlobal
        }
        ponerDatosUsuario()
        cambiarApartados()
        editarDatosUsuario()
        cambiarModo()
        binding.logOut.setOnClickListener {
            logOut()
        }
    }

    private fun ponerDatosUsuario() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarioDb = myApp.usuarioDao.getUsuarioPorId(usuarioActualId)
            withContext(Dispatchers.Main) {
                if (usuarioDb != null) {
                    usuarioActual = usuarioDb
                    val edad=usuarioDb.edad
                    val altura=usuarioDb.altura
                    val peso=usuarioDb.peso
                    binding.nombreUsuario.setText(usuarioDb.nombre)
                    binding.editarPeso.setText(peso.toString())
                    binding.editarAltura.setText(altura.toString())
                    binding.editarEdad.setText(edad.toString())
                    val generoMasculino=usuarioDb.generoMasculino
                    if(generoMasculino){
                        binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                    }else{
                        binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                    }
                    //TODO calcular IMC Y calorias
                } else {
                    Toast.makeText(baseContext, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                }
            }
        }
    }

    private fun cambiarModo() {
        val modoOscuro = sharedPreferences.getBoolean(DailyRepApp.KEY_MODO_OSCURO, true)
        binding.switchModo.isChecked = modoOscuro

        binding.switchModo.setOnCheckedChangeListener { buttonView, isChecked ->
            if(buttonView.isPressed){
            sharedPreferences.edit().apply {
                putBoolean(DailyRepApp.KEY_MODO_OSCURO, isChecked)
                apply()
            }
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            }
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
        binding.apartadoProgreso.setOnClickListener{
            val cambiarAProgresoIntent: Intent =Intent(context, ProgresoActivity::class.java)
            startActivity(cambiarAProgresoIntent)
        }
    }
    private fun logOut(){
        auth.signOut()
        val volverALoginIntent:Intent=Intent(context, LoginActivity::class.java)
        startActivity(volverALoginIntent)
    }
    private fun editarDatosUsuario(){
        binding.masculino.setOnClickListener {
            //cambiar a usuario su genero
            binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
            binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))

        }
        binding.femenino.setOnClickListener {
            binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))
            binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))

        }

        //TODO configurar demas botones
    }

}