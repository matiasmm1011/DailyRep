package com.example.dailyrep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyrep.databinding.ActivityConfiguracionInicial3Binding

class ConfiguracionInicial3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionInicial3Binding
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityConfiguracionInicial3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var seleccionadoSedentario = false
        var seleccionadoLigActivo = false
        var seleccionadoModActivo = false
        var seleccionadoMuyActivo = false
        var seleccionadoExtActivo = false

        binding.sedentario.setOnClickListener {
            seleccionadoSedentario = !seleccionadoSedentario
            if (seleccionadoSedentario){
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
            seleccionadoSedentario = false
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
        }

        binding.ligeramenteActivo.setOnClickListener {
            seleccionadoLigActivo = !seleccionadoLigActivo
            if (seleccionadoLigActivo){
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
            seleccionadoLigActivo = false
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
        }

        binding.moderadamenteActivo.setOnClickListener {
            seleccionadoModActivo = !seleccionadoModActivo
            if (seleccionadoModActivo){
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
            seleccionadoModActivo = false
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
        }

        binding.muyActivo.setOnClickListener {
            seleccionadoMuyActivo = !seleccionadoMuyActivo
            if (seleccionadoMuyActivo){
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
            seleccionadoMuyActivo = false
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
        }

        binding.extremadamenteActivo.setOnClickListener {
            seleccionadoExtActivo = !seleccionadoExtActivo
            if (seleccionadoExtActivo){
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
            } else {
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }
            seleccionadoExtActivo = false
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
        }

        binding.buttonContinuar3.setOnClickListener {
            val intentCambioLogin: Intent = Intent(context, LoginActivity::class.java)
            startActivity(intentCambioLogin)
        }

    }

    fun seleccionarDia(seleccionadoSedentario: Boolean,
                       seleccionadoLigActivo: Boolean,
                       seleccionadoModActivo: Boolean,
                       seleccionadoMuyActivo: Boolean,
                       seleccionadoExtActivo: Boolean){
        if (seleccionadoSedentario) {
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
        } else if (seleccionadoLigActivo){
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
        } else if (seleccionadoModActivo){
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
        } else if (seleccionadoMuyActivo){
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
        } else if (seleccionadoExtActivo){
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
        } else {
            binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))

        }
    }

}