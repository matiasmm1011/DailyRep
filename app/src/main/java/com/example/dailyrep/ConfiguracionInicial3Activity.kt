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
import androidx.lifecycle.lifecycleScope
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.LISTA_DIAS2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_ALTURA2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_CORREO2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_EDAD2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_GENERO2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_ID2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_NAME2
import com.example.dailyrep.ConfiguracionInicial2Activity.Companion.USER_PESO2
import com.example.dailyrep.databinding.ActivityConfiguracionInicial3Binding
import com.example.dailyrep.dataclases.DiasObjetivoUsuario
import com.example.dailyrep.dataclases.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfiguracionInicial3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionInicial3Binding
    val context: Context = this
    var seleccionadoSedentario = false
    var seleccionadoLigActivo = false
    var seleccionadoModActivo = false
    var seleccionadoMuyActivo = false
    var seleccionadoExtActivo = false
    val listaSeleccionado=mutableListOf<Int>(0,0,0,0,0)
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
        seleccionarActividad()
        val correoRecibido=intent.getStringExtra(USER_CORREO2)
        val nombreRecibido=intent.getStringExtra(USER_NAME2)
        val idRecibido=intent.getStringExtra(USER_ID2)
        val edadRecibida=intent.getIntExtra(USER_EDAD2,0)
        val alturaRecibida=intent.getIntExtra(USER_ALTURA2,0)
        val generoMasculinoRecibido=intent.getBooleanExtra(USER_GENERO2,true)
        val pesoRecibido=intent.getIntExtra(USER_PESO2,0)
        val diasSeleccionadosRecibido=intent.getIntArrayExtra(LISTA_DIAS2)
        val listaDiasSeleccionados=diasSeleccionadosRecibido?.toList()?:emptyList()

        binding.buttonContinuar3.setOnClickListener {
            var nivelActividad=-1
            for(i in 0..4){
                if(listaSeleccionado[i]==1){
                    nivelActividad=i
                    break
                }
            }
            if(nivelActividad==-1){
                Toast.makeText(this, "Por favor, introduce tu nivel de actividad.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                val id=idRecibido?:""
                val nombre=nombreRecibido?:""
                val correo=correoRecibido?:""
                val usuario= Usuario(id,nombre,correo,
                    edadRecibida,generoMasculinoRecibido,pesoRecibido,alturaRecibida,0,null, nivelActividad,0)
                val listaObjetosDias = listaDiasSeleccionados.map { diaIndice ->
                    DiasObjetivoUsuario(
                        usuarioId = id,
                        diaSemanaIndice = diaIndice
                    )
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val db = (applicationContext as DailyRepApp).database
                        db.usuarioDao().insertAll(usuario)
                        db.rachaDao().insertarDiasObjetivo(listaObjetosDias)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "¡Registro completado con éxito!", Toast.LENGTH_SHORT).show()
                            val intentCambioLogin: Intent = Intent(context, LoginActivity::class.java)
                            startActivity(intentCambioLogin)
                            finish()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error al guardar en BD: ${e.message}", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }
        }
    } }

    fun seleccionarActividad(){
        binding.sedentario.setOnClickListener {
            seleccionadoSedentario = !seleccionadoSedentario
            if (seleccionadoSedentario){
                listaSeleccionado[0]=1
                listaSeleccionado[1]=0
                listaSeleccionado[2]=0
                listaSeleccionado[3]=0
                listaSeleccionado[4]=0

                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            } else {
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }

        }
        binding.ligeramenteActivo.setOnClickListener {
            seleccionadoLigActivo = !seleccionadoLigActivo
            if (seleccionadoLigActivo){
                listaSeleccionado[0]=0
                listaSeleccionado[1]=1
                listaSeleccionado[2]=0
                listaSeleccionado[3]=0
                listaSeleccionado[4]=0
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            } else {
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }

        }
        binding.moderadamenteActivo.setOnClickListener {
            seleccionadoModActivo = !seleccionadoModActivo
            if (seleccionadoModActivo){
                listaSeleccionado[0]=0
                listaSeleccionado[1]=0
                listaSeleccionado[2]=1
                listaSeleccionado[3]=0
                listaSeleccionado[4]=0
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            } else {
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }

        }
        binding.muyActivo.setOnClickListener {
            seleccionadoMuyActivo = !seleccionadoMuyActivo
            if (seleccionadoMuyActivo){
                listaSeleccionado[0]=0
                listaSeleccionado[1]=0
                listaSeleccionado[2]=0
                listaSeleccionado[3]=1
                listaSeleccionado[4]=0
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            } else {
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }

        }
        binding.extremadamenteActivo.setOnClickListener {
            seleccionadoExtActivo = !seleccionadoExtActivo
            if (seleccionadoExtActivo){
                listaSeleccionado[0]=0
                listaSeleccionado[1]=0
                listaSeleccionado[2]=0
                listaSeleccionado[3]=0
                listaSeleccionado[4]=1
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.naranja))
                binding.sedentario.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.ligeramenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.moderadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
                binding.muyActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            } else {
                binding.extremadamenteActivo.setBackgroundColor(ContextCompat.getColor(this, R.color.plomo_oscuro))
            }

        }
    }
}