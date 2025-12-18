package com.example.dailyrep.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyrep.R
import com.example.dailyrep.databinding.SerieAdapterBinding
import com.example.dailyrep.dataclases.SeriePlanificada

class SerieAdapter(private val onCheckClick: (SeriePlanificada)->Unit)
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
            val textoSerie="Serie ${seriePlanificada.numeroSerie}"
            binding.numeroSerie.setText(textoSerie)
            binding.reps.hint=seriePlanificada.repeticiones.toString()
            binding.check.setOnClickListener {
                onCheckClick(seriePlanificada)
            }
            if(seriePlanificada.completado){
                val color = ContextCompat.getColor(context, R.color.naranja)

                binding.check.imageTintList = ColorStateList.valueOf(color)
            }else{
                val color = ContextCompat.getColor(context, R.color.plomo)

                binding.check.imageTintList = ColorStateList.valueOf(color)
            }
        }
    }
    fun ponerListaSeries(nuevaLista: List<SeriePlanificada>) {
        listaSeries.clear()
        listaSeries.addAll(nuevaLista)
        notifyDataSetChanged()
    }
    }
