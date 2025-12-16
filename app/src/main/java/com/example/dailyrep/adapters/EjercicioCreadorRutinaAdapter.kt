package com.example.dailyrep.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.DescripcionEjercicioActivity
import com.example.dailyrep.R
import com.example.dailyrep.databinding.ActivityEjercicioCreadorRutinaAdapterBinding
import com.example.dailyrep.dataclases.Ejercicio

class EjercicioCreadorRutinaAdapter(
    private var listaEjercicios: MutableList<Ejercicio> = mutableListOf<Ejercicio>(),
    private val onSeleccionCambio: (Ejercicio, Boolean) -> Unit): RecyclerView.Adapter<EjercicioCreadorRutinaAdapter.EjercicioCreadorRutinaViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioCreadorRutinaViewHolder {
        context = parent.context
        return EjercicioCreadorRutinaViewHolder(
            ActivityEjercicioCreadorRutinaAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: EjercicioCreadorRutinaAdapter.EjercicioCreadorRutinaViewHolder, position: Int) {
        val ejercicio = listaEjercicios[position]
        holder.binding(ejercicio)
    }

    override fun getItemCount(): Int = listaEjercicios.size

    inner class EjercicioCreadorRutinaViewHolder(private val binding: ActivityEjercicioCreadorRutinaAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(ejercicio: Ejercicio) {
            binding.nombreEjercicio.text=ejercicio.nombre
            if(ejercicio.esFavorito){
                binding.corazon.setImageResource(R.drawable.heart)
            }else {
                binding.corazon.setImageResource(R.drawable.corazon_vacio)
            }
            val iconoParteCuerpo: Int = when(ejercicio.parteCuerpo){
                "Brazos"->R.drawable.brazos
                "Espalda"->R.drawable.espalda
                "Abdominales"->R.drawable.abdominales
                "Pierna"->R.drawable.piernas
                "Hombro"->R.drawable.hombros
                "Pecho"-> R.drawable.pectoral
                else -> {
                    R.drawable.tipo_ejercicio
                }
            }
            val iconoTipoEjercicio: Int = when(ejercicio.tipo){
                "Mancuernas"->R.drawable.mancuernas
                "Polea"->R.drawable.polea
                "Maquina"->R.drawable.maquina
                "Peso Corporal"->R.drawable.cuerpo
                "Barra"->R.drawable.barra
                "Cardio"->R.drawable.cardio
                else -> {
                    R.drawable.tipo_ejercicio
                }
            }
            binding.parteCuerpo.setImageResource(iconoParteCuerpo)
            binding.tipoEjercicio.setImageResource(iconoTipoEjercicio)

            val iconoSeleccionado = R.drawable.listo
            val iconoDeseleccionado = R.drawable.circulo_vacio

            if (ejercicio.seleccionado) {
                binding.seleccionarEjercicio.setImageResource(iconoSeleccionado)
            } else {
                binding.seleccionarEjercicio.setImageResource(iconoDeseleccionado)
            }

            binding.seleccionarEjercicio.setOnClickListener {
                ejercicio.seleccionado = !ejercicio.seleccionado
                if (ejercicio.seleccionado) {
                    binding.seleccionarEjercicio.setImageResource(iconoSeleccionado)
                    onSeleccionCambio(ejercicio, true)
                } else {
                    binding.seleccionarEjercicio.setImageResource(iconoDeseleccionado)
                    onSeleccionCambio(ejercicio, false)
                }
            }
        }
    }
    fun ponerListaEjercicios(nuevaLista: List<Ejercicio>) {
        listaEjercicios.clear()
        listaEjercicios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}

