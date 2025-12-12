package com.example.dailyrep.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.DescripcionEjercicioActivity
import com.example.dailyrep.R
import com.example.dailyrep.databinding.ActivityAdapterEjercicioBinding
import com.example.dailyrep.dataclases.Ejercicio

class EjercicioAdapter(
    private var listaEjercicios: MutableList<Ejercicio> = mutableListOf<Ejercicio>(),
    private val onEjercicioClick: (Ejercicio) -> Unit): RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        context = parent.context
        return EjercicioViewHolder(
            ActivityAdapterEjercicioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: EjercicioAdapter.EjercicioViewHolder, position: Int) {
        val ejercicio=listaEjercicios[position]
        holder.binding(ejercicio)
        holder.itemView.setOnClickListener {
            onEjercicioClick(ejercicio)
        }
    }

    override fun getItemCount(): Int = listaEjercicios.size

    inner class EjercicioViewHolder(private val binding: ActivityAdapterEjercicioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(ejercicio: Ejercicio) {
            binding.nombreEjercicio.text=ejercicio.nombre
            if(ejercicio.favorito){
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

        }
    }
    fun ponerListaEjercicios(nuevaLista: List<Ejercicio>) {
        listaEjercicios.clear()
        listaEjercicios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}

