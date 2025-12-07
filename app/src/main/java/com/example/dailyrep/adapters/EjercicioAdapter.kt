package com.example.dailyrep.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.R
import com.example.dailyrep.databinding.ActivityAdapterEjercicioBinding
import com.example.dailyrep.dataclases.Ejercicio

class EjercicioAdapter: RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {
    private var context: Context? = null
    private val listaEjercicios = mutableListOf<Ejercicio>()
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
        holder.binding(listaEjercicios[position])
    }

    override fun getItemCount(): Int = listaEjercicios.size

    inner class EjercicioViewHolder(private val binding: ActivityAdapterEjercicioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(ejercicio: Ejercicio) {
            binding.nombreEjercicio.text=ejercicio.nombre
            if(ejercicio.favorito){
                binding.corazon.setImageResource(R.drawable.corazon)

            }else {
                binding.corazon.setImageResource(R.drawable.corazon_vacio)
            }
            val parteCuerpo:String= ejercicio.parteCuerpo

            binding.root.setOnClickListener {
            }
        }
    }
    fun ponerListaEjercicios(nuevaLista: List<Ejercicio>) {
        listaEjercicios.clear()
        listaEjercicios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}

