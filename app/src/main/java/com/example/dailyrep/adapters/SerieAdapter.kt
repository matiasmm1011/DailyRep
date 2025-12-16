package com.example.dailyrep.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.databinding.SerieAdapterBinding
import com.example.dailyrep.dataclases.SeriePlanificada

class SerieAdapter
    : RecyclerView.Adapter<SerieAdapter.SerieViewHolder>(){
    private var context: Context? = null
    private var listaSeries: MutableList<SeriePlanificada> = mutableListOf<SeriePlanificada>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SerieViewHolder {
        context = parent.context
        return SerieViewHolder(
            SerieAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: SerieAdapter.SerieViewHolder, position: Int) {
        val serie=listaSeries[position]
        holder.binding(serie)

    }

    override fun getItemCount(): Int = listaSeries.size

    inner class SerieViewHolder(private val binding: SerieAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(seriePlanificada: SeriePlanificada) {
            binding.peso.hint=seriePlanificada.peso.toString()
            binding.reps.hint=seriePlanificada.repeticiones.toString()
        }
    }
    fun ponerListaSeries(nuevaLista: List<SeriePlanificada>) {
        listaSeries.clear()
        listaSeries.addAll(nuevaLista)
        notifyDataSetChanged()
    }
    }
