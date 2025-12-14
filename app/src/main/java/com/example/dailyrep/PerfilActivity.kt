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
import kotlin.math.roundToInt

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

        ponerDatosUsuarioYBotones()
        cambiarApartados()
        cambiarModo()
        binding.logOut.setOnClickListener {
            logOut()
        }
    }

    private fun ponerDatosUsuarioYBotones() {
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
                    val alturaMetros = altura / 100.0
                    val imc = (peso.toDouble() / (alturaMetros * alturaMetros)).roundToInt()
                    val calorias=if(generoMasculino){
                        (88.362+(13.397*peso)+(4.799*altura)-(5.677*edad)).roundToInt()
                    }else{
                        (447.593+(9.247*peso)+(3.098*altura)-(4.330*edad)).roundToInt()
                    }
                    val textoCalorias="$calorias kcal"
                    binding.imc.setText(imc.toString())
                    binding.calorias.setText(textoCalorias)
                    var generoMNuevo = usuarioActual.generoMasculino

                    binding.masculino.setOnClickListener {
                        generoMNuevo = true
                        binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                        binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))
                    }
                    binding.femenino.setOnClickListener {
                        generoMNuevo = false
                        binding.masculino.setBackgroundColor(ContextCompat.getColor(context, R.color.cuadros))
                        binding.femenino.setBackgroundColor(ContextCompat.getColor(context, R.color.naranja))
                    }

                    binding.guardar.setOnClickListener {
                        val edadActual = usuarioActual.edad
                        val alturaActual = usuarioActual.altura
                        val pesoActual = usuarioActual.peso
                        val generoMActual = usuarioActual.generoMasculino

                        val edadNueva = binding.editarEdad.text.toString().toIntOrNull() ?: edadActual
                        val alturaNueva = binding.editarAltura.text.toString().toIntOrNull() ?: alturaActual
                        val pesoNuevo = binding.editarPeso.text.toString().toIntOrNull() ?: pesoActual

                        if (edadNueva != edadActual || alturaNueva != alturaActual || pesoNuevo != pesoActual || generoMActual != generoMNuevo) {
                            val usuarioActualizado = usuarioActual.copy(
                                peso = pesoNuevo,
                                altura = alturaNueva,
                                edad = edadNueva,
                                generoMasculino = generoMNuevo
                            )
                            lifecycleScope.launch(Dispatchers.IO) {
                                myApp.usuarioDao.updateUsuario(usuarioActualizado)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(baseContext, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                    usuarioActual = usuarioActualizado
                                    ponerDatosUsuarioYBotones()
                                }
                            }
                        } else {
                            Toast.makeText(context, "No hubo cambios para guardar", Toast.LENGTH_SHORT).show()
                        }
                    }
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

}