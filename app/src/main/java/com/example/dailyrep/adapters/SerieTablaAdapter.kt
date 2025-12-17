package com.example.dailyrep.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.databinding.ActivitySerieTablaAdapterBinding
import com.example.dailyrep.databinding.SerieAdapterBinding
import com.example.dailyrep.dataclases.SeriePlanificada

class SerieTablaAdapter
    : RecyclerView.Adapter<SerieTablaAdapter.SerieTablaViewHolder>(){
    private var context: Context? = null
    private var listaSeries: MutableList<SeriePlanificada> = mutableListOf<SeriePlanificada>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SerieTablaViewHolder {
        context = parent.context
        return SerieTablaViewHolder(
            ActivitySerieTablaAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: SerieTablaAdapter.SerieTablaViewHolder, position: Int) {
        val serie=listaSeries[position]
        holder.binding(serie)

    }

    override fun getItemCount(): Int = listaSeries.size

    inner class SerieTablaViewHolder(private val binding: ActivitySerieTablaAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(seriePlanificada: SeriePlanificada) {
            binding.tvSerie.text=seriePlanificada.numeroSerie.toString()
            binding.etPeso.hint=seriePlanificada.peso.toString()
            binding.etReps.hint=seriePlanificada.repeticiones.toString()
        }
    }
    fun ponerListaSeries(nuevaLista: List<SeriePlanificada>) {
        listaSeries.clear()
        listaSeries.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
